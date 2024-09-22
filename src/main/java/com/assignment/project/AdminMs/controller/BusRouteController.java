package com.assignment.project.AdminMs.controller;

import com.assignment.project.AdminMs.exception.AdminException;
import com.assignment.project.AdminMs.exception.ResourceNotFoundException;
import com.assignment.project.AdminMs.model.Bus;
import com.assignment.project.AdminMs.model.BusRoute;
import com.assignment.project.AdminMs.repository.BusRouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/adminApi")
@Slf4j
public class BusRouteController {

    private static final Logger logger = LoggerFactory.getLogger(BusRouteController.class);

    @Autowired
    private BusRouteRepository busRouteRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping
    public List<BusRoute> getAllBusRoutes() {
        return busRouteRepository.findAll();
    }

    @GetMapping("/getAvailableSeats/{busNumber}")
    public ResponseEntity<BusRoute> getBusRoute(@PathVariable Long busNumber) {
        try {
            return busRouteRepository.findById(busNumber)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception ex) {
            logger.error("Error occurred while retrieving BusRoute in Admin service");
            throw new AdminException(ex.getMessage());
        }
    }

    @PostMapping("/createBusRoute")
    public List<BusRoute> createBusRoute(@RequestBody List<BusRoute> busRouteList) {
        logger.info("Inside BusRouteController.createBusRoute method");
        List<Bus> busList = busRouteList
                .stream()
                .map(busRoute -> new Bus(busRoute.getBusNumber(), busRoute.getTotalSeats(), String.valueOf(new Date())))
                .toList();
        webClientBuilder
                .build()
                .post()
                .uri("http://localhost:8082/inventoryApi/addInventory")
                .body(BodyInserters.fromValue(busList))
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        response -> {
                            logger.info("Inventory successfully updated.");
                        },
                        ex -> {
                            if (ex.getMessage().contains(HttpStatus.NOT_FOUND.toString())) {
                                logger.error("Inventory service not found");
                                throw new ResourceNotFoundException(ex.getMessage());
                            } else {
                                logger.error("Error occurred: " + ex.getMessage());
                                throw new AdminException(ex.getMessage());
                            }
                        }
                );

        return busRouteRepository.saveAll(busRouteList);
    }

    @PutMapping("/updateBusRoute")
    public ResponseEntity<BusRoute> updateBusRoute(@RequestBody BusRoute bus) {
        if (!busRouteRepository.existsById(bus.getBusNumber())) {
            return ResponseEntity.notFound().build();
        }
        BusRoute busRoute = busRouteRepository.findById(bus.getBusNumber()).get();
        busRoute.setTotalSeats(busRoute.getTotalSeats() - bus.getTotalSeats());
        BusRoute updatedBusRoute = busRouteRepository.save(busRoute);
        return ResponseEntity.ok(updatedBusRoute);
    }

    @DeleteMapping("/{busNumber}")
    public ResponseEntity<Void> deleteBusRoute(@PathVariable Long busNumber) {
        if (!busRouteRepository.existsById(busNumber)) {
            return ResponseEntity.notFound().build();
        }
        busRouteRepository.deleteById(busNumber);
        return ResponseEntity.noContent().build();
    }
}

