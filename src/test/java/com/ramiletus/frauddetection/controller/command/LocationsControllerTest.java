package com.ramiletus.frauddetection.controller.command;

import com.ramiletus.frauddetection.service.location.LocationsCommandHandler;
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
class LocationsControllerTest {

    @MockBean
    private LocationsCommandHandler locationsCommandHandler;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToDevicesAndValidDevice_thenCorrectResponse() throws Exception {
        String injectDeviceCommand = "{\n" +
                "    \"ip\": \"24.48.0.1\",\n" +
                "    \"deviceId\": \"7198ec26-0739-477e-96a2-c760ccce9289\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/locations/inject")
                        .content(injectDeviceCommand)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenPostRequestToDevicesAndInValidDevice_thenCorrectResponse() throws Exception {
        String wrongInjectDeviceCommand = "{\n" +
                "    \"ip\": null,\n" +
                "    \"deviceId\": \"7198ec26-0739-477e-96a2-c760ccce9289\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/locations/inject")
                        .content(wrongInjectDeviceCommand)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
