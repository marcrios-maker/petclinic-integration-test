package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.PetType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TypeMapper {
    TypeDTO toDto(PetType type);
    PetType toEntity(TypeDTO typeDTO);
    List<TypeDTO> toDtoList(List<PetType> types);
}
