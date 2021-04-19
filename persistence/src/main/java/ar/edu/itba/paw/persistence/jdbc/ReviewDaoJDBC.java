package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.dao.JobContractDao;
import ar.edu.itba.paw.interfaces.dao.JobPostDao;
import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.*;
import exceptions.JobContractNotFoundException;
import exceptions.JobPostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class ReviewDaoJDBC implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    private JobContractDao jobContractDao;

    @Autowired
    private JobPostDao jobPostDao;

    private static List<JobPost.Zone> auxiGetZones(Object[] objs) {
        List<JobPost.Zone> zones = new ArrayList<>();
        Arrays.stream(objs)
                .forEach(obj -> zones.add(JobPost.Zone.values()[(int) obj]));
        return zones;
    }

    private final RowMapper<Review> REVIEW_ROW_MAPPER = (resultSet, i) ->
            new Review(resultSet.getInt("review_rate"), resultSet.getString("review_title"),
                    resultSet.getString("review_description"),
                    new User(
                            resultSet.getLong("client_id"),
                            resultSet.getString("client_email"),
                            resultSet.getString("client_name"),
                            null,
                            resultSet.getString("client_phone"),
                            resultSet.getBoolean("client_is_active")
                    ),
                    new JobPost(
                            resultSet.getLong("post_id"),
                            new User(
                                    resultSet.getLong("professional_id"),
                                    resultSet.getString("professional_email"),
                                    resultSet.getString("professional_name"),
                                    null,
                                    resultSet.getString("professional_phone"),
                                    resultSet.getBoolean("professional_is_active")
                            ),
                            resultSet.getString("post_title"),
                            resultSet.getString("post_available_hours"),
                            JobPost.JobType.values()[resultSet.getInt("post_job_type")],
                            ReviewDaoJDBC.auxiGetZones((Object[]) resultSet.getArray("zones").getArray()),
                            -1,
                            resultSet.getBoolean("post_is_active"))
            );

    @Autowired
    public ReviewDaoJDBC(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("review");
    }

    @Override
    public Review create(long contractId, int rate, String title, String description) {
        Number key = jdbcInsert.executeAndReturnKey(new HashMap<String,Object>(){{
            put("contract_id", contractId);
            put("review_rate", rate);
            put("review_title",title);
            put("review_description",description);
        }});

        //TODO: Cambiar excepciones por excepciones propias
        JobContract jobContract = jobContractDao.findById(contractId).orElseThrow(JobContractNotFoundException::new);
        JobPost jobPost = jobPostDao.findById(jobContract.getJobPackage().getPostId()).orElseThrow(JobPostNotFoundException::new);

        return new Review(rate, title, description, jobContract.getClient(), jobPost);
    }

    @Override
    public List<Review> findAllReviews(long id) {
        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM full_contract NATURAL JOIN review " +
                        "WHERE post_id = ?", new Object[]{id}, REVIEW_ROW_MAPPER);
    }

    @Override
    public int findJobPostReviewsSize(long id) {
        return jdbcTemplate.queryForObject("SELECT reviews FROM full_post WHERE post_id = ?", new Object[]{id},
                Integer.class);
    }

    @Override
    public List<Review> findReviews(long id) {
        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM full_contract NATURAL JOIN review " +
                        "WHERE package_id = ?", new Object[]{id}, REVIEW_ROW_MAPPER);
    }

    @Override
    public Optional<Review> findReviewByContractId(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM review WHERE contract_id = ?", new Object[]{id}, REVIEW_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Review> findProfessionalReviews(long id) {
        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM full_contract NATURAL JOIN review " +
                        "WHERE professional_id = ?", new Object[]{id}, REVIEW_ROW_MAPPER);
    }

    @Override
    public Double findProfessionalAvgRate(long id) {
        return jdbcTemplate.query("SELECT coalesce(avg(review_rate),0) as rating " +
                        "FROM review NATURAL JOIN contract NATURAL JOIN job_package NATURAL JOIN job_post " +
                        "NATURAL JOIN users WHERE user_id = ? GROUP BY user_id"
                , new Object[]{id}, (resultSet, i) -> resultSet.getDouble("rating")).stream().findFirst().orElse(0.0);
    }
}
