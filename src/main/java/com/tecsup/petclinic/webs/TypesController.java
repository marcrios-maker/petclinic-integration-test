package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.services.TypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/types")
public class TypesController {
    private final TypeService service;

    public TypesController(TypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TypeDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TypeDTO findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<TypeDTO> create(@RequestBody TypeDTO type) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(type));
    }

    @PutMapping("/{id}")
    public TypeDTO update(@PathVariable Integer id, @RequestBody TypeDTO type) {
        return service.update(id, type);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(TypeNotFoundException.class)
    public ResponseEntity<Void> notFound() {
        return ResponseEntity.notFound().build();
    }
}
