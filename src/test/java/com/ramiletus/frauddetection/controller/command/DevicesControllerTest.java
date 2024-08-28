package com.ramiletus.frauddetection.controller.command;

import com.ramiletus.frauddetection.service.devices.DevicesCommandHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Reinicia el contexto después de cada prueba
class DevicesControllerTest {

    @MockBean
    private DevicesCommandHandler devicesCommandHandler;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToDevicesAndValidDevice_thenCorrectResponse() throws Exception {
        String injectDeviceCommand = "{\n    \"operativeSystem\": \"Windows 10\",\n    \"browser\": \"Chrome 21\",\n    \"userId\": \"0eb8b937-75d9-4786-8f35-28207821b83f\"\n}";
        mockMvc.perform(MockMvcRequestBuilders.post("/devices/inject")
                        .content(injectDeviceCommand)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenPostRequestToDevicesAndInValidDevice_thenCorrectResponse() throws Exception {
        String wrongInjectDeviceCommand = "{\n    \"operativeSystem\": null,\n    \"browser\": \"Chrome 21\",\n    \"userId\": \"0eb8b937-75d9-4786-8f35-28207821b83f\"\n}";
        mockMvc.perform(MockMvcRequestBuilders.post("/devices/inject")
                        .content(wrongInjectDeviceCommand)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
