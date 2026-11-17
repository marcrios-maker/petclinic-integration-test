package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import java.util.List;

public interface VetService {
    List<VetDTO> findAll();
    VetDTO findById(Integer id);
    VetDTO create(VetDTO vet);
    VetDTO update(Integer id, VetDTO vet);
    void delete(Integer id);
}
