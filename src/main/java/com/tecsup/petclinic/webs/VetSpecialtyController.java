package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.services.VetSpecialtyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vet-specialties")
public class VetSpecialtyController {
    private final VetSpecialtyService service;

    public VetSpecialtyController(VetSpecialtyService service) {
        this.service = service;
    }

    @GetMapping
    public List<VetSpecialtyDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{vetId}/{specialtyId}")
    public VetSpecialtyDTO findByIds(@PathVariable Integer vetId, @PathVariable Integer specialtyId) {
        return service.findByIds(vetId, specialtyId);
    }

    @PostMapping
    public ResponseEntity<VetSpecialtyDTO> create(@RequestBody VetSpecialtyDTO vetSpecialty) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(vetSpecialty));
    }

    @DeleteMapping("/{vetId}/{specialtyId}")
    public ResponseEntity<Void> delete(@PathVariable Integer vetId, @PathVariable Integer specialtyId) {
        service.delete(vetId, specialtyId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(VetSpecialtyNotFoundException.class)
    public ResponseEntity<Void> notFound() {
        return ResponseEntity.notFound().build();
    }
}
