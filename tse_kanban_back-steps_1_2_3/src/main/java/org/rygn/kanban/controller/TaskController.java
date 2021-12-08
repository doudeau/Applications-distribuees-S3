package org.rygn.kanban.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.rygn.kanban.domain.Task;
import org.rygn.kanban.service.TaskService;
import org.rygn.kanban.utils.Constants;
import org.rygn.kanban.utils.TaskMoveAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/tasks")
	Collection<Task> findAllTask() {
		return taskService.findAllTasks();
	}
	
	@PostMapping("/tasks")
	public Task createTask(@Valid @RequestBody Task task) {
		return taskService.createTask(task);
	}
	
	@PatchMapping("/tasks/{id}")
	public Task moveTask(@RequestBody TaskMoveAction taskMoveAction, @PathVariable Long id) {
		
		Task task = taskService.findTask(id);
				
		if (Constants.MOVE_LEFT_ACTION.equals(taskMoveAction.getAction())) { 
			task = taskService.moveLeftTask(task);
		}
		else if (Constants.MOVE_RIGHT_ACTION.equals(taskMoveAction.getAction())) {
			task = taskService.moveRightTask(task);
		}
		return task;
	}
}
