package com.data.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import com.data.integration.DataIntegrationApplication;
import com.data.integration.service.enums.GrantTypeEnum;


@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DataIntegrationApplication.class)
@WebAppConfiguration
@ActiveProfiles("localtest")
public class MultiPartFileUploadTest {

	private MockMvc mvc;
	
	@Autowired
	WebApplicationContext context;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private Environment environment;
	
	@Test
	public void testFileUploadInChunk(){
		
	}
	
	@Before
	public void setUp() {
	
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilter(springSecurityFilterChain).build();
        
	}
	
	@Test
	public void testMultipartUpload(){
		
		try {
		getOauthToken();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getOauthToken() throws UnsupportedEncodingException, Exception{
		String authorization = "Basic "
				+ new String(
						Base64Utils
								.encode("172d4260-4802-11e6-beb8-9e71128cae77:e4feeb68-4801-11e6-beb8-9e71128cae77"
										.getBytes()));
		String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

		mvc.perform(
				post("/oauth/token")
						.header("Authorization", authorization)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("grant_type",
								GrantTypeEnum.CLIENT_CREDENTIALS.getKey())
						.param("scope", "read write")
						.param("client_id",
								"172d4260-4802-11e6-beb8-9e71128cae77")
						.param("client_secret",
								"e4feeb68-4801-11e6-beb8-9e71128cae77"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.access_token", is(notNullValue())))
				.andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
				.andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
				.andExpect(jsonPath("$.scope", is(equalTo("read write"))))
				.andReturn().getResponse().getContentAsString();
	
		return  null;
	}

}
