package com.github.rivulet.producer;

import com.github.rivulet.types.Message;
import com.github.rivulet.types.ProducerMessage;
import com.github.rivulet.types.ProduceSuccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produce")
public class ProducerController {
    @Autowired
    private ProducerService producerService;


    @PostMapping("")
    public ProduceSuccess produce(@RequestBody ProducerMessage message) {
        boolean backendMessage=producerService.produceMessage(message);
        return new ProduceSuccess("Message produce to topic successfully", 200);
    }

    @GetMapping("")
    public String demo() {
        return "This is working";
    }

}
