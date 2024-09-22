package com.assignment.project.AdminMs.model;

import lombok.Data;

@Data
public class Bus {
    private Long busNumber;
    private int availableSeats;
    private String lastUpdatedDate;

    public Bus(Long busNumber, Integer availableSeats, String lastUpdatedDate) {
        this.busNumber=busNumber;
        this.availableSeats=availableSeats;
        this.lastUpdatedDate=lastUpdatedDate;
    }
}
