package ar.edu.itba.paw.services.nologin;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.MailingService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.VerificationTokenService;
import ar.edu.itba.paw.models.ByteImage;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserAuth;
import ar.edu.itba.paw.models.VerificationToken;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import exceptions.UserNotVerifiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoLoginUserService implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private MailingService mailingService;

    @Override
    public User register(String email, String password, String username, String phone, List<Integer> roles,
                         ByteImage image) throws UserAlreadyExistsException, UserNotVerifiedException{
        Optional<User> maybeUser = userDao.findByEmail(email);

        //TODO Logica de usuario legacy???
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            if (user.isVerified())
                throw new UserAlreadyExistsException();

            VerificationToken token = verificationTokenService.create(user);
            mailingService.sendVerificationTokenEmail(user, token);
            throw new UserNotVerifiedException();
        }

        User registeredUser;
        if(image == null)
            registeredUser = userDao.register(email, password, username, phone);
        else
            registeredUser = userDao.register(email, password, username, phone, image);

        //TODO manejo de roles
        roles.forEach(role -> userDao.assignRole(registeredUser.getId(), role));

        VerificationToken token = verificationTokenService.create(registeredUser);
        mailingService.sendVerificationTokenEmail(registeredUser, token);

        return registeredUser;
    }

    @Override
    public User register(String email, String password, String username, String phone, List<Integer> role) {
        return register(email, password, username, phone, role, null);
    }

    @Override
    public User findById(long id) {
        return userDao.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User updateUserByEmail(String email, String phone, String name) {
        return userDao.updateUserByEmail(email, phone, name).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User updateUserById(long id, String name, String phone) {
        return userDao.updateUserById(id, name, phone).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User updateUserById(long id, String name, String phone, ByteImage byteImage) {
        return userDao.updateUserById(id, name, phone, byteImage).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User getUserByRoleAndId(int role, long id) {
        return userDao.findUserByRoleAndId(UserAuth.Role.values()[role], id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Optional<UserAuth> getAuthInfo(String email) {
        return userDao.findAuthInfo(email);
    }

    @Override
    public void assignRole(long id, int role) {
        List<UserAuth.Role> roles = userDao.findRoles(id);
        if (!roles.contains(UserAuth.Role.values()[role]))
            userDao.assignRole(id, role);
    }

    @Override
    public boolean validCredentials(String email, String password) {
        UserAuth auth = userDao.findAuthInfo(email).orElseThrow(UserNotFoundException::new);
        return passwordEncoder.matches(password,auth.getPassword());
    }

    @Override
    public void changeUserPassword(String email, String password) {
        User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
        userDao.changeUserPassword(user.getId(), passwordEncoder.encode(password));
    }

}
