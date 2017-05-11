package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import ru.javawebinar.topjava.model.NamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {
    private static final String SELECT = "SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_id ";
    private static final String INSERT = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";

    private static final ResultSetExtractor<List<User>> RSE = resultSet -> {
        Map<Integer, User> userMap = new HashMap<>();

        while (resultSet.next()) {
            Set<Role> roles = new HashSet<>();

            Integer userId = resultSet.getInt("user_id");
            Role role = Role.valueOf(resultSet.getString("role"));

            User user = userMap.get(userId);
            if (user != null) {
                user.getRoles().add(role);
                continue;
            }

            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            boolean enabled = resultSet.getBoolean("enabled");
            Integer calories = resultSet.getInt("calories_per_day");

            roles.add(role);
            User newUser = new User(id, name, email, password, calories, enabled, roles);
            userMap.put(id, newUser);
        }

        return userMap.values()
                .stream()
                .sorted(Comparator.comparing(NamedEntity::getName))
                .collect(Collectors.toList());
    };

    private TransactionTemplate txTemplate;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate,
                                  NamedParameterJdbcTemplate namedParameterJdbcTemplate, TransactionTemplate txTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.txTemplate = txTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        BatchPreparedStatementSetter batch = new BatchPreparedStatementSetter() {
            List<Role> roleList = new ArrayList<>(user.getRoles());
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Role role = roleList.get(i);
                ps.setInt(1, user.getId());
                ps.setString(2, role.toString());
            }

            @Override
            public int getBatchSize() {
                return user.getRoles().size();
            }
        };

        return txTemplate.execute(status -> {
            if (user.isNew()) {
                Number newKey = insertUser.executeAndReturnKey(parameterSource);
                user.setId(newKey.intValue());
                jdbcTemplate.batchUpdate(INSERT, batch);
            } else {
                namedParameterJdbcTemplate.update(
                        "UPDATE users SET name=:name, email=:email, password=:password, " +
                                "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);

                deleteRoles(user.getId());
                jdbcTemplate.batchUpdate(INSERT, batch);
            }

            return user;
        });
    }

    @Override
    public boolean delete(int id) {
        return txTemplate.execute(status ->
                jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0);
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query( SELECT + "WHERE id=?", RSE, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query(SELECT + "WHERE email=?", RSE, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email", RSE);
    }

    private void deleteRoles(int id) {
        txTemplate.execute(status ->
                jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id));
    }
}
