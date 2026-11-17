package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.services.VisitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
public class VisitController {
    private final VisitService service;

    public VisitController(VisitService service) {
        this.service = service;
    }

    @GetMapping
    public List<VisitDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public VisitDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<VisitDTO> create(@RequestBody VisitDTO visit) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(visit));
    }

    @PutMapping("/{id}")
    public VisitDTO update(@PathVariable Long id, @RequestBody VisitDTO visit) {
        return service.update(id, visit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(VisitNotFoundException.class)
    public ResponseEntity<Void> notFound() {
        return ResponseEntity.notFound().build();
    }
}
