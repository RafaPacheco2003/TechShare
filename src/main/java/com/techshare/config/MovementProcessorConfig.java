package com.techshare.config;
import com.techshare.entities.enums.MoveType;
import com.techshare.services.MovementProcessor.AdjustMovement;
import com.techshare.services.MovementProcessor.InMovement;
import com.techshare.services.MovementProcessor.MovementProcessor;
import com.techshare.services.MovementProcessor.OutMovement;
import com.techshare.services.MovementProcessor.SaleMovement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.HashMap;

@Configuration
public class MovementProcessorConfig {    @Bean
    public Map<MoveType, MovementProcessor> movementProcessorMap(
            InMovement inProcessor,
            OutMovement outProcessor,
            AdjustMovement adjustProcessor,
            SaleMovement saleProcessor) {

        Map<MoveType, MovementProcessor> map = new HashMap<>();
        map.put(MoveType.IN, inProcessor);
        map.put(MoveType.OUT, outProcessor);
        map.put(MoveType.ADJUST, adjustProcessor);
        map.put(MoveType.SALE, saleProcessor);

        return map;
    }
}
