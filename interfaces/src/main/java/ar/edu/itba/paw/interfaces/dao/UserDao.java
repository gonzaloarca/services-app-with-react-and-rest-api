package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserAuth;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User register(String email,String password, String username, String phone);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    Optional<User> updateUserByEmail(String email,String phone, String name);

    Optional<User> updateUserById(long id, String phone, String name);

    Optional<UserAuth> findAuthInfo(String email);

    void assignRole(long id, int role);

    List<UserAuth.Role> findRoles(long id);

    List<Review> findUserReviews(long id);

    Optional<User> findUserByRoleAndId(UserAuth.Role role, long id);

    Double findProfessionalAvgRate(long id);


}
