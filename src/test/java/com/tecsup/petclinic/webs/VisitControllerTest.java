package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VisitDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VisitControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllVisitsReturnsSeededVisits() throws Exception {
        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[*].description", hasItems("rabies shot", "general checkup", "dental cleaning")));
    }

    @Test
    void findVisitByIdReturnsVisit() throws Exception {
        mockMvc.perform(get("/visits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.petId", is(7)))
                .andExpect(jsonPath("$.visitDate", is("2010-03-04")))
                .andExpect(jsonPath("$.description", is("rabies shot")));
    }

    @Test
    void findMissingVisitReturnsNotFound() throws Exception {
        mockMvc.perform(get("/visits/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createVisitReturnsCreatedVisit() throws Exception {
        VisitDTO visit = VisitDTO.builder()
                .petId(1)
                .visitDate(LocalDate.of(2025, 3, 10))
                .description("annual checkup")
                .build();

        mockMvc.perform(post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(visit)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.petId", is(1)))
                .andExpect(jsonPath("$.visitDate", is("2025-03-10")))
                .andExpect(jsonPath("$.description", is("annual checkup")));
    }

    @Test
    void updateVisitChangesStoredVisit() throws Exception {
        VisitDTO visit = VisitDTO.builder()
                .petId(1)
                .visitDate(LocalDate.of(2025, 4, 12))
                .description("follow-up examination")
                .build();

        mockMvc.perform(put("/visits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(visit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.petId", is(1)))
                .andExpect(jsonPath("$.visitDate", is("2025-04-12")))
                .andExpect(jsonPath("$.description", is("follow-up examination")));
    }

    @Test
    void deleteVisitRemovesVisit() throws Exception {
        VisitDTO visit = VisitDTO.builder()
                .petId(1)
                .visitDate(LocalDate.of(2025, 5, 15))
                .description("visit to delete")
                .build();
        String response = mockMvc.perform(post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(visit)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long id = OBJECT_MAPPER.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/visits/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/visits/{id}", id))
                .andExpect(status().isNotFound());
    }
}
