package com.assignment.project.AdminMs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusRoute {
    @Id
    private Long busNumber;
    private String source;
    private String destination;
    private BigDecimal price;
    private Integer totalSeats;
}
