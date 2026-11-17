package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VetControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllVetsReturnsSeededVets() throws Exception {
        mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("James")))
                .andExpect(jsonPath("$[0].lastName", is("Carter")));
    }

    @Test
    void findVetByIdReturnsVet() throws Exception {
        mockMvc.perform(get("/vets/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("Helen")))
                .andExpect(jsonPath("$.lastName", is("Leary")));
    }

    @Test
    void findMissingVetReturnsNotFound() throws Exception {
        mockMvc.perform(get("/vets/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createVetReturnsCreatedVet() throws Exception {
        VetDTO vet = VetDTO.builder().firstName("Ada").lastName("Lovelace").build();

        mockMvc.perform(post("/vets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(vet)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName", is("Ada")))
                .andExpect(jsonPath("$.lastName", is("Lovelace")));
    }

    @Test
    void updateVetChangesStoredVet() throws Exception {
        VetDTO vet = VetDTO.builder().firstName("Helen").lastName("Updated").build();

        mockMvc.perform(put("/vets/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(vet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("Helen")))
                .andExpect(jsonPath("$.lastName", is("Updated")));
    }

    @Test
    void deleteVetRemovesVet() throws Exception {
        VetDTO vet = VetDTO.builder().firstName("Test").lastName("Delete").build();
        String response = mockMvc.perform(post("/vets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(vet)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Integer id = OBJECT_MAPPER.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/vets/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/vets/{id}", id))
                .andExpect(status().isNotFound());
    }
}
