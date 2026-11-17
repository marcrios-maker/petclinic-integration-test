package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VetSpecialtyServiceImpl implements VetSpecialtyService {
    private final JdbcTemplate jdbcTemplate;
    private final VetRepository vetRepository;
    private final SpecialtyRepository specialtyRepository;

    public VetSpecialtyServiceImpl(JdbcTemplate jdbcTemplate, VetRepository vetRepository,
                                   SpecialtyRepository specialtyRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.vetRepository = vetRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public List<VetSpecialtyDTO> findAll() {
        return jdbcTemplate.query("SELECT vet_id, specialty_id FROM vet_specialties ORDER BY vet_id, specialty_id",
                (rs, rowNum) -> VetSpecialtyDTO.builder()
                        .vetId(rs.getInt("vet_id"))
                        .specialtyId(rs.getInt("specialty_id"))
                        .build());
    }

    @Override
    public VetSpecialtyDTO findByIds(Integer vetId, Integer specialtyId) {
        return jdbcTemplate.query("SELECT vet_id, specialty_id FROM vet_specialties WHERE vet_id = ? AND specialty_id = ?",
                        (rs, rowNum) -> VetSpecialtyDTO.builder()
                                .vetId(rs.getInt("vet_id"))
                                .specialtyId(rs.getInt("specialty_id"))
                                .build(), vetId, specialtyId)
                .stream().findFirst()
                .orElseThrow(() -> new VetSpecialtyNotFoundException(
                        "Vet-specialty association not found: " + vetId + ", " + specialtyId));
    }

    @Override
    @Transactional
    public VetSpecialtyDTO create(VetSpecialtyDTO vetSpecialty) {
        Integer vetId = vetSpecialty.getVetId();
        Integer specialtyId = vetSpecialty.getSpecialtyId();
        if (!vetRepository.existsById(vetId)) {
            throw new VetSpecialtyNotFoundException("Vet not found: " + vetId);
        }
        if (!specialtyRepository.existsById(specialtyId)) {
            throw new VetSpecialtyNotFoundException("Specialty not found: " + specialtyId);
        }
        jdbcTemplate.update("INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (?, ?)", vetId, specialtyId);
        return findByIds(vetId, specialtyId);
    }

    @Override
    @Transactional
    public void delete(Integer vetId, Integer specialtyId) {
        findByIds(vetId, specialtyId);
        jdbcTemplate.update("DELETE FROM vet_specialties WHERE vet_id = ? AND specialty_id = ?", vetId, specialtyId);
    }
}
