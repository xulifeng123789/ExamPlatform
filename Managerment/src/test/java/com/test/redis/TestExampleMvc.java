package com.test.redis;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{"classpath:spring/applicationContext-dao.xml",
"classpath:spring/springmvc.xml"})
public class TestExampleMvc {

	private MockMvc mockMvc;
	
    @Resource
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void getTest() throws Exception {
    	

    	MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/article/table"))   
                .andDo(MockMvcResultHandlers.print())  
                .andReturn();   
    }
    
    @Test
    public void addTest() throws Exception {
    	
    	MvcResult result = mockMvc.perform(post("/article/table")
    			.content("")) 
                .andDo(MockMvcResultHandlers.print())  
                .andReturn();   
    }
}
