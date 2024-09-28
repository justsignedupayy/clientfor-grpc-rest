package com.kolaysoft.client_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @Autowired
    private MultithreadedRequestService requestService;

    @GetMapping("/send-requests")
    public String sendRequests(@RequestParam int numberOfThreads) {
        requestService.sendConcurrentRequests(numberOfThreads);
        return "Requests sent!";
    }
}
