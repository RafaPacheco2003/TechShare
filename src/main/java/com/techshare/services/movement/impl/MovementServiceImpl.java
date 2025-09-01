package com.techshare.services.movement.impl;
import com.techshare.entities.Product;
import com.techshare.https.response.MovementDTO;
import com.techshare.mappers.movement.ConvertMovement;
import com.techshare.entities.enums.MoveType;
import com.techshare.entities.Movement;
import com.techshare.https.request.MovementRequest;
import com.techshare.repositories.ProductRepository;
import com.techshare.repositories.MovementRepository;
import com.techshare.services.movement.MovementService;
import com.techshare.services.movementProcessor.MovementProcessor;

import com.techshare.util.JwtDecoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final ProductRepository productRepository;
    private final ConvertMovement convertMovement;
    private final Map<MoveType, MovementProcessor> movementProcessorMap;
    private final JwtDecoderService jwtDecoderService; // o UserIdExtractorService

    @Autowired
    public MovementServiceImpl(
            MovementRepository movementRepository,
            ProductRepository productRepository,
            ConvertMovement convertMovement,
            Map<MoveType, MovementProcessor> movementProcessorMap, JwtDecoderService jwtDecoderService) {
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
        this.convertMovement = convertMovement;
        this.movementProcessorMap = movementProcessorMap;
        this.jwtDecoderService = jwtDecoderService;
    }



    @Override
    @Transactional
    public MovementDTO createMovementSale(MovementRequest movementRequest) {
        Product product = findMaterialById(movementRequest.getProduct_id());


        Movement movementEntity = convertMovement.convertMovementRequestToMovementEntity(movementRequest);

        processMovement(movementEntity, product, movementRequest);
        Movement savedMovement = saveMovementAndMaterial(movementEntity, product);

        return convertMovement.convertMovementEntityToMovementDTO(savedMovement);
    }



    @Override
    @Transactional
    public MovementDTO createMovement(MovementRequest movementRequest, String authorizationHeader) {
        Product product = findMaterialById(movementRequest.getProduct_id());

        Long userId = jwtDecoderService.extractUserIdFromHeader(authorizationHeader);

        movementRequest.setUser_id(userId);

        Movement movementEntity = convertMovement.convertMovementRequestToMovementEntity(movementRequest);
        
        processMovement(movementEntity, product, movementRequest);
        Movement savedMovement = saveMovementAndMaterial(movementEntity, product);
        
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
        validateMaterialChange(existingMovement, movementRequest.getProduct_id());
        
        convertMovement.convertUpdateMovementRequestToMovement(movementRequest, existingMovement);
        Movement updatedMovement = movementRepository.save(existingMovement);
        
        return Optional.of(convertMovement.convertMovementEntityToMovementDTO(updatedMovement));
    }
    @Override
    public Page<MovementDTO> getAllMovements(int page, int size, MoveType moveType) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("movement_id").descending());

        return movementRepository.findAllMovements(moveType, pageRequest)
                .map(this::convertToSimplifiedDTO);
    }

    private MovementDTO convertToSimplifiedDTO(Movement movement) {
        MovementDTO dto = new MovementDTO();
        dto.setMovement_id(movement.getMovement_id());
        dto.setMoveType(movement.getMoveType());
        dto.setQuantity(movement.getQuantity());
        dto.setComment(movement.getComment());
        dto.setDate(movement.getDate());

        if (movement.getMaterial() != null) {
            dto.setProduct_id(movement.getMaterial().getProduct_id());
            dto.setProduct_name(movement.getMaterial().getName());
        }

        return dto;
    }
    @Override
    @Transactional
    public void deleteMovement(Long id) {
        if (!movementRepository.existsById(id)) {
            throw new RuntimeException("Movimiento con ID " + id + " no encontrado");
        }
        movementRepository.deleteById(id);
    }




    private void processMovement(Movement movement, Product product, MovementRequest request) {
        MovementProcessor processor = movementProcessorMap.get(movement.getMoveType());
        if (processor == null) {
            throw new IllegalArgumentException("Tipo de movimiento no soportado: " + movement.getMoveType());
        }
        processor.applyMovement(product, request);
    }
    private Product findMaterialById(Long materialId) {
        return productRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material con ID " + materialId + " no encontrado"));
    }

    private Movement findMovementById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento con ID " + id + " no encontrado"));
    }



    private Movement saveMovementAndMaterial(Movement movement, Product product) {
        productRepository.save(product);
        return movementRepository.save(movement);
    }

    private void validateMaterialChange(Movement existingMovement, Long newMaterialId) {
        if (newMaterialId != null &&
                (existingMovement.getMaterial() == null ||
                        !existingMovement.getMaterial().getProduct_id().equals(newMaterialId))) {
            findMaterialById(newMaterialId);
        }
    }
}