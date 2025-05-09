package com.techshare.config;
import com.techshare.entities.MoveType;
import com.techshare.service.MovementProcessor.AdjustMovement;
import com.techshare.service.MovementProcessor.InMovement;
import com.techshare.service.MovementProcessor.MovementProcessor;
import com.techshare.service.MovementProcessor.OutMovement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.HashMap;

@Configuration
public class MovementProcessorConfig {

    @Bean
    public Map<MoveType, MovementProcessor> movementProcessorMap(
            InMovement inProcessor,
            OutMovement outProcessor,
            AdjustMovement adjustProcessor) {

        Map<MoveType, MovementProcessor> map = new HashMap<>();
        map.put(MoveType.IN, inProcessor);
        map.put(MoveType.OUT, outProcessor);
        map.put(MoveType.ADJUST, adjustProcessor);

        return map;
    }
}
