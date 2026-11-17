package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.TypeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TypesControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllTypesReturnsSeededTypes() throws Exception {
        mockMvc.perform(get("/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[*].name", hasItems("cat", "dog", "rabbit", "turtle")));
    }

    @Test
    void findTypeByIdReturnsType() throws Exception {
        mockMvc.perform(get("/types/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("dog")));
    }

    @Test
    void findMissingTypeReturnsNotFound() throws Exception {
        mockMvc.perform(get("/types/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTypeReturnsCreatedType() throws Exception {
        TypeDTO type = TypeDTO.builder().name("ferret").build();

        mockMvc.perform(post("/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(type)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is("ferret")));
    }

    @Test
    void updateTypeChangesStoredType() throws Exception {
        TypeDTO type = TypeDTO.builder().name("canine").build();

        mockMvc.perform(put("/types/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(type)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("canine")));
    }

    @Test
    void deleteTypeRemovesType() throws Exception {
        TypeDTO type = TypeDTO.builder().name("type to delete").build();
        String response = mockMvc.perform(post("/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(type)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Integer id = OBJECT_MAPPER.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/types/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/types/{id}", id))
                .andExpect(status().isNotFound());
    }
}
