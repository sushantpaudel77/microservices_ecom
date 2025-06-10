package com.microservices_ecom.customer.handler;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
