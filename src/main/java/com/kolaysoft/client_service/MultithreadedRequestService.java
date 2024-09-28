package com.kolaysoft.client_service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MultithreadedRequestService {

    @Autowired
    private RestClientService restClientService;

    public void sendConcurrentRequests(int numberOfThreads) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            if (i % 2 == 0) {
                // Add JSON upload tasks
                tasks.add(() -> {
                    String url = "http://localhost:8081/api/rest/writeJson";

                    // Create a Map with the JSON data to send
                    Map<String, Object> dataObject = new HashMap<>();
                    dataObject.put("field1", "value1");
                    dataObject.put("field2", "value2");

                    // Measure the time before the request
                    long startTime = System.nanoTime();

                    // Send the POST request
                    String response = restClientService.sendPostRequest(url, dataObject);

                    // Measure the time after the request
                    long endTime = System.nanoTime();

                    // Calculate response time in milliseconds
                    long responseTime = (endTime - startTime) / 1_000_000;

                    return "JSON Response: " + response + ", Time Taken: " + responseTime + " ms";
                });
            } else {
                // Add file upload tasks
                tasks.add(() -> {
                    String url = "http://localhost:8081/api/rest/uploadFile"; 
                    // File path to be sent for testing
                    String filePath = "/Users/osman/Documents/projects/testfiles/test.txt"; // Replace with the actual file path
                    File file = new File(filePath);
                    if (!file.exists()) {
                        throw new RuntimeException("Test file not found: " + file.getAbsolutePath());
                    }

                    // Measure the time before the request
                    long startTime = System.nanoTime();

                    // Send the POST request to upload the file
                    String response = restClientService.sendFileRequest(url, filePath);

                    // Measure the time after the request
                    long endTime = System.nanoTime();

                    // Calculate response time in milliseconds
                    long responseTime = (endTime - startTime) / 1_000_000;

                    return "File Upload Response: " + response + ", Time Taken: " + responseTime + " ms";
                });
            }
        }

        try {
            List<Future<String>> futures = executorService.invokeAll(tasks);
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}

