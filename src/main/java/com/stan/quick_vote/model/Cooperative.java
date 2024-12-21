package com.stan.quick_vote.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "cooperatives")
public class Cooperative {
    @Id
    private String id;

    private String name;

    private String registrationNumber;

    private String address;

    private String contactEmail;

    private String contactPhone;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
