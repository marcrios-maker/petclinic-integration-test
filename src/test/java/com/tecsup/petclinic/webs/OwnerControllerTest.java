package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.OwnerDTO;
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
class OwnerControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllOwnersReturnsSeededOwners() throws Exception {
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("George")))
                .andExpect(jsonPath("$[0].lastName", is("Franklin")));
    }

    @Test
    void findOwnerByIdReturnsOwner() throws Exception {
        mockMvc.perform(get("/owners/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("Betty")))
                .andExpect(jsonPath("$.lastName", is("Davis")))
                .andExpect(jsonPath("$.city", is("Sun Prairie")));
    }

    @Test
    void findMissingOwnerReturnsNotFound() throws Exception {
        mockMvc.perform(get("/owners/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOwnerReturnsCreatedOwner() throws Exception {
        OwnerDTO owner = OwnerDTO.builder()
                .firstName("Ada")
                .lastName("Lovelace")
                .address("1 Analytical Engine Way")
                .city("London")
                .telephone("5551234567")
                .build();

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(owner)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName", is("Ada")))
                .andExpect(jsonPath("$.lastName", is("Lovelace")))
                .andExpect(jsonPath("$.city", is("London")));
    }

    @Test
    void updateOwnerChangesStoredOwner() throws Exception {
        OwnerDTO owner = OwnerDTO.builder()
                .firstName("Betty")
                .lastName("Updated")
                .address("Updated address")
                .city("Updated city")
                .telephone("5557654321")
                .build();

        mockMvc.perform(put("/owners/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(owner)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.lastName", is("Updated")))
                .andExpect(jsonPath("$.address", is("Updated address")))
                .andExpect(jsonPath("$.telephone", is("5557654321")));
    }

    @Test
    void deleteOwnerRemovesOwner() throws Exception {
        OwnerDTO owner = OwnerDTO.builder()
                .firstName("Test")
                .lastName("Delete")
                .address("Test address")
                .city("Test city")
                .telephone("5550000000")
                .build();
        String response = mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(owner)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long id = OBJECT_MAPPER.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/owners/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/owners/{id}", id))
                .andExpect(status().isNotFound());
    }
}
