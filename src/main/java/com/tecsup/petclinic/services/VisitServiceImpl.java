package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitServiceImpl implements VisitService {
    private final VisitRepository visitRepository;
    private final PetRepository petRepository;

    public VisitServiceImpl(VisitRepository visitRepository, PetRepository petRepository) {
        this.visitRepository = visitRepository;
        this.petRepository = petRepository;
    }

    @Override
    public List<VisitDTO> findAll() {
        return visitRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public VisitDTO findById(Long id) {
        return toDto(visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found: " + id)));
    }

    @Override
    public VisitDTO create(VisitDTO visitDTO) {
        Visit visit = new Visit();
        updateFields(visit, visitDTO);
        return toDto(visitRepository.save(visit));
    }

    @Override
    public VisitDTO update(Long id, VisitDTO visitDTO) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found: " + id));
        updateFields(visit, visitDTO);
        return toDto(visitRepository.save(visit));
    }

    @Override
    public void delete(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found: " + id));
        visitRepository.delete(visit);
    }

    private void updateFields(Visit visit, VisitDTO visitDTO) {
        Pet pet = petRepository.findById(visitDTO.getPetId())
                .orElseThrow(() -> new VisitNotFoundException("Pet not found: " + visitDTO.getPetId()));
        visit.setPet(pet);
        visit.setVisitDate(visitDTO.getVisitDate());
        visit.setDescription(visitDTO.getDescription());
    }

    private VisitDTO toDto(Visit visit) {
        return VisitDTO.builder()
                .id(visit.getId())
                .petId(visit.getPet().getId())
                .visitDate(visit.getVisitDate())
                .description(visit.getDescription())
                .build();
    }
}
