package com.tecsup.petclinic.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VetDTO {
    private Integer id;
    private String firstName;
    private String lastName;
}
