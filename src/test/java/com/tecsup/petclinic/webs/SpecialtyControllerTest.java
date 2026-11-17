package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SpecialtyControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllSpecialtiesReturnsSeededSpecialties() throws Exception {
        mockMvc.perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].name", hasItems("radiology", "surgery", "dentistry")));
    }

    @Test
    void findSpecialtyByIdReturnsSpecialty() throws Exception {
        mockMvc.perform(get("/specialties/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("surgery")));
    }

    @Test
    void findMissingSpecialtyReturnsNotFound() throws Exception {
        mockMvc.perform(get("/specialties/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createSpecialtyReturnsCreatedSpecialty() throws Exception {
        SpecialtyDTO specialty = SpecialtyDTO.builder().name("dermatology").build();

        mockMvc.perform(post("/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(specialty)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is("dermatology")));
    }

    @Test
    void updateSpecialtyChangesStoredSpecialty() throws Exception {
        SpecialtyDTO specialty = SpecialtyDTO.builder().name("orthopedic surgery").build();

        mockMvc.perform(put("/specialties/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(specialty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("orthopedic surgery")));
    }

    @Test
    void deleteSpecialtyRemovesSpecialty() throws Exception {
        SpecialtyDTO specialty = SpecialtyDTO.builder().name("test specialty").build();
        String response = mockMvc.perform(post("/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(specialty)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Integer id = OBJECT_MAPPER.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/specialties/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/specialties/{id}", id))
                .andExpect(status().isNotFound());
    }
}
