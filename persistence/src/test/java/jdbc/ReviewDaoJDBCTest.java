package jdbc;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.jdbc.ReviewDaoJDBC;
import config.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.*;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:review_test.sql")
public class ReviewDaoJDBCTest {
    private static final User USER1 = new User(
            1,
            "franquesada@gmail.com",
            "Francisco Quesada",
            "1147895678",
            true,
            true
    );
    private static final List<UserAuth.Role> USER1_ROLES = Arrays.asList(UserAuth.Role.CLIENT, UserAuth.Role.PROFESSIONAL);
    private static final User USER2 = new User(
            2,
            "manurodriguez@gmail.com",
            "Manuel Rodriguez",
            "1109675432",
            true,
            true
    );
    private static final List<UserAuth.Role> USER2_ROLES = Arrays.asList(UserAuth.Role.CLIENT);
    private static final List<JobPost.Zone> ZONES = new ArrayList<>(Arrays.asList(JobPost.Zone.values()[1], JobPost.Zone.values()[2]));
    private static final JobPost JOB_POST = new JobPost(1, USER1, "Electricista Matriculado", "Lun a Viernes 10hs - 14hs", JobPost.JobType.values()[1], ZONES, 0.0, true);
    private static final JobPackage JOB_PACKAGE = new JobPackage(
            1,
            1,
            "Trabajo Simple",
            "Arreglos de tomacorrientes",
            200.00, JobPackage.RateType.values()[0],
            true
    );
    private static final JobContract JOB_CONTRACT = new JobContract(
            1, USER2,
            JOB_PACKAGE,
            USER1,
            new Date(),
            "Se me rompio una zapatilla"
    );

    private static final Review REVIEW_1 = new Review(
            4,
            "Muy bueno",
            "Resolvio todo en cuestion de minutos", USER2, JOB_POST
    );
    private static final Review REVIEW_2 = new Review(
            2, "Medio pelo", "Resolvio todo de forma ideal", USER2, JOB_POST
    );

    @Autowired
    private DataSource ds;

    @InjectMocks
    @Autowired
    private ReviewDaoJDBC reviewDaoJDBC;

//    TODO: FIX tests
//    @Test
//    public void testFindAllReviews() {
//        List<Review> maybeUserReviews = reviewDaoJDBC.findReviewsByPostId(JOB_POST.getId(), 0);
//
//        Assert.assertEquals(2, maybeUserReviews.size());
//        Assert.assertEquals(REVIEW_1, maybeUserReviews.get(0));
//        Assert.assertEquals(REVIEW_2, maybeUserReviews.get(1));
//    }
//
//
//    @Test
//    public void findProfessionalReviews() {
//        List<Review> maybeUserReviews = reviewDaoJDBC.findProfessionalReviews(USER1.getId(), 0);
//
//        Assert.assertEquals(2, maybeUserReviews.size());
//        Assert.assertEquals(REVIEW_1, maybeUserReviews.get(0));
//        Assert.assertEquals(REVIEW_2, maybeUserReviews.get(1));
//    }
//
//    @Test
//    public void testFindReviews() {
//        List<Review> maybeReviews = reviewDaoJDBC.findReviewsByPackageId(JOB_PACKAGE.getId(), 0);
//
//        Assert.assertEquals(2, maybeReviews.size());
//        Assert.assertEquals(REVIEW_1, maybeReviews.get(0));
//        Assert.assertEquals(REVIEW_2, maybeReviews.get(1));
//    }

}
