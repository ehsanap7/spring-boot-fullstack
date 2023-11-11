package com.ehsan;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    record PingPong(String result){}

    @GetMapping("/Ping")
    public PingPong getPingPong(){
        return new PingPong("Pong 3");
    }

}
