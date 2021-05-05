package jdbc;

import ar.edu.itba.paw.interfaces.HirenetUtils;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.jdbc.*;
import config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:job_card_data_test.sql")
public class JobCardDaoJDBCTest {

    private static final User USER1 = new User(
            1,
            "franquesada@gmail.com",
            "Francisco Quesada",
            "1147895678",
            true,
            true,
            LocalDateTime.now());
    private static final User USER2 = new User(
            3,
            "gonzaarca@gmail.com",
            "Gonzalo Arca",
            "0549940406521",
            true,
            true,
            LocalDateTime.now());
    private static final User USER3 = new User(
            8,
            "npapa@gmail.com",
            "Nicolas Papa",
            "09876654321354",
            true,
            true,
            LocalDateTime.now());
    private static final User USER4 = new User(
            11,
            "tren@gmail.com",
            "Soledad del Cielo",
            "87876767",
            true,
            true,
            LocalDateTime.now());

    private static final List<JobPost.Zone> ZONES_USER = new ArrayList<>(
            Arrays.asList(
                    JobPost.Zone.values()[1],
                    JobPost.Zone.values()[2]
            )
    );
    private static final JobPost JOB_POST_USER2 = new JobPost(
            11,
            USER2,
            "Electricista no matriculado", "Lun a Jueves 13hs - 14hs",
            JobPost.JobType.values()[1],
            ZONES_USER,
            0.0,
            true,
            LocalDateTime.now());
    private static final JobPackage JOB_PACKAGE_USER2 = new JobPackage(
            19,
            JOB_POST_USER2.getId(),
            "Trabajo simple",
            "Arreglos de tomacorrientes",
            200.00, JobPackage.RateType.values()[0],
            true
    );
    private static final JobCard JOB_CARD_USER2 = new JobCard(
            JOB_POST_USER2,
            JOB_PACKAGE_USER2.getRateType(),
            JOB_PACKAGE_USER2.getPrice(),
            null,
            1,
            0
    );
    private static final JobPost JOB_POST_USER3 = new JobPost(
            12,
            USER3,
            "Paseador de gatos", "Sabados de 8hs - 14hs",
            JobPost.JobType.values()[3],
            ZONES_USER,
            0.0,
            true,
            LocalDateTime.now());
    private static final JobPackage JOB_PACKAGE_USER3 = new JobPackage(
            20,
            JOB_POST_USER3.getId(),
            "Trabajo simple",
            "Paseo tardio",
            300.00, JobPackage.RateType.values()[0],
            true
    );
    private static final JobCard JOB_CARD_USER3 = new JobCard(
            JOB_POST_USER3,
            JOB_PACKAGE_USER3.getRateType(),
            JOB_PACKAGE_USER3.getPrice(),
            null,
            2,
            0
    );
    private static final JobPost JOB_POST_USER4 = new JobPost(
            13,
            USER4,
            "Paseador de urones", "Domingos de 8hs - 14hs",
            JobPost.JobType.values()[3],
            ZONES_USER,
            0.0,
            true,
            LocalDateTime.now());
    private static final JobPackage JOB_PACKAGE_USER4 = new JobPackage(
            21,
            JOB_POST_USER4.getId(),
            "Trabajo simple",
            "Paseo recreativo",
            300.00, JobPackage.RateType.values()[0],
            true
    );
    private static final JobCard JOB_CARD_USER4 = new JobCard(
            JOB_POST_USER4,
            JOB_PACKAGE_USER4.getRateType(),
            JOB_PACKAGE_USER4.getPrice(),
            null,
            1,
            0
    );

    @Autowired
    private DataSource ds;

    @InjectMocks
    @Autowired
    private JobCardDaoJDBC jobCardDaoJDBCTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFindRelatedJobCards() {
        List<JobCard> maybeJobCards = jobCardDaoJDBCTest.findRelatedJobCards(1, HirenetUtils.ALL_PAGES);

        Assert.assertFalse(maybeJobCards.isEmpty());
        Assert.assertEquals(3, maybeJobCards.size());
        Assert.assertEquals(JOB_CARD_USER3, maybeJobCards.get(0));  //aparece primero pues tiene mas clientes en comun
        Assert.assertEquals(JOB_CARD_USER2, maybeJobCards.get(1));  //aparece segundo por que a pesar de tener los mismos clientes que 4, tiene mas contratos completados
        Assert.assertEquals(JOB_CARD_USER4, maybeJobCards.get(2));
    }

    @Test
    public void testSearch() {
        String title = "Electricista";
        JobPost.Zone zone = JobPost.Zone.values()[1];
        List<JobCard> jobCards = jobCardDaoJDBCTest.search(title, zone, new ArrayList<>(), HirenetUtils.ALL_PAGES);

        Assert.assertFalse(jobCards.isEmpty());
        Assert.assertEquals(4, jobCards.size());
    }

    @Test
    public void testSearchWithCategory() {
        String title = "";
        JobPost.Zone zone = JobPost.Zone.values()[1];
        JobPost.JobType jobType = JobPost.JobType.ELECTRICITY;
        List<JobCard> jobCards = jobCardDaoJDBCTest.searchWithCategory(title, zone, jobType, new ArrayList<>(), HirenetUtils.ALL_PAGES);

        Assert.assertFalse(jobCards.isEmpty());
        Assert.assertEquals(4, jobCards.size());
    }

    @Test
    public void findAllTest() {
        List<JobCard> jobCards = jobCardDaoJDBCTest.findAll(HirenetUtils.ALL_PAGES);

        Assert.assertFalse(jobCards.isEmpty());
        Assert.assertEquals(11, jobCards.size());
    }

    @Test
    public void findByUserIdTest() {
        List<JobCard> jobCards = jobCardDaoJDBCTest.findByUserId(USER1.getId(), HirenetUtils.ALL_PAGES);

        Assert.assertFalse(jobCards.isEmpty());
        Assert.assertEquals(9, jobCards.size());
    }

    @Test
    public void findByPostIdTest() {
        Assert.assertTrue(jobCardDaoJDBCTest.findByPostId(1).isPresent());
    }
}
