package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mapper.OwnerMapper;
import com.tecsup.petclinic.repositories.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository repository;
    private final OwnerMapper mapper;

    public OwnerServiceImpl(OwnerRepository repository, OwnerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<OwnerDTO> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public OwnerDTO findById(Long id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found: " + id)));
    }

    @Override
    public OwnerDTO create(OwnerDTO owner) {
        Owner entity = mapper.toEntity(owner);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public OwnerDTO update(Long id, OwnerDTO owner) {
        Owner entity = repository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found: " + id));
        entity.setFirstName(owner.getFirstName());
        entity.setLastName(owner.getLastName());
        entity.setAddress(owner.getAddress());
        entity.setCity(owner.getCity());
        entity.setTelephone(owner.getTelephone());
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        Owner entity = repository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found: " + id));
        repository.delete(entity);
    }
}
