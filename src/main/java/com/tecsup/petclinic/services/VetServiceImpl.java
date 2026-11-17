package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mapper.VetMapper;
import com.tecsup.petclinic.repositories.VetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VetServiceImpl implements VetService {
    private final VetRepository repository;
    private final VetMapper mapper;

    public VetServiceImpl(VetRepository repository, VetMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<VetDTO> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public VetDTO findById(Integer id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new VetNotFoundException("Vet not found: " + id)));
    }

    @Override
    public VetDTO create(VetDTO vet) {
        Vet entity = mapper.toEntity(vet);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public VetDTO update(Integer id, VetDTO vet) {
        Vet entity = repository.findById(id).orElseThrow(() -> new VetNotFoundException("Vet not found: " + id));
        entity.setFirstName(vet.getFirstName());
        entity.setLastName(vet.getLastName());
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Integer id) {
        Vet entity = repository.findById(id).orElseThrow(() -> new VetNotFoundException("Vet not found: " + id));
        repository.delete(entity);
    }
}
