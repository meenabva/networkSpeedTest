package com.speed.test.speedTest.Controller;

import com.speed.test.speedTest.Service.SpeedTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpeedTestController {

    Logger log = LoggerFactory.getLogger(SpeedTestController.class);
    @Autowired
    SpeedTestService speedTestService;

    @GetMapping("/ping")
    public ResponseEntity<String> getPingSpeed(){
        log.debug("REST request to get ping speed");
        return ResponseEntity.ok(speedTestService.getPingSpeed());
    }

    @GetMapping("/download")
    public ResponseEntity<Double> getDownloadSpeed(){
        log.debug("REST request to get download speed");
        try {
            Double speed = speedTestService.getDownloadSpeed();
            return ResponseEntity.ok(speed);
        } catch (Exception e){
            log.debug("Exception while getting download speed: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/upload")
    public ResponseEntity<Double> getUploadSpeed(){
        log.debug("REST request to get upload speed");
        try {
            Double speed = speedTestService.getUploadSpeed();
            return ResponseEntity.ok(speed);
        } catch (Exception e){
            log.debug("Exception while getting upload speed: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
