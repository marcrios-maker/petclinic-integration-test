package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import java.util.List;

public interface TypeService {
    List<TypeDTO> findAll();
    TypeDTO findById(Integer id);
    TypeDTO create(TypeDTO type);
    TypeDTO update(Integer id, TypeDTO type);
    void delete(Integer id);
}
