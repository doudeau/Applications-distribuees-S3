package org.rygn.kanban;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rygn.kanban.controller.TaskController;
import org.rygn.kanban.domain.Task;
import org.rygn.kanban.service.TaskService;
import org.rygn.kanban.utils.Constants;
import org.rygn.kanban.utils.TaskMoveAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class TaskControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TaskController taskController;
	
	@Autowired
	private TaskService taskService;
	
	@Test
	public void testGetAllDevelopers() throws Exception{
		   
		String uri = "/tasks";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
		
		String response = mvcResult.getResponse().getContentAsString();
		JSONArray array = new JSONArray(response); 
		JSONObject object = array.getJSONObject(0);  
	    Assert.assertEquals(object.getString("title"), "task1");
	}
	
	@Test
	public void testPostTask() throws Exception{
		
		Task task2 = new Task();
		task2.setTitle("task2");
		task2.setNbHoursForecast(0);
		task2.setNbHoursReal(0);
		task2.setDevelopers(null);
		task2.setStatus(taskService.findTaskStatus(Constants.TASK_STATUS_TODO_ID));
		
		String uri = "/tasks";
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] sendData = mapper.writeValueAsBytes(task2);
        
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(sendData))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(status, 200);
		
		Collection<Task> tasks = taskService.findAllTasks();
		Assert.assertEquals(tasks.size(), 3);
	}
	
	@Test
	public void testPatchTask() throws Exception{
		
		Task task3 = new Task();
		task3.setTitle("task3");
		task3.setNbHoursForecast(0);
		task3.setNbHoursReal(0);
		task3.setDevelopers(null);
		task3.setStatus(taskService.findTaskStatus(Constants.TASK_STATUS_TODO_ID));
		task3 = taskService.createTask(task3);
		
		String uri = "/tasks/" + task3.getId();
		
        TaskMoveAction moveRight = new TaskMoveAction();
		moveRight.setAction(Constants.MOVE_RIGHT_ACTION);
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] sendData = mapper.writeValueAsBytes(moveRight);
        
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(sendData))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(status, 200);
		
		Task task = taskService.findTask(task3.getId());
		Assert.assertEquals(task.getStatus().getId(), Constants.TASK_STATUS_DOING_ID);
	}
}
