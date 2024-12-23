package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.AssociateRequestDTO;
import com.stan.quick_vote.dto.AssociateResponseDTO;
import com.stan.quick_vote.dto.CooperativeResponseDTO;
import com.stan.quick_vote.model.Associate;
import com.stan.quick_vote.repository.AssociateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssociateService {
    private final AssociateRepository associateRepository;
    private final CooperativeService cooperativeService;

    public AssociateResponseDTO createAssociate(AssociateRequestDTO associateRequestDTO) {
        CooperativeResponseDTO cooperative = cooperativeService
                .findById(associateRequestDTO.getCooperativeId());

        Associate associate = Associate.builder()
                .name(associateRequestDTO.getName())
                .documentNumber(associateRequestDTO.getDocumentNumber())
                .cooperativeId(cooperative.getId())
                .build();

        associate = associateRepository.save(associate);
        return mapToResponseDTO(associate);
    }

    private AssociateResponseDTO mapToResponseDTO(Associate associate) {
        return AssociateResponseDTO.builder()
                .id(associate.getId())
                .name(associate.getName())
                .documentNumber(associate.getDocumentNumber())
                .cooperativeId(associate.getCooperativeId())
                .createdAt(associate.getCreatedAt())
                .updatedAt(associate.getUpdatedAt())
                .build();
    }
}
