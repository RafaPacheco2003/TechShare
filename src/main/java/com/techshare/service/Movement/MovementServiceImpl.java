package com.techshare.service.Movement;
import com.techshare.DTO.MovementDTO;
import com.techshare.convert.Movement.ConvertMovement;
import com.techshare.entities.Material;
import com.techshare.entities.MoveType;
import com.techshare.entities.Movement;
import com.techshare.http.request.MovementRequest;
import com.techshare.repositories.MaterialRepository;
import com.techshare.repositories.MovementRepository;
import com.techshare.service.Movement.MovementService;
import com.techshare.service.movementProcessor.MovementProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {


    private final MovementRepository movementRepository;


    private final MaterialRepository materialRepository;

    private final ConvertMovement convertMovement;

    private final Map<MoveType, MovementProcessor> movementProcessorMap;

    @Autowired
    public MovementServiceImpl(
            MovementRepository movementRepository,
            MaterialRepository materialRepository,
            ConvertMovement convertMovement,
            Map<MoveType, MovementProcessor> movementProcessorMap) {
        this.movementRepository = movementRepository;
        this.materialRepository = materialRepository;
        this.convertMovement = convertMovement;
        this.movementProcessorMap = movementProcessorMap;
    }
    @Override
    public MovementDTO createMovement(MovementRequest movementRequest) {
        Movement movementEntity = convertMovement.convertMovementRequestToMovementEntity(movementRequest);
        Material material = materialRepository.getById(movementRequest.getMaterial_id());

        // Obtenemos el procesador adecuado según el tipo de movimiento
        MovementProcessor processor = movementProcessorMap.get(movementEntity.getMoveType());
        if (processor != null) {
            processor.applyMovement(material, movementRequest); // Procesa el movimiento
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no soportado: " + movementEntity.getMoveType());
        }

        // Guardamos el material y el movimiento
        materialRepository.save(material);
        Movement savedMovement = movementRepository.save(movementEntity);

        return convertMovement.convertMovementEntityToMovementDTO(savedMovement);
    }


    @Override
    public Optional<MovementDTO> getMovementById(Long id) {
        return movementRepository.findById(id)
                .map(movement -> Optional.of(convertMovement.convertMovementEntityToMovementDTO(movement)))
                .orElseThrow(() -> new RuntimeException("Movement not found"));
    }

    @Override
    public Optional<MovementDTO> updateMovement(Long id, MovementRequest movementRequest) {
        return movementRepository.findById(id)
                .map(existingMovement -> {
                    convertMovement.convertUpdateMovementRequestToMovement(movementRequest, existingMovement);
                    Movement updatedMovement = movementRepository.save(existingMovement);
                    return Optional.of(convertMovement.convertMovementEntityToMovementDTO(updatedMovement));
                })
                .orElseThrow(() -> new RuntimeException("Movement not found"));
    }

    @Override
    public List<MovementDTO> getAllMovements() {
        return movementRepository.findAll().stream()
                .map(convertMovement::convertMovementEntityToMovementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMovement(Long id) {
        if (!movementRepository.existsById(id)) {
            throw new RuntimeException("Movement not found");
        }
        movementRepository.deleteById(id);
    }
}