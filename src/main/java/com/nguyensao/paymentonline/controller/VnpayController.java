package com.nguyensao.paymentonline.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nguyensao.paymentonline.dto.request.VnpayRequest;
import com.nguyensao.paymentonline.service.VnpayService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/vnpay")
@AllArgsConstructor
public class VnpayController {

    private final VnpayService vnpayService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody VnpayRequest paymentRequest) {
        try {
            String paymentUrl = vnpayService.createPayment(paymentRequest);
            return ResponseEntity.ok(paymentUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi tạo thanh toán!");
        }
    }

    @GetMapping("/return")
    public ResponseEntity<String> returnPayment(@RequestParam("vnp_ResponseCode") String responseCode) {
        return vnpayService.handlePaymentReturn(responseCode);
    }
}