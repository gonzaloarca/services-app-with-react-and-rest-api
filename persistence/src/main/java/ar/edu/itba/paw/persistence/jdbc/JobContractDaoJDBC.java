package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.dao.JobContractDao;
import ar.edu.itba.paw.interfaces.dao.JobPackageDao;
import ar.edu.itba.paw.interfaces.dao.JobPostDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.JobContract;
import ar.edu.itba.paw.models.JobPackage;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class JobContractDaoJDBC implements JobContractDao {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JobPackageDao jobPackageDao;

    @Autowired
    private JobPostDao jobPostDao;

    private final static RowMapper<JobContract> JOB_CONTRACT_ROW_MAPPER = (resultSet, in) -> new JobContract(
            resultSet.getLong("contract_id"),
            new User(
                    resultSet.getLong("client_id"),
                    resultSet.getString("client_email"),
                    resultSet.getString("client_name"),
                    "",
                    resultSet.getString("client_phone"),
                    resultSet.getBoolean("client_is_professional"),
                    resultSet.getBoolean("client_is_active")
            ),
            new JobPackage(
                    resultSet.getLong("package_id"),
                    resultSet.getLong("post_id"),
                    resultSet.getString("package_title"),
                    resultSet.getString("package_description"),
                    resultSet.getDouble("package_price"),
                    JobPackage.RateType.values()[resultSet.getInt("package_rate_type")],
                    resultSet.getBoolean("package_is_active")
            ), new User(
            resultSet.getLong("professional_id"),
            resultSet.getString("professional_email"),
            resultSet.getString("professional_username"),
            "",
            resultSet.getString("professional_phone"),
            resultSet.getBoolean("professional_is_professional"),
            resultSet.getBoolean("professional_is_active")
    ),
            resultSet.getDate("creation_date"),
            resultSet.getString("contract_description"),
            ""
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JobContractDaoJDBC(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("contract").usingGeneratedKeyColumns("contract_id");

    }

    @Override
    public JobContract create(long clientId, long packageId, String description) {
        Date creationDate = new Date();
        Number key = jdbcInsert.executeAndReturnKey(new HashMap<String, Object>() {{
            put("client_id", clientId);
            put("package_id", packageId);
            put("contract_description", description);
            put("creation_date", new java.sql.Date(creationDate.getTime()));
        }});

        //TODO: Cambiar excepciones por excepciones propias
        User client = userDao.findById(clientId).orElseThrow(NoSuchElementException::new);

        JobPackage jobPackage = jobPackageDao.findById(packageId).orElseThrow(NoSuchElementException::new);

        JobPost jobPost = jobPostDao.findById(jobPackage.getPostId()).orElseThrow(NoSuchElementException::new);

        //TODO: Revisar caso de nullPointerException
        User profesional = userDao.findById(jobPost.getUser().getId()).orElseThrow(NoSuchElementException::new);

        return new JobContract(key.longValue(), client, jobPackage, profesional, creationDate, description, "");
    }

    @Override
    public Optional<JobContract> findById(long id) {


        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM contract " +
                        "NATURAL JOIN job_package " +
                        "NATURAL JOIN (SELECT post_id, user_id AS professional_id FROM job_post) AS posts " +
                        "NATURAL JOIN (SELECT user_id              AS client_id, " +
                        "user_name            AS client_name, " +
                        "user_email           AS client_email, " +
                        "user_phone           AS client_phone, " +
                        "user_is_professional AS client_is_professional, " +
                        "user_is_active       AS client_is_active " +
                        "FROM users) AS clients " +
                        "NATURAL JOIN (SELECT user_id              AS professional_id, " +
                        "user_name            AS professional_username, " +
                        "user_email           AS professional_email, " +
                        "user_phone           AS professional_phone, " +
                        "user_is_professional AS professional_is_professional, " +
                        "user_is_active       AS professional_is_active " +
                        "FROM users) AS professionals " +
                        "WHERE contract_id = ?"
                , new Object[]{id}, JOB_CONTRACT_ROW_MAPPER).stream().findFirst();


    }

    @Override
    public Optional<List<JobContract>> findByClientId(long id) {
        return Optional.of(
                jdbcTemplate.query(
                        "SELECT * " +
                                "FROM contract " +
                                "NATURAL JOIN job_package " +
                                "NATURAL JOIN (SELECT post_id, user_id AS professional_id FROM job_post) AS posts " +
                                "NATURAL JOIN (SELECT user_id              AS client_id, " +
                                "user_name            AS client_name, " +
                                "user_email           AS client_email, " +
                                "user_phone           AS client_phone, " +
                                "user_is_professional AS client_is_professional, " +
                                "user_is_active       AS client_is_active " +
                                "FROM users) AS clients " +
                                "NATURAL JOIN (SELECT user_id              AS professional_id, " +
                                "user_name            AS professional_username, " +
                                "user_email           AS professional_email, " +
                                "user_phone           AS professional_phone, " +
                                "user_is_professional AS professional_is_professional, " +
                                "user_is_active       AS professional_is_active " +
                                "FROM users) AS professionals " +
                                "WHERE client_id = ?"
                        , new Object[]{id},
                        JOB_CONTRACT_ROW_MAPPER));
    }

    @Override
    public Optional<List<JobContract>> findByProId(long id) {
        //TODO
        return Optional.of(jdbcTemplate.query(
                "SELECT * " +
                        "FROM contract " +
                        "NATURAL JOIN job_package " +
                        "NATURAL JOIN (SELECT post_id, user_id AS professional_id FROM job_post) AS posts " +
                        "NATURAL JOIN (SELECT user_id              AS client_id, " +
                        "user_name            AS client_name, " +
                        "user_email           AS client_email, " +
                        "user_phone           AS client_phone, " +
                        "user_is_professional AS client_is_professional, " +
                        "user_is_active       AS client_is_active " +
                        "FROM users) AS clients " +
                        "NATURAL JOIN (SELECT user_id              AS professional_id, " +
                        "user_name            AS professional_username, " +
                        "user_email           AS professional_email, " +
                        "user_phone           AS professional_phone, " +
                        "user_is_professional AS professional_is_professional, " +
                        "user_is_active       AS professional_is_active " +
                        "FROM users) AS professionals " +
                        "WHERE professional_id = ?",
                new Object[]{id}, JOB_CONTRACT_ROW_MAPPER));
    }

    @Override
    public Optional<List<JobContract>> findByPostId(long id) {

        return Optional.of(jdbcTemplate.query(
                "SELECT * " +
                        "FROM contract " +
                        "NATURAL JOIN job_package " +
                        "NATURAL JOIN (SELECT post_id, user_id AS professional_id FROM job_post) AS posts " +
                        "NATURAL JOIN (SELECT user_id              AS client_id, " +
                        "user_name            AS client_name, " +
                        "user_email           AS client_email, " +
                        "user_phone           AS client_phone, " +
                        "user_is_professional AS client_is_professional, " +
                        "user_is_active       AS client_is_active " +
                        "FROM users) AS clients " +
                        "NATURAL JOIN (SELECT user_id              AS professional_id, " +
                        "user_name            AS professional_username, " +
                        "user_email           AS professional_email, " +
                        "user_phone           AS professional_phone, " +
                        "user_is_professional AS professional_is_professional, " +
                        "user_is_active       AS professional_is_active " +
                        "FROM users) AS professionals " +
                        "WHERE post_id = ?"
                , new Object[]{id},
                JOB_CONTRACT_ROW_MAPPER));
    }

    @Override
    public Optional<List<JobContract>> findByPackageId(long id) {

        return Optional.of(jdbcTemplate.query(
                "SELECT * " +
                        "FROM contract " +
                        "NATURAL JOIN job_package " +
                        "NATURAL JOIN (SELECT post_id, user_id AS professional_id FROM job_post) AS posts " +
                        "NATURAL JOIN (SELECT user_id              AS client_id, " +
                        "user_name            AS client_name, " +
                        "user_email           AS client_email, " +
                        "user_phone           AS client_phone, " +
                        "user_is_professional AS client_is_professional, " +
                        "user_is_active       AS client_is_active " +
                        "FROM users) AS clients " +
                        "NATURAL JOIN (SELECT user_id              AS professional_id, " +
                        "user_name            AS professional_username, " +
                        "user_email           AS professional_email, " +
                        "user_phone           AS professional_phone, " +
                        "user_is_professional AS professional_is_professional, " +
                        "user_is_active       AS professional_is_active " +
                        "FROM users) AS professionals " +
                        "WHERE package_id = ?"
                , new Object[]{id},
                JOB_CONTRACT_ROW_MAPPER));
    }
}
