package com.kolaysoft.client_service.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.kolaysoft.grpc_service.FileServiceGrpc;
import com.kolaysoft.grpc_service.FileRequest;
import com.kolaysoft.grpc_service.FileResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfiguration {

    @Bean
    public FileServiceGrpc.FileServiceBlockingStub fileServiceClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8082)
                .usePlaintext() // Use plaintext if no TLS is enabled
                .build();

        return FileServiceGrpc.newBlockingStub(channel);
    }
}

