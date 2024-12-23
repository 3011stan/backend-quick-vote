package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.CooperativeRequestDTO;
import com.stan.quick_vote.dto.CooperativeResponseDTO;
import com.stan.quick_vote.exceptions.NotFoundException;
import com.stan.quick_vote.model.Cooperative;
import com.stan.quick_vote.repository.CooperativeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CooperativeService {
    private final CooperativeRepository cooperativeRepository;

    public CooperativeResponseDTO findById(String id) {
        Cooperative cooperative = cooperativeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cooperative not found"));

        return mapToResponseDTO(cooperative);
    }

    public CooperativeResponseDTO createCooperative(CooperativeRequestDTO cooperativeRequestDTO) {
        Cooperative cooperative = Cooperative.builder()
                .name(cooperativeRequestDTO.getName())
                .registrationNumber(cooperativeRequestDTO.getRegistrationNumber())
                .address(cooperativeRequestDTO.getAddress())
                .contactEmail(cooperativeRequestDTO.getContactEmail())
                .contactPhone(cooperativeRequestDTO.getContactPhone())
                .build();

        cooperative = cooperativeRepository.save(cooperative);
        return mapToResponseDTO(cooperative);
    }

    private CooperativeResponseDTO mapToResponseDTO(Cooperative cooperative) {
        return CooperativeResponseDTO.builder()
                .id(cooperative.getId())
                .name(cooperative.getName())
                .address(cooperative.getAddress())
                .contactEmail(cooperative.getContactEmail())
                .contactPhone(cooperative.getContactPhone())
                .createdAt(cooperative.getCreatedAt())
                .updatedAt(cooperative.getUpdatedAt())
                .build();
    }
}
