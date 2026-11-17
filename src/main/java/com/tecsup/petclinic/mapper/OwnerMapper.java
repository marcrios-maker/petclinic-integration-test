package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    OwnerDTO toDto(Owner owner);
    Owner toEntity(OwnerDTO ownerDTO);
    List<OwnerDTO> toDtoList(List<Owner> owners);
}
