package nologin;

import ar.edu.itba.paw.interfaces.services.MailingService;
import ar.edu.itba.paw.interfaces.services.VerificationTokenService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.persistence.jdbc.UserDaoJDBC;
import ar.edu.itba.paw.services.simple.SimpleUserService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class SimpleUserServiceTest {

    private static final User EXISTING_USER = new User(
            1,
            "fquesada@gmail.com",
            "Francisco Quesada",
            "11-4578-9087",
            true,
            true,
            LocalDateTime.now());
    private static final User NEW_USER = new User(
            1,
            "mrodriguez@gmail.com",
            "Manuel Rodriguez",
            "11-5678-4353",
            true,
            true,
            LocalDateTime.now());
    private static final VerificationToken TOKEN = new VerificationToken("", NEW_USER, Instant.now());

    @InjectMocks
    private SimpleUserService userService = new SimpleUserService();

    @Mock
    private MailingService mailingService;  //necesario que este como Mock para el testRegisterNewUser

    @Mock
    private UserDaoJDBC userDaoJDBC;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationTokenService verificationTokenService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testRegisterNewUser() {
        Mockito.when(userDaoJDBC.register(Mockito.eq(NEW_USER.getEmail()),Mockito.eq(""),Mockito.eq(NEW_USER.getUsername()),
                Mockito.eq(NEW_USER.getPhone()))).thenReturn(NEW_USER);
        Mockito.when(passwordEncoder.encode(Mockito.eq(""))).thenReturn("");
        Mockito.when(verificationTokenService.create(Mockito.eq(NEW_USER))).thenReturn(TOKEN);

        User createdUser = userService.register(NEW_USER.getEmail(),"",NEW_USER.getUsername(),NEW_USER.getPhone());
        Assert.assertNotNull(createdUser);
        Assert.assertEquals(NEW_USER,createdUser);
    }

    @Test
    public void testRegisterUserAlreadyCreated(){
        exceptionRule.expect(RuntimeException.class);
        Mockito.when(
                userDaoJDBC.register(
                        Mockito.eq(EXISTING_USER.getEmail()),
                        "",
                        Mockito.eq(EXISTING_USER.getUsername()),
                        Mockito.eq(EXISTING_USER.getPhone())
                )
        ).thenThrow(
                new RuntimeException()
        );
        userDaoJDBC.register(EXISTING_USER.getEmail(),"",EXISTING_USER.getUsername(),EXISTING_USER.getPhone());
    }


}
