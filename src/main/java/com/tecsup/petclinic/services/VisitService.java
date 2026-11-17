package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import java.util.List;

public interface VisitService {
    List<VisitDTO> findAll();
    VisitDTO findById(Long id);
    VisitDTO create(VisitDTO visit);
    VisitDTO update(Long id, VisitDTO visit);
    void delete(Long id);
}
