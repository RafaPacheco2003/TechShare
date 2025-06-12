package com.techshare.controller.Admin;

import com.techshare.DTO.MovementDTO;
import com.techshare.http.request.MovementRequest;
import com.techshare.service.Movement.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    }    @GetMapping("/all")
    public ResponseEntity<org.springframework.data.domain.Page<MovementDTO>> findAllMovements(
            @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(movementService.getAllMovements(page, 9), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<MovementDTO>> findMovementById(@PathVariable Long id){
        return new ResponseEntity<>(movementService.getMovementById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<MovementDTO>> updateMovement(@PathVariable Long id, @RequestBody MovementRequest movementRequest){
        return new ResponseEntity<>(movementService.updateMovement(id, movementRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable Long id){
        movementService.deleteMovement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
