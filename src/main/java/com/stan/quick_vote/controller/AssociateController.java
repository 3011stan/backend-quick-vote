package com.stan.quick_vote.controller;

import com.stan.quick_vote.dto.AssociateRequestDTO;
import com.stan.quick_vote.dto.AssociateResponseDTO;
import com.stan.quick_vote.service.AssociateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/associates")
@Tag(name = "Associates", description = "Manage associates")
public class AssociateController {
    private final AssociateService associateService;

    @PostMapping
    @Operation(summary = "Create a new associate", description = "Adds a new associate to the system")
    public AssociateResponseDTO createAssociate(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Associate details")
                                                @RequestBody @Valid AssociateRequestDTO associateRequestDTO) {
        return associateService.createAssociate(associateRequestDTO);
    }
}
