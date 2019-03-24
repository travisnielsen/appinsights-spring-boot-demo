package io.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import io.example.User;

@Repository
public class UserRepository {
    private static final String SQL_FIND_BY_ID = "SELECT * FROM \"USER\" WHERE ID = :id";
    private static final String SQL_FIND_ALL = "SELECT * FROM \"USER\"";
    private static final String SQL_INSERT = "INSERT INTO \"USER\" (firstName, lastName, createdAt, updatedAt) values(:firstName, :lastName, :createdAt, :updatedAt)";
    // private static final String SQL_INSERT = "INSERT INTO \"USER\" (firstName, lastName, createdAt, updatedAt) OUTPUT Inserted.Id values(:firstName, :lastName, :createdAt, :updatedAt)";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM \"USER\" WHERE ID = :id";

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = new BeanPropertyRowMapper<>(User.class);

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public Iterable<User> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, ROW_MAPPER);
    }

    public User findById(Integer id) {
        try {
            final SqlParameterSource paramSource = new MapSqlParameterSource("id", id);
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, paramSource, ROW_MAPPER);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public int create(User user) {
        final SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("createdAt", user.getCreatedAt())
                .addValue("updatedAt", user.getLastModified());

        final KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, paramSource, holder, new String[] {"id"});
        Number generatedId = holder.getKey();
        return generatedId.intValue();
    }

    public void deleteById(Integer id) {
        final SqlParameterSource paramSource = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(SQL_DELETE_BY_ID, paramSource);
    }

}