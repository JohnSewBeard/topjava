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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {
  //  private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final String SQL = "SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_id ";

    private static final Map<Integer, User> userMap = new ConcurrentHashMap<>();

    private static final RowMapper<User> MAPPER = (resultSet, i) -> {
        Set<Role> roles = new HashSet<>();

        Integer userId = resultSet.getInt("user_id");
        Role role = Role.valueOf(resultSet.getString("role"));

        User user = userMap.get(userId);
        if (user != null) {
            user.getRoles().add(role);
            return user;
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

        return newUser;
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

        return txTemplate.execute(status -> {
            if (user.isNew()) {
                Number newKey = insertUser.executeAndReturnKey(parameterSource);
                user.setId(newKey.intValue());
                jdbcTemplate.batchUpdate("INSERT INTO user_roles (role, user_id) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                    List<Role> roleList = new ArrayList<>(user.getRoles());
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Role role = roleList.get(i);
                        ps.setString(1, role.toString());
                        ps.setInt(2, user.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getRoles().size();
                    }
                });
            } else {
                namedParameterJdbcTemplate.update(
                        "UPDATE users SET name=:name, email=:email, password=:password, " +
                                "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
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
        userMap.clear();
        jdbcTemplate.query( SQL + "WHERE id=?", MAPPER, id);
        return DataAccessUtils.singleResult(userMap.values());
    }

    @Override
    public User getByEmail(String email) {
        userMap.clear();
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        jdbcTemplate.query(SQL + "WHERE email=?", MAPPER, email);
        return DataAccessUtils.singleResult(userMap.values());
    }

    @Override
    public List<User> getAll() {
        userMap.clear();
        jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email", MAPPER);
        return userMap.values().stream().sorted(Comparator.comparing(NamedEntity::getName)).collect(Collectors.toList());
    }

}
