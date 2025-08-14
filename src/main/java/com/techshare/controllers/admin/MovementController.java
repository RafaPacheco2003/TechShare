package com.techshare.controllers.admin;

import com.techshare.entities.enums.MoveType;
import com.techshare.https.response.MovementDTO;
import com.techshare.https.request.MovementRequest;
import com.techshare.services.movement.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    }
    @GetMapping("/all")
    public ResponseEntity<Page<MovementDTO>> findAllMovements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) MoveType moveType) {

        return ResponseEntity.ok(
                movementService.getAllMovements(page, size, moveType)
        );
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
