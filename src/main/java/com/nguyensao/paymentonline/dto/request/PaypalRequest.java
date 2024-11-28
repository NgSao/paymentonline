package com.nguyensao.paymentonline.dto.request;

import lombok.Data;

@Data
public class PaypalRequest {
    private String total;
    private String currency = "USD";
}
