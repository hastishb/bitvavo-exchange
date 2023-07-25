package org.example.validator;

import org.example.domain.OrderSide;

import java.util.Arrays;
import java.util.regex.Pattern;

public class OrderValidator {
    public static void validate(String orderLine, int lineNumber) {
        String[] orderData = orderLine.split(",");
        if (orderData.length != 4) throw new OrderLineValidationException("INVALID_LINE", "insufficient data", lineNumber);
        Pattern pattern = Pattern.compile("\\d+");
        if (!pattern.matcher(orderData[0]).matches()) throw new OrderLineValidationException("INVALID_ID",  "invalid id format", lineNumber);
        if (Arrays.stream(OrderSide.values()).map(Enum::toString).noneMatch(s -> s.equals(orderData[1]))) throw new OrderLineValidationException("INVALID_SIDE", "invalid side", lineNumber);
        if (!pattern.matcher(orderData[2]).matches()) throw new OrderLineValidationException("INVALID_PRICE", "invalid price format", lineNumber);
        if (!pattern.matcher(orderData[3]).matches()) throw new OrderLineValidationException("INVALID_QUANTITY", "invalid quantity format", lineNumber);
        long priceValue = Long.parseLong(orderData[2]);
        long quantityValue = Long.parseLong(orderData[2]);
        if (priceValue > 999999) throw new OrderLineValidationException("INVALID_PRICE", "price more than 999999", lineNumber);
        if (priceValue <= 0) throw new OrderLineValidationException("INVALID_PRICE", "price less than or equal to 0", lineNumber);
        if (quantityValue > 999999999) throw new OrderLineValidationException("INVALID_QUANTITY", "quantity more than 999999999", lineNumber);
        if (quantityValue <= 0) throw new OrderLineValidationException("INVALID_QUANTITY", "quantity less than or equal to 0", lineNumber);
    }
}
