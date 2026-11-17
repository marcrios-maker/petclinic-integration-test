package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mapper.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    private final TypeRepository repository;
    private final TypeMapper mapper;

    public TypeServiceImpl(TypeRepository repository, TypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<TypeDTO> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public TypeDTO findById(Integer id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new TypeNotFoundException("Type not found: " + id)));
    }

    @Override
    public TypeDTO create(TypeDTO type) {
        PetType entity = mapper.toEntity(type);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public TypeDTO update(Integer id, TypeDTO type) {
        PetType entity = repository.findById(id)
                .orElseThrow(() -> new TypeNotFoundException("Type not found: " + id));
        entity.setName(type.getName());
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Integer id) {
        PetType entity = repository.findById(id)
                .orElseThrow(() -> new TypeNotFoundException("Type not found: " + id));
        repository.delete(entity);
    }
}
