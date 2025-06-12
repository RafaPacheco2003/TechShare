package com.techshare.service.Movement.impl;
import com.techshare.DTO.MovementDTO;
import com.techshare.convert.Movement.ConvertMovement;
import com.techshare.entities.Material;
import com.techshare.entities.MoveType;
import com.techshare.entities.Movement;
import com.techshare.http.request.MovementRequest;
import com.techshare.repositories.MaterialRepository;
import com.techshare.repositories.MovementRepository;
import com.techshare.service.Movement.MovementService;
import com.techshare.service.MovementProcessor.MovementProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public MovementDTO createMovement(MovementRequest movementRequest) {
        // Verificar si el material existe
        Material material = materialRepository.findById(movementRequest.getMaterial_id())
                .orElseThrow(() -> new RuntimeException("Material con ID " + movementRequest.getMaterial_id() + " no encontrado"));
        
        Movement movementEntity = convertMovement.convertMovementRequestToMovementEntity(movementRequest);

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
                .map(convertMovement::convertMovementEntityToMovementDTO)
                .map(Optional::of)
                .orElseThrow(() -> new RuntimeException("Movimiento con ID " + id + " no encontrado"));
    }

    @Override
    @Transactional
    public Optional<MovementDTO> updateMovement(Long id, MovementRequest movementRequest) {
        // Verificar si el movimiento existe
        Movement existingMovement = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento con ID " + id + " no encontrado"));
        
        // Si el movimiento cambia de material, verificar que el nuevo material existe
        if (movementRequest.getMaterial_id() != null && 
            (existingMovement.getMaterial() == null || 
             !existingMovement.getMaterial().getMaterial_id().equals(movementRequest.getMaterial_id()))) {
            
            materialRepository.findById(movementRequest.getMaterial_id())
                .orElseThrow(() -> new RuntimeException("Material con ID " + movementRequest.getMaterial_id() + " no encontrado"));
        }
        
        // Actualizar el movimiento
        convertMovement.convertUpdateMovementRequestToMovement(movementRequest, existingMovement);
        Movement updatedMovement = movementRepository.save(existingMovement);
        
        return Optional.of(convertMovement.convertMovementEntityToMovementDTO(updatedMovement));
    }    @Override    public org.springframework.data.domain.Page<MovementDTO> getAllMovements(int page, int size) {
        org.springframework.data.domain.PageRequest pageRequest = org.springframework.data.domain.PageRequest.of(page, size);
        return movementRepository.findAllByOrderByMovementIdDesc(pageRequest)
                .map(convertMovement::convertMovementEntityToMovementDTO);
    }

    @Override
    @Transactional
    public void deleteMovement(Long id) {
        if (!movementRepository.existsById(id)) {
            throw new RuntimeException("Movimiento con ID " + id + " no encontrado");
        }
        movementRepository.deleteById(id);
    }
}