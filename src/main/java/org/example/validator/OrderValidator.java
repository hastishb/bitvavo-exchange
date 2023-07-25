package org.example.validator;

import org.example.domain.OrderSide;

import java.util.Arrays;
import java.util.regex.Pattern;

public class OrderValidator {
    public static void validate(String orderLine) {
        String[] orderData = orderLine.split(",");
        if (orderData.length != 4) throw new OrderLineValidationException("INVALID_LINE", "insufficient data", orderLine);
        Pattern pattern = Pattern.compile("\\d+");
        if (!pattern.matcher(orderData[0]).matches()) throw new OrderLineValidationException("INVALID_ID_FORMAT",  "invalid id format", orderLine);
        if (Arrays.stream(OrderSide.values()).map(Enum::toString).noneMatch(s -> s.equals(orderData[1]))) throw new OrderLineValidationException("INVALID_SIDE_FORMAT", "invalid side", orderLine);
        if (!pattern.matcher(orderData[2]).matches()) throw new OrderLineValidationException("INVALID_PRICE_FORMAT", "invalid price format", orderLine);
        if (!pattern.matcher(orderData[3]).matches()) throw new OrderLineValidationException("INVALID_QUANTITY_FORMAT", "invalid quantity format", orderLine);
        long priceValue = Long.parseLong(orderData[2]);
        long quantityValue = Long.parseLong(orderData[3]);
        if (priceValue > 999999) throw new OrderLineValidationException("INVALID_PRICE", "price more than 999999", orderLine);
        if (priceValue <= 0) throw new OrderLineValidationException("INVALID_PRICE", "price less than or equal to 0", orderLine);
        if (quantityValue > 999999999) throw new OrderLineValidationException("INVALID_QUANTITY", "quantity more than 999999999", orderLine);
        if (quantityValue <= 0) throw new OrderLineValidationException("INVALID_QUANTITY", "quantity less than or equal to 0", orderLine);
    }
}
