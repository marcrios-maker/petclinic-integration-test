package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mapper.SpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository repository;
    private final SpecialtyMapper mapper;

    public SpecialtyServiceImpl(SpecialtyRepository repository, SpecialtyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<SpecialtyDTO> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public SpecialtyDTO findById(Integer id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new SpecialtyNotFoundException("Specialty not found: " + id)));
    }

    @Override
    public SpecialtyDTO create(SpecialtyDTO specialty) {
        Specialty entity = mapper.toEntity(specialty);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public SpecialtyDTO update(Integer id, SpecialtyDTO specialty) {
        Specialty entity = repository.findById(id)
                .orElseThrow(() -> new SpecialtyNotFoundException("Specialty not found: " + id));
        entity.setName(specialty.getName());
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Integer id) {
        Specialty entity = repository.findById(id)
                .orElseThrow(() -> new SpecialtyNotFoundException("Specialty not found: " + id));
        repository.delete(entity);
    }
}
