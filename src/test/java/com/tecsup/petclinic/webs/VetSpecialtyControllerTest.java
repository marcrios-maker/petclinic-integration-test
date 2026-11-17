package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
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
class VetSpecialtyControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllVetSpecialtiesReturnsSeededAssociations() throws Exception {
        mockMvc.perform(get("/vet-specialties"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].vetId", hasItems(2, 3, 4, 5)))
                .andExpect(jsonPath("$[*].specialtyId", hasItems(1, 2, 3)));
    }

    @Test
    void findVetSpecialtyByIdsReturnsAssociation() throws Exception {
        mockMvc.perform(get("/vet-specialties/3/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vetId", is(3)))
                .andExpect(jsonPath("$.specialtyId", is(3)));
    }

    @Test
    void findMissingAssociationReturnsNotFound() throws Exception {
        mockMvc.perform(get("/vet-specialties/1/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAssociationReturnsCreatedAssociation() throws Exception {
        VetSpecialtyDTO association = VetSpecialtyDTO.builder().vetId(1).specialtyId(1).build();

        mockMvc.perform(post("/vet-specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(association)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vetId", is(1)))
                .andExpect(jsonPath("$.specialtyId", is(1)));
    }

    @Test
    void createAssociationWithMissingVetReturnsNotFound() throws Exception {
        VetSpecialtyDTO association = VetSpecialtyDTO.builder().vetId(9999).specialtyId(1).build();

        mockMvc.perform(post("/vet-specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(association)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAssociationRemovesAssociation() throws Exception {
        VetSpecialtyDTO association = VetSpecialtyDTO.builder().vetId(1).specialtyId(1).build();
        mockMvc.perform(post("/vet-specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(association)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/vet-specialties/1/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/vet-specialties/1/1"))
                .andExpect(status().isNotFound());
    }
}
