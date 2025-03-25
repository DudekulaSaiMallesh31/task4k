package com.example.taskk.controller;

import com.example.taskk.model.Task;
import com.example.taskk.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") // Allows API access from anywhere
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/search")
    public List<Task> getTasksByName(@RequestParam(name = "name", required = false) String name) {
        return (name != null && !name.isEmpty()) ? taskService.getTasksByName(name) : taskService.getAllTasks();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}/execute")
    public Task executeTask(@PathVariable String id) {
        return taskService.executeTask(id);
    }
}
