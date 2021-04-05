package ar.edu.itba.paw.services.nologin;

import ar.edu.itba.paw.interfaces.dao.JobPostDao;
import ar.edu.itba.paw.interfaces.services.JobPackageService;
import ar.edu.itba.paw.interfaces.services.JobPostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.JobPackage;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoLoginJobPostService implements JobPostService {

    @Autowired
    private JobPostDao jobPostDao;

    @Autowired
    private UserService userService;

    @Autowired
    private JobPackageService jobPackageService;

    @Override
    public JobPost create(String email, String username, String phone, String title, String availableHours, JobPost.JobType jobType, List<JobPost.Zone> zones) {

        //Chequeamos si el user esta registrado
        Optional<User> maybeUser = userService.findByEmail(email);
        User user = null;
        //Si existe vemos si es profesional, si no lo es lo hacemos
        if (maybeUser.isPresent()) {
            if (!maybeUser.get().isProfessional()) {
                Optional<User> newRole = userService.switchRole(maybeUser.get().getId());
                user = newRole.orElse(maybeUser.get());
            }
        } else {
            user = userService.register(email, username, phone, true);
        }
        if (user == null)
            //TODO: LANZAR EXCEPCION APROPIADA
            throw new RuntimeException();
        return jobPostDao.create(user.getId(), title, availableHours, jobType, zones);

    }

    @Override
    public Optional<JobPost> findById(long id) {
        return jobPostDao.findById(id);
    }

    @Override
    public Optional<List<JobPost>> findByUserId(long id) {
        return jobPostDao.findByUserId(id);
    }

    @Override
    public Optional<List<JobPost>> findByJobType(JobPost.JobType jobType) {
        return jobPostDao.findByJobType(jobType);
    }

    @Override
    public Optional<List<JobPost>> findByZone(JobPost.Zone zone) {
        return jobPostDao.findByZone(zone);
    }

    @Override
    public Optional<List<JobPost>> findAll() {
        return jobPostDao.findAll();
    }

    @Override
    public Map<JobPost, JobPackage> findAllWithCheapierPackage() {
        Map<JobPost, JobPackage> answer = new HashMap<>();
        Optional<List<JobPost>> jobPosts = findAll();

        if (!jobPosts.isPresent())
            return answer;

        jobPosts.get().forEach(jobPost -> {
            Optional<List<JobPackage>> aux = jobPackageService.findByPostId(jobPost.getId());
            //TODO: SI NO ESTA PRESENTE ARROJAR EXCEPCION?
            aux.ifPresent(jobPackages -> answer.put(jobPost,
                    jobPackages.stream().min(Comparator.comparingDouble(JobPackage::getPrice)).get()));
        });

        return answer;
    }

    @Override
    public Optional<List<JobPost>> search(String title, JobPost.Zone zone) {
        return jobPostDao.search(title, zone);
    }
}
