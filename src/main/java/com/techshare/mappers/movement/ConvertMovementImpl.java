package com.techshare.mappers.movement;

import com.techshare.entities.Product;
import com.techshare.entities.UserEntity;
import com.techshare.https.response.MovementDTO;
import com.techshare.entities.Movement;
import com.techshare.https.request.MovementRequest;
import com.techshare.repositories.ProductRepository;

import com.techshare.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertMovementImpl implements ConvertMovement {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Movement convertMovementRequestToMovementEntity(MovementRequest movementRequest) {
        Movement movement = new Movement();
        movement.setMoveType(movementRequest.getMoveType());
        movement.setQuantity(movementRequest.getQuantity());
        movement.setComment(movementRequest.getComment());

        // Obtener el material completo de la base de datos
        Product product = productRepository.findById(movementRequest.getProduct_id())
                .orElseThrow(() -> new RuntimeException("Material no encontrado con ID: " + movementRequest.getProduct_id()));
        movement.setMaterial(product);

        UserEntity user = userRepository.findById(movementRequest.getProduct_id())
                .orElseThrow(() -> new RuntimeException("User no encontrado con ID: " + movementRequest.getProduct_id()));
        movement.setUser(user);


        return movement;
    }    @Override
    public void convertUpdateMovementRequestToMovement(MovementRequest movementRequest, Movement existingMovement) {
        existingMovement.setMoveType(movementRequest.getMoveType());
        existingMovement.setQuantity(movementRequest.getQuantity());
        existingMovement.setComment(movementRequest.getComment());

        // Si es necesario actualizar el material
        if (movementRequest.getProduct_id() != null) {
            Product product = productRepository.findById(movementRequest.getProduct_id())
                    .orElseThrow(() -> new RuntimeException("Material no encontrado con ID: " + movementRequest.getProduct_id()));
            existingMovement.setMaterial(product);
        }
    }

    @Override
    public MovementDTO convertMovementEntityToMovementDTO(Movement movement) {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setMovement_id(movement.getMovement_id());
        movementDTO.setMoveType(movement.getMoveType());
        movementDTO.setQuantity(movement.getQuantity());
        movementDTO.setComment(movement.getComment());
        movementDTO.setDate(movement.getDate());

        movementDTO.setProduct_id(movement.getMaterial().getProduct_id());
        movementDTO.setProduct_name(movement.getMaterial().getName());

        movementDTO.setUser_id(movement.getUser().getUser_id());
        movementDTO.setUser_name(movement.getUser().getFirstName() + " " + movement.getUser().getLastName());



        return movementDTO;
    }
}
