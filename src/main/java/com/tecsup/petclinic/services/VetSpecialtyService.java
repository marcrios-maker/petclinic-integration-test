package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import java.util.List;

public interface VetSpecialtyService {
    List<VetSpecialtyDTO> findAll();
    VetSpecialtyDTO findByIds(Integer vetId, Integer specialtyId);
    VetSpecialtyDTO create(VetSpecialtyDTO vetSpecialty);
    void delete(Integer vetId, Integer specialtyId);
}
