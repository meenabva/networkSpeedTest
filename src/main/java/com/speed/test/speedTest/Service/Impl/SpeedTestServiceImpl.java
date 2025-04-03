package com.speed.test.speedTest.Service.Impl;

import com.speed.test.speedTest.Service.SpeedTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class SpeedTestServiceImpl implements SpeedTestService {

    Logger log = LoggerFactory.getLogger(SpeedTestServiceImpl.class);
    private String HOST_URL = "google.com";

    @Override
    public Double getUploadSpeed() {
        return null;
    }

    @Override
    public Double getDownloadSpeed() {
        return null;
    }

    @Override
    public String getPingSpeed() {
        try{
            ProcessBuilder processBuilder = new ProcessBuilder("ping", "-n", "4", HOST_URL);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            while(line != null){
                log.debug("Output line: {}", line);
                if(line.contains("Average = ")){
                    return line.split("Average = ")[1];
                }
                line = reader.readLine();
            }
        } catch(Exception e){
            throw new RuntimeException("Unable to ping host");
        }
        return null;
    }
}
