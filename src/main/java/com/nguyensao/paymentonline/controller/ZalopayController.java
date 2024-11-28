package com.nguyensao.paymentonline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nguyensao.paymentonline.service.ZalopayService;

import java.util.Map;

@RestController
@RequestMapping("/api/zalopay")
public class ZalopayController {

    @Autowired
    private ZalopayService zalopayService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody Map<String, Object> orderRequest) {
        try {
            String response = zalopayService.createOrder(orderRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating payment: " + e.getMessage());
        }
    }

    @GetMapping("/order-status/{appTransId}")
    public ResponseEntity<String> getOrderStatus(@PathVariable String appTransId) {
        String response = zalopayService.getOrderStatus(appTransId);
        return ResponseEntity.ok(response);
    }

}
