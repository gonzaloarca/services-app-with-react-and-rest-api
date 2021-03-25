package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    private final static RowMapper<User> USER_ROW_MAPPER = (resultSet, i) -> {
        String username = resultSet.getString("username");
        long userId = resultSet.getLong("userId");
        String password = resultSet.getString("password");

        User user = new User();
        user.setId(userId);
        user.setName(username);
        user.setPassword(password);

        return user;
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("userid");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (" +
                "userid SERIAL PRIMARY KEY, " +
                "username VARCHAR(100) NOT NULL," +
                "password VARCHAR(100) NOT NULL" +
                ")");
    }

    @Override
    public Optional<User> get(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE userid = ?", new Object[]{id},
                USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public User register(String username, String password) {
        Number key = jdbcInsert.executeAndReturnKey(new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
        }});

        User user = new User();
        user.setId(key.longValue());
        user.setName(username);
        user.setPassword(password);

        return user;
    }
}
