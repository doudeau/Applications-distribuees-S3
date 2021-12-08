package org.rygn.kanban;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rygn.kanban.controller.DeveloperController;
import org.rygn.kanban.domain.Developer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class DeveloperControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private DeveloperController developerController;
	
	@Test
	public void testGetAllDevelopers() throws Exception{
		   
		String uri = "/developers";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
		
		String response = mvcResult.getResponse().getContentAsString();
		JSONArray array = new JSONArray(response); 
		JSONObject object = array.getJSONObject(0);  
	    Assert.assertEquals(object.getString("lastname"), "dev1");
	    Assert.assertEquals(object.getString("firstname"), "dev1");
	    Assert.assertEquals(object.getString("email"), "dev1@dev.dev");
	}
}
