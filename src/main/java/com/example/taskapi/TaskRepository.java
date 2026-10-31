package com.example.taskapi;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks ORDER BY id ASC";

        return jdbcTemplate.query(sql,(rs, rowNum) ->
                new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getBoolean("completed")
                        ));
    }

    public Task createTask(String title) {
        String sql = "INSERT INTO tasks (title) VALUES (?) RETURNING id,title,completed";
        return jdbcTemplate.queryForObject(sql,(rs, rowNum) ->new Task(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getBoolean("completed")
        ),title);
    }

    public Task findById(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";

        List<Task> results = jdbcTemplate.query(sql,(rs, rowNum) -> new Task(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getBoolean("completed")
        ),id);

        return results.isEmpty() ? null : results.get(0);
    }


    public Task updateTask(int id, Boolean completed){
        String sql = "UPDATE tasks SET completed = ? WHERE id = ? RETURNING id,title,completed";

        List<Task> results = jdbcTemplate.query(sql,(rs, rowNum) -> new Task(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getBoolean("completed")
        ),completed,id);

        return results.isEmpty() ? null : results.get(0);
    }

    public Boolean deleteTask(int id){
        String sql = "DELETE FROM tasks WHERE id = ?";

        int affected = jdbcTemplate.update(sql,id);

        return affected > 0;
    }


}
