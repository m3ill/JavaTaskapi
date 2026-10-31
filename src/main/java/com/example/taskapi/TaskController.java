package com.example.taskapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.List;


@RestController
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public List<Task> getTasks(){
        return taskService.getTasks();
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable int id){
        Task task = taskService.getTaskById(id);
        if(task == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found");
        }
        return task;
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody CreateTaskRequest request){
        if(request.title() == null || request.title().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Title is empty");
        }
        return taskService.CreateTask(request.title().trim());
    }

    @PatchMapping("/tasks/{id}")
    public Task updateTask(@PathVariable("id") int id, @RequestBody UpdateTaskRequest request){
        if (request.completed() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Complete is null");
        }

        Task task = taskService.UpdateTask(id,request.completed());
        if(task == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found");
        }
        return task;
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("id") int id){
        Boolean deleted = taskService.DeleteTask(id);

        if (!deleted){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found");
        }
    }
}
