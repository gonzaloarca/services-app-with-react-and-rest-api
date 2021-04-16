package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserAuth;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User register(String email,String password, String username, String phone, List<Integer> role);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    Optional<User> updateUserByEmail(String email,String phone, String name);

    Optional<User> updateUserByid(long id,String phone, String name);

    Optional<UserAuth> getAuthInfo(String email);

}
