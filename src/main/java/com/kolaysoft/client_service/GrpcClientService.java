package com.kolaysoft.client_service;

import com.kolaysoft.grpc_service.FileServiceGrpc;
import com.kolaysoft.grpc_service.FileRequest;
import com.kolaysoft.grpc_service.FileResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GrpcClientService {

    private final FileServiceGrpc.FileServiceBlockingStub fileServiceClient;

    @Autowired
    public GrpcClientService(FileServiceGrpc.FileServiceBlockingStub fileServiceClient) {
        this.fileServiceClient = fileServiceClient;
    }

    public void sendMultithreadedFileRequests(int numberOfRequests) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long startTime = System.nanoTime();

        for (int i = 0; i < numberOfRequests; i++) {
            final int requestId = i;
            executor.submit(() -> {
                try {
                    long requestStartTime = System.nanoTime();

                    // Create file upload request
                    FileRequest request = FileRequest.newBuilder().setFileName("file_" + requestId + ".txt")
                            .setFileContent("Sample file content for request " + requestId).build();

                    // Send the request
                    FileResponse response = fileServiceClient.uploadFile(request);

                    long requestEndTime = System.nanoTime();
                    long duration = requestEndTime - requestStartTime;

                    System.out.println("Response for file request " + requestId + ": " + response.getMessage() +
                            ". Time taken: " + (duration / 1_000_000.0) + " ms");
                } catch (Exception e) {
                    System.err.println("Error in file request " + requestId + ": " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all threads to finish
        }

        long endTime = System.nanoTime();
        long totalDuration = endTime - startTime;
        System.out.println("Total time for file requests: " + (totalDuration / 1_000_000.0) + " ms");
    }
}


