package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.services.VetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vets")
public class VetController {
    private final VetService service;

    public VetController(VetService service) {
        this.service = service;
    }

    @GetMapping
    public List<VetDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public VetDTO findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<VetDTO> create(@RequestBody VetDTO vet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(vet));
    }

    @PutMapping("/{id}")
    public VetDTO update(@PathVariable Integer id, @RequestBody VetDTO vet) {
        return service.update(id, vet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(VetNotFoundException.class)
    public ResponseEntity<Void> notFound() {
        return ResponseEntity.notFound().build();
    }
}
