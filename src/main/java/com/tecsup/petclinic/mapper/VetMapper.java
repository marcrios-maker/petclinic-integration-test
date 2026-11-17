package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VetMapper {
    VetDTO toDto(Vet vet);
    Vet toEntity(VetDTO vetDTO);
    List<VetDTO> toDtoList(List<Vet> vets);
}
