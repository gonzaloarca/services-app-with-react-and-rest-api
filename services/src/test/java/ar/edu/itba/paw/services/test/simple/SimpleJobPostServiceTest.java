package ar.edu.itba.paw.services.test.simple;

import ar.edu.itba.paw.interfaces.dao.JobPostDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.JobPostNotFoundException;
import ar.edu.itba.paw.services.simple.SimpleJobPostService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class SimpleJobPostServiceTest {

    private static final User EXISTING_USER = new User(
            1,
            "fquesada@gmail.com",
            "Francisco Quesada",
            "11-4578-9087",
            true,
            true,
            LocalDateTime.now());
    private static final User EXISTING_USER_TO_PROF = new User(
            1,
            "fquesada@gmail.com",
            "Francisco Quesada",
            "11-4578-9087",
            true,
            true,
            LocalDateTime.now());
    private static final User NEW_PROFESSIONAL = new User(
            1,
            "mrodriguez@gmail.com",
            "Manuel Rodriguez",
            "11-5678-4353",
            true,
            true,
            LocalDateTime.now());
    private static final List<JobPost.Zone> ZONES =
            new ArrayList<>(Arrays.asList(JobPost.Zone.values()[1],
                    JobPost.Zone.values()[2]));
    private static final JobPost JOB_POST_NEW_USER = new JobPost(
            1,
            NEW_PROFESSIONAL,
            "Electricista a domicilio",
            "Luna a viernes 10 a 14",
            JobPost.JobType.ELECTRICITY,
            ZONES,
            true,
            LocalDateTime.now());
    private static final JobPost JOB_POST_EXISTING_USER = new JobPost(
            1,
            EXISTING_USER_TO_PROF,
            "Electricista a domicilio",
            "Luna a viernes 10 a 14",
            JobPost.JobType.ELECTRICITY,
            ZONES,
            true,
            LocalDateTime.now());
    private final long FAKE_ID = 999L;

    @InjectMocks
    private final SimpleJobPostService jobPostService = new SimpleJobPostService();

    @Mock
    private UserService userService;

    @Mock
    private JobPostDao jobPostDao;

    @Test
    public void testCreatePostNewUser() {
        Mockito.when(userService.findByEmail(NEW_PROFESSIONAL.getEmail()))
                .thenReturn(Optional.of(NEW_PROFESSIONAL));
        Mockito.when(jobPostDao.create(NEW_PROFESSIONAL.getId(), JOB_POST_NEW_USER.getTitle(), JOB_POST_NEW_USER.getAvailableHours(), JOB_POST_NEW_USER.getJobType(), ZONES))
                .thenReturn(JOB_POST_NEW_USER);
        int[] zonesInt = new int[ZONES.size()];
        for (int i = 0; i < ZONES.size(); i++) {
            zonesInt[i] = ZONES.get(i).getValue();
        }
        JobPost jobPost = jobPostService.create(NEW_PROFESSIONAL.getEmail(), JOB_POST_NEW_USER.getTitle(),
                JOB_POST_NEW_USER.getAvailableHours(), JOB_POST_NEW_USER.getJobType().ordinal(), zonesInt);

        Assert.assertEquals(jobPost, JOB_POST_NEW_USER);

    }

    @Test
    public void testCreatePostExistingUserNoProf() {
        Mockito.when(userService.findByEmail(EXISTING_USER.getEmail()))
                .thenReturn(Optional.of(EXISTING_USER));
        Mockito.when(jobPostDao.create(EXISTING_USER.getId(), JOB_POST_EXISTING_USER.getTitle(), JOB_POST_EXISTING_USER.getAvailableHours(), JOB_POST_EXISTING_USER.getJobType(), ZONES))
                .thenReturn(JOB_POST_EXISTING_USER);
        int[] zonesInt = new int[ZONES.size()];
        for (int i = 0; i < ZONES.size(); i++) {
            zonesInt[i] = ZONES.get(i).getValue();
        }
        JobPost jobPost = jobPostService.create(EXISTING_USER.getEmail(), JOB_POST_EXISTING_USER.getTitle(), JOB_POST_EXISTING_USER.getAvailableHours(), JOB_POST_EXISTING_USER.getJobType().ordinal(), zonesInt);

        Assert.assertEquals(JOB_POST_EXISTING_USER, jobPost);
        Assert.assertEquals(0, 0);
    }

    @Test(expected = JobPostNotFoundException.class)
    public void updateJobPostExceptionTest() {
        Mockito.when(jobPostDao.updateById(Mockito.eq(JOB_POST_EXISTING_USER.getId()), Mockito.anyString(), Mockito.anyString(),
                Mockito.any(), Mockito.anyList()))
                .thenReturn(false);

        jobPostService.updateJobPost(JOB_POST_EXISTING_USER.getId(), JOB_POST_EXISTING_USER.getTitle(),
                JOB_POST_EXISTING_USER.getAvailableHours(), JOB_POST_EXISTING_USER.getJobType().getValue(),
                new int[]{});
    }

    @Test(expected = JobPostNotFoundException.class)
    public void deleteJobPostExceptionTest() {
        Mockito.when(jobPostDao.deleteJobPost(Mockito.eq(FAKE_ID)))
                .thenReturn(false);

        jobPostService.deleteJobPost(FAKE_ID);
    }
}
