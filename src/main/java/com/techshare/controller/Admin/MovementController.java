package com.techshare.controller.Admin;

import com.techshare.http.request.MovementRequest;
import com.techshare.service.Movement.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/movement")
public class MovementController {
    private final MovementService movementService;

    @Autowired
    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MovementRequest movementRequest){
        return new ResponseEntity<>(movementService.createMovement(movementRequest), HttpStatus.CREATED);
    }

    @GetMapping("all")
    public ResponseEntity<?> findAllMovements (){
        return new ResponseEntity<>(movementService.getAllMovements(), HttpStatus.OK);
    }




}
