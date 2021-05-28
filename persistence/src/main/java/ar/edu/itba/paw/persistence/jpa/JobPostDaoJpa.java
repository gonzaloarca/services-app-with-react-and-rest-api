package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.HirenetUtils;
import ar.edu.itba.paw.interfaces.dao.JobPostDao;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.User;
import exceptions.UserNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JobPostDaoJpa implements JobPostDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public JobPost create(long userId, String title, String availableHours, JobPost.JobType jobType, List<JobPost.Zone> zones) {
        User user = em.find(User.class, userId);
        if (user == null)
            throw new UserNotFoundException();

        JobPost jobPost = new JobPost(user, title, availableHours, jobType, zones, LocalDateTime.now());
        em.persist(jobPost);
        return jobPost;
    }

    @Override
    public Optional<JobPost> findById(long id) {
        return em.createQuery("FROM JobPost AS jp WHERE jp.id = :id AND jp.isActive = TRUE", JobPost.class)
                .setParameter("id", id).getResultList().stream().findFirst();
    }

    @Override
    public Optional<JobPost> findByIdWithInactive(long id) {
        return Optional.ofNullable(em.find(JobPost.class, id));
    }

    @Override
    public List<JobPost> findByUserId(long id, int page) {
        Query nativeQuery = em.createNativeQuery("SELECT post_id FROM job_post WHERE user_id = :id AND post_is_active = TRUE")
                .setParameter("id", id);

        return executePageQuery(page, nativeQuery);
    }

    @Override
    public int findSizeByUserId(long id) {
        return em.createQuery("SELECT COUNT(*) FROM JobPost jpost WHERE jpost.user.id = :id AND jpost.isActive = TRUE", Long.class)
                .setParameter("id", id).getResultList().stream().findFirst().orElse(0L).intValue();
    }

    @Override
    public List<JobPost> findByJobType(JobPost.JobType jobType, int page) {
        Query nativeQuery = em.createNativeQuery(
                "SELECT post_id FROM job_post WHERE post_job_type = :jobType AND post_is_active = TRUE"
        ).setParameter("jobType", jobType.getValue());

        return executePageQuery(page, nativeQuery);
    }

    @Override
    public List<JobPost> findByZone(JobPost.Zone zone, int page) {
        Query nativeQuery = em.createNativeQuery(
                "SELECT post_id FROM job_post NATURAL JOIN post_zone" +
                        " WHERE zone_id = :zone AND post_is_active = TRUE"
        ).setParameter("zone", zone.getValue());

        return executePageQuery(page, nativeQuery);
    }


    @Override
    public List<JobPost> findAll(int page) {
        Query nativeQuery = em.createNativeQuery(
                "SELECT post_id FROM job_post WHERE post_is_active = TRUE"
        );

        return executePageQuery(page, nativeQuery);
    }

    @Override
    public Optional<User> findUserByPostId(long id) {
        return em.createQuery(
                "SELECT jp.user FROM JobPost jp WHERE jp.id = :id", User.class
        ).setParameter("id", id).getResultList().stream().findFirst();
    }

    @Override
    public boolean updateById(long id, String title, String availableHours, JobPost.JobType jobType, List<JobPost.Zone> zones) {
        JobPost jobPost = em.find(JobPost.class, id);
        if (jobPost != null) {
            jobPost.setTitle(title);
            jobPost.setAvailableHours(availableHours);
            jobPost.setJobType(jobType);
            jobPost.setZones(zones);
            em.persist(jobPost);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteJobPost(long id) {
        JobPost jobPost = em.find(JobPost.class, id);
        if (jobPost != null) {
            jobPost.setActive(false);
            em.persist(jobPost);
            return true;
        }
        return false;
    }

    private List<JobPost> executePageQuery(int page, Query nativeQuery) {
        if (page != HirenetUtils.ALL_PAGES) {
            nativeQuery.setFirstResult((page) * HirenetUtils.PAGE_SIZE);
            nativeQuery.setMaxResults(HirenetUtils.PAGE_SIZE);
        }

        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream()
                .map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());

        if (filteredIds.isEmpty())
            return new ArrayList<>();

        return em.createQuery("FROM JobPost AS jp WHERE jp.id IN :filteredIds", JobPost.class)
                .setParameter("filteredIds", filteredIds).getResultList().stream().sorted(
                        //Ordenamos los elementos segun el orden de filteredIds
                        Comparator.comparingInt(o -> filteredIds.indexOf(o.getId()))
                ).collect(Collectors.toList());
    }
}
