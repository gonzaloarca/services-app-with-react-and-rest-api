package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoJpa implements UserDao {

    @PersistenceContext
    private EntityManager em;
    private Query  query;

    @Override
    public User register(String email, String password, String username, String phone) {
        return register(email, password, username, phone, null);
    }

    @Override
    public User register(String email, String password, String username, String phone, ByteImage image) {
        final UserWithImage userWithImage = new UserWithImage(email, username, phone, true, false, image, LocalDateTime.now(), password);
        em.persist(userWithImage);
        return new User(userWithImage);
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return em.createQuery("FROM User AS u WHERE u.email = :email", User.class)
                .setParameter("email", email).getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> updateUserByEmail(String email, String phone, String name) {
        Optional<User> aux = findByEmail(email);
        if (aux.isPresent()) {
            aux.get().setUsername(name);
            aux.get().setPhone(phone);
            em.persist(aux.get());
        }
        return aux;
    }

    @Override
    public Optional<User> updateUserById(long id, String name, String phone) {
        User aux = em.find(User.class, id);
        if (aux != null) {
            aux.setUsername(name);
            aux.setPhone(phone);
            em.persist(aux);
        }
        return Optional.ofNullable(aux);
    }

    @Override
    public Optional<User> updateUserById(long id, String name, String phone, ByteImage image) {
        UserWithImage userWithImage = em.find(UserWithImage.class, id);

        if(userWithImage == null)
            return Optional.empty();

        userWithImage.setUsername(name);
        userWithImage.setPhone(phone);
        userWithImage.setByteImage(image);

        em.persist(userWithImage);

        return Optional.of(new User(userWithImage));
    }

    @Override
    public Optional<UserAuth> findAuthInfo(String email) {
        return em.createQuery("FROM UserAuth AS u JOIN FETCH u.roles WHERE u.email = :email", UserAuth.class)
                .setParameter("email", email).getResultList().stream().findFirst();
    }

    @Override
    public Optional<UserAuth> assignRole(long id, int role) {
        UserAuth userAuth = em.find(UserAuth.class, id);
        UserAuth.Role aux_role = UserAuth.Role.values()[role];
        if (userAuth != null && !userAuth.getRoles().contains(aux_role)) {
            userAuth.getRoles().add(aux_role);
            em.persist(userAuth);
        }
        return Optional.ofNullable(userAuth);
    }

    @Override
    public List<UserAuth.Role> findRoles(long id) {
        List<UserAuth> resultList = em.createQuery("FROM UserAuth AS u WHERE u.id = :id", UserAuth.class)
                .setParameter("id", id).getResultList();
        return resultList.isEmpty() ? Collections.emptyList() : resultList.get(0).getRoles();
    }

    @Override
    public Optional<User> findUserByRoleAndId(UserAuth.Role role, long id) {
        return em.createQuery(
                "FROM User u WHERE u.id = :user_id AND EXISTS(FROM UserAuth ua WHERE ua.id = u.id AND :role_id IN elements(ua.roles))"
                , User.class)
                .setParameter("user_id", id).setParameter("role_id", role).
                        getResultList().stream().findFirst();
    }

    @Override
    public boolean changeUserPassword(long id, String password) {
        User aux = em.find(User.class, id);
        if (aux != null) {
            aux.setPassword(password);
            em.persist(aux);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyUser(long id) {
        User aux = em.find(User.class, id);
        if (aux != null) {
            aux.setVerified(true);
            em.persist(aux);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            user.setActive(false);
            em.persist(user);
            return true;
        }
        return false;
    }

    @Override
    public List<JobPost.JobType> findUserJobTypes(long id) {
        @SuppressWarnings("unchecked")
        List<JobPost.JobType> resultList = em.createQuery(
                "SELECT DISTINCT jp.jobType FROM JobPost jp WHERE jp.user.id = :id"
        ).setParameter("id", id).getResultList();
        return resultList;
    }

    @Override
    public int findUserRankingInJobType(long id, JobPost.JobType jobType) {

        String sqlQuery = new StringBuilder()
                .append("SELECT CAST(rank AS INT) FROM (")
                .append("         SELECT user_id, ROW_NUMBER() OVER () AS rank")
                .append("         FROM (SELECT user_id")
                .append("               FROM job_cards")
                .append("               WHERE post_job_type = :jobType")
                .append("               GROUP BY user_id")
                .append("               ORDER BY SUM(post_contract_count) DESC")
                //    ORDERBY esta en una subquery para que funcione correctamente con HSQLDB
                .append("              ) AS users_ordered_by_contract")
                .append("     ) AS rankings")
                .append(" WHERE user_id = :id")
                .toString();

        final Query query = em.createNativeQuery(sqlQuery);
        @SuppressWarnings("unchecked")
        Integer result = (Integer) query.setParameter("jobType", jobType.getValue())
                .setParameter("id", id).getResultList().stream().findFirst().orElse(0);

        return result;
    }

    @Override
    public Optional<UserWithImage> findUserWithImage(long id) {
        return Optional.ofNullable(em.find(UserWithImage.class, id));
    }

    @Override
    public Optional<ByteImage> findImageByUserId(long id){
        List<ByteImage> resultList = em.createQuery("SELECT u.byteImage FROM UserWithImage u WHERE u.id = :id", ByteImage.class).setParameter("id", id).getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.ofNullable(resultList.get(0));
    }
}
