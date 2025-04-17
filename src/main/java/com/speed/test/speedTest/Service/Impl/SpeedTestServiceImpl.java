package com.speed.test.speedTest.Service.Impl;

import com.speed.test.speedTest.Service.SpeedTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

@Service
public class SpeedTestServiceImpl implements SpeedTestService {

    Logger log = LoggerFactory.getLogger(SpeedTestServiceImpl.class);
    private static final String HOST_URL = "google.com";

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String DOWNLOAD_URL = "http://speedtest.tele2.net/10MB.zip";

    private static final String UPLOAD_URL = "https://httpbin.org/post";

    @Override
    public Double getUploadSpeed() {
        log.debug("Request to get uplaod speed");
        int uploadFileSize = 10 * 1024 * 1024; //10mb
        byte[] inputArray = new byte[uploadFileSize];
        new Random().nextBytes(inputArray);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(inputArray) {
            @Override
            public String getFilename() {
                return "test.bin";
            }
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        long startTime = System.currentTimeMillis();
        ResponseEntity<String> response = restTemplate.postForEntity(UPLOAD_URL, requestEntity, String.class);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double timeInSec = duration/ 1000.0;
        double speedMbps =  10 * 8 / timeInSec;
        log.debug("Downloaded {} MB in {} s --→ {} Mbps", uploadFileSize, timeInSec, speedMbps);
        return speedMbps;
    }

    @Override
    public Double getDownloadSpeed() {
        log.debug("Request to get download speed");
        Double speedMbps = null;
        long startTime = System.currentTimeMillis();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(DOWNLOAD_URL, byte[].class);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        byte[] downloadData = response.getBody();
        if (downloadData == null) {
            throw new RuntimeException("Error getting the download speed.");
        }
        double fileSizeInMb = downloadData.length / (1024.0 * 1024.0);
        double timeInSec = duration / 1000.0;
        speedMbps = (fileSizeInMb * 8) / timeInSec;
        log.debug("Downloaded {} MB in {} s --→ {} Mbps", fileSizeInMb, timeInSec, speedMbps);
        return speedMbps;
    }

    @Override
    public String getPingSpeed() {
        log.debug("Request to get ping speed");
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
