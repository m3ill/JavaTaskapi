package com.example.taskapi;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.util.List;
import java.util.ArrayList;

@Service
public class TaskService {

    //sql bağlantılı liste
    private final TaskRepository taskRepository;
    private final ResourceUrlProvider resourceUrlProvider;

    public TaskService(TaskRepository taskRepository, ResourceUrlProvider resourceUrlProvider){
        this.taskRepository = taskRepository;
        this.resourceUrlProvider = resourceUrlProvider;
    }


    public List<Task> getTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskById(int id){

        return taskRepository.findById(id);
    }

    public Task CreateTask(String title){
        return taskRepository.createTask(title);
    }

    public Task UpdateTask(int id, Boolean completed){
        return taskRepository.updateTask(id, completed);
    }

    public Boolean DeleteTask(int id){
        return taskRepository.deleteTask(id);
    }
}
