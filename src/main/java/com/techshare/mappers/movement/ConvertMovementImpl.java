package com.techshare.mappers.movement;

import com.techshare.dto.MovementDTO;
import com.techshare.entities.Material;
import com.techshare.entities.Movement;
import com.techshare.http.request.MovementRequest;
import com.techshare.repositories.MaterialRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertMovementImpl implements ConvertMovement {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public Movement convertMovementRequestToMovementEntity(MovementRequest movementRequest) {
        Movement movement = new Movement();
        movement.setMoveType(movementRequest.getMoveType());
        movement.setQuantity(movementRequest.getQuantity());
        movement.setComment(movementRequest.getComment());

        // Obtener el material completo de la base de datos
        Material material = materialRepository.findById(movementRequest.getMaterial_id())
                .orElseThrow(() -> new RuntimeException("Material no encontrado con ID: " + movementRequest.getMaterial_id()));
        movement.setMaterial(material);

        return movement;
    }    @Override
    public void convertUpdateMovementRequestToMovement(MovementRequest movementRequest, Movement existingMovement) {
        existingMovement.setMoveType(movementRequest.getMoveType());
        existingMovement.setQuantity(movementRequest.getQuantity());
        existingMovement.setComment(movementRequest.getComment());

        // Si es necesario actualizar el material
        if (movementRequest.getMaterial_id() != null) {
            Material material = materialRepository.findById(movementRequest.getMaterial_id())
                    .orElseThrow(() -> new RuntimeException("Material no encontrado con ID: " + movementRequest.getMaterial_id()));
            existingMovement.setMaterial(material);
        }
    }

    @Override
    public MovementDTO convertMovementEntityToMovementDTO(Movement movement) {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setMovement_id(movement.getMovement_id());
        movementDTO.setMoveType(movement.getMoveType());
        movementDTO.setQuantity(movement.getQuantity());
        movementDTO.setComment(movement.getComment());
        movementDTO.setDate(movement.getDate());        if (movement.getMaterial() != null) {
            movementDTO.setMaterial_id(movement.getMaterial().getMaterial_id());
            movementDTO.setMaterial_name(movement.getMaterial().getName());
        }

        return movementDTO;
    }
}
