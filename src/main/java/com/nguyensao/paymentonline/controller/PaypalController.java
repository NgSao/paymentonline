package com.nguyensao.paymentonline.controller;

import com.nguyensao.paymentonline.dto.request.PaypalRequest;
import com.nguyensao.paymentonline.service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal")
public class PaypalController {

    @Autowired
    private PaypalService paypalService;

    @PostMapping()
    public ResponseEntity<String> createPayment(@RequestBody PaypalRequest paypalRequest) {
        double amount = 0;

        try {
            amount = Double.parseDouble(paypalRequest.getTotal()) * 100.0;
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Số tiền không hợp lệ");
        }

        try {
            String approvalUrl = paypalService.createPayment(amount, paypalRequest.getCurrency(),
                    "Payment for Order",
                    "http://localhost:8080/api/paypal/cancel",
                    "http://localhost:8080/api/paypal/success");
            return ResponseEntity.ok(approvalUrl);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating payment: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> success(@RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            return ResponseEntity.ok("Payment Success! Payment ID: " + payment.getId());
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment execution failed: " + e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment was canceled");
    }
}
