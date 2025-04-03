package com.speed.test.speedTest.Controller;

import com.speed.test.speedTest.Service.SpeedTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpeedTestController {

    @Autowired
    SpeedTestService speedTestService;

    @GetMapping("/ping")
    public ResponseEntity<String> getPingSpeed(){
        return ResponseEntity.ok(speedTestService.getPingSpeed());
    }
}
