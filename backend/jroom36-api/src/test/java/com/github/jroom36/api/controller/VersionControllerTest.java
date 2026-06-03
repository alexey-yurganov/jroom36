package com.github.jroom36.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VersionController.class)
class VersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BuildProperties buildProperties;

    @Test
    @DisplayName("GET /api/v1/version returns version and name")
    void getVersion_whenCalled_thenReturnsVersionView() throws Exception {
        when(buildProperties.getVersion()).thenReturn("1.0.0");
        when(buildProperties.getName()).thenReturn("jroom36");

        mockMvc.perform(get("/api/v1/version"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.name").value("jroom36"));

        verify(buildProperties).getVersion();
        verify(buildProperties).getName();
    }
}
