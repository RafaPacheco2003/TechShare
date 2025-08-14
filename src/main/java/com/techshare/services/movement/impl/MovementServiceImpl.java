package com.techshare.services.movement.impl;
import com.techshare.https.response.MovementDTO;
import com.techshare.mappers.movement.ConvertMovement;
import com.techshare.entities.Material;
import com.techshare.entities.enums.MoveType;
import com.techshare.entities.Movement;
import com.techshare.https.request.MovementRequest;
import com.techshare.repositories.MaterialRepository;
import com.techshare.repositories.MovementRepository;
import com.techshare.services.movement.MovementService;
import com.techshare.services.MovementProcessor.MovementProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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




    // Métodos públicos del servicio
    @Override
    @Transactional
    public MovementDTO createMovement(MovementRequest movementRequest) {
        Material material = findMaterialById(movementRequest.getMaterial_id());
        Movement movementEntity = convertMovement.convertMovementRequestToMovementEntity(movementRequest);
        
        processMovement(movementEntity, material, movementRequest);
        Movement savedMovement = saveMovementAndMaterial(movementEntity, material);
        
        return convertMovement.convertMovementEntityToMovementDTO(savedMovement);
    }

    @Override
    public Optional<MovementDTO> getMovementById(Long id) {
        Movement movement = findMovementById(id);
        return Optional.of(convertMovement.convertMovementEntityToMovementDTO(movement));
    }

    @Override
    @Transactional
    public Optional<MovementDTO> updateMovement(Long id, MovementRequest movementRequest) {
        Movement existingMovement = findMovementById(id);
        validateMaterialChange(existingMovement, movementRequest.getMaterial_id());
        
        convertMovement.convertUpdateMovementRequestToMovement(movementRequest, existingMovement);
        Movement updatedMovement = movementRepository.save(existingMovement);
        
        return Optional.of(convertMovement.convertMovementEntityToMovementDTO(updatedMovement));
    }
    @Override
    public Page<MovementDTO> getAllMovements(int page, int size, MoveType moveType) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return movementRepository.findAllMovements(
                moveType, // Puede ser null
                pageRequest
        ).map(convertMovement::convertMovementEntityToMovementDTO);
    }
    @Override
    @Transactional
    public void deleteMovement(Long id) {
        if (!movementRepository.existsById(id)) {
            throw new RuntimeException("Movimiento con ID " + id + " no encontrado");
        }
        movementRepository.deleteById(id);
    }




    private void processMovement(Movement movement, Material material, MovementRequest request) {
        MovementProcessor processor = movementProcessorMap.get(movement.getMoveType());
        if (processor == null) {
            throw new IllegalArgumentException("Tipo de movimiento no soportado: " + movement.getMoveType());
        }
        processor.applyMovement(material, request);
    }
    private Material findMaterialById(Long materialId) {
        return materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material con ID " + materialId + " no encontrado"));
    }

    private Movement findMovementById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento con ID " + id + " no encontrado"));
    }



    private Movement saveMovementAndMaterial(Movement movement, Material material) {
        materialRepository.save(material);
        return movementRepository.save(movement);
    }

    private void validateMaterialChange(Movement existingMovement, Long newMaterialId) {
        if (newMaterialId != null &&
                (existingMovement.getMaterial() == null ||
                        !existingMovement.getMaterial().getMaterial_id().equals(newMaterialId))) {
            findMaterialById(newMaterialId);
        }
    }
}