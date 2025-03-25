package com.example.taskk.service;

import com.example.taskk.model.Task;
import com.example.taskk.model.TaskExecution;
import com.example.taskk.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    public Task executeTask(String id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            TaskExecution execution = new TaskExecution();
            execution.setStartTime(new Date());

            try {
                if (task.getCommand() == null || task.getCommand().trim().isEmpty()) {
                    throw new IllegalArgumentException("Command is missing for task ID: " + id);
                }

                String os = System.getProperty("os.name").toLowerCase();
                ProcessBuilder processBuilder;

                if (os.contains("win")) {
                    processBuilder = new ProcessBuilder("cmd.exe", "/c", task.getCommand());
                } else {
                    processBuilder = new ProcessBuilder("/bin/sh", "-c", task.getCommand());
                }

                System.out.println("Executing command: " + String.join(" ", processBuilder.command()));

                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                process.waitFor();

                execution.setOutput(output.toString().trim());

            } catch (Exception e) {
                execution.setOutput("Error executing command: " + e.getMessage());
                e.printStackTrace();
            }

            execution.setEndTime(new Date());

            // Clear previous executions and store only the latest one
            task.getTaskExecutions().clear();
            task.getTaskExecutions().add(execution);

            return taskRepository.save(task);
        }
        return null;
    }

    public List<Task> getTasksByName(String name) {
        return taskRepository.findByNameContainingIgnoreCase(name);
    }
}
