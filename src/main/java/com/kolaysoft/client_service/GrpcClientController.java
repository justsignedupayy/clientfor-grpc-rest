package com.kolaysoft.client_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GrpcClientController {

    private final GrpcClientService grpcClientService;

    @Autowired
    public GrpcClientController(GrpcClientService grpcClientService) {
        this.grpcClientService = grpcClientService;
    }

    @GetMapping("/sendMultithreadedRequests")
    public String sendMultithreadedRequests(@RequestParam(name = "count", defaultValue = "20") int count) {
        grpcClientService.sendMultithreadedRequests(count);
        return "Sent " + count + " multithreaded requests.";
    }
}

