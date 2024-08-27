package com.ramiletus.frauddetection.controller;

import com.ramiletus.frauddetection.service.users.UsersCommandHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UsersController.class)
@AutoConfigureMockMvc
class UsersControllerTest {

    @MockBean
    private UsersCommandHandler usersCommandHandler;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToUsersAndValidUser_thenCorrectResponse() throws Exception {
        String injectUserCommand = "{\n    \"name\": \"Juan Ramil\",\n    \"email\": \"juanram@udc.es\",\n    \"phoneNumbers\": [\n        {\n            \"number\": 626262626,\n            \"isMainNumber\": false,\n            \"operator\": \"Telefonica\"\n        }, \n        {\n            \"number\": 655555555,\n            \"isMainNumber\": true,\n            \"operator\": \"Vodafone\"\n        }\n    ]\n}";
        mockMvc.perform(MockMvcRequestBuilders.post("/users/inject")
                        .content(injectUserCommand)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenPostRequestToUsersAndInValidUser_thenCorrectResponse() throws Exception {
        String wrongInjectUserCommand = "{\n    \"name\": \"Juan Ramil\",\n    \"email\": \"juanramudc.es\",\n    \"phoneNumbers\": [\n        {\n            \"number\": 626262626,\n            \"isMainNumber\": null,\n            \"operator\": \"Telefonica\"\n        }, \n        {\n            \"number\": 655555555,\n            \"isMainNumber\": true,\n            \"operator\": \"Vodafone\"\n        }\n    ]\n}";
        mockMvc.perform(MockMvcRequestBuilders.post("/users/inject")
                        .content(wrongInjectUserCommand)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}