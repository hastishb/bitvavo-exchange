package org.example;

import org.example.domain.Order;
import org.example.validator.OrderLineValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    void testInvalidLine() {
        //give
        String orderLine = "line";
        //when
        OrderLineValidationException exception = assertThrows(OrderLineValidationException.class, () -> new Order(orderLine));

        //then
        assertThat(exception.getCode()).isEqualTo("INVALID_LINE");
    }
    @Test
    void testInvalidId() {
        //give
        String orderLine = "no_number,S,100,100";
        //when
        OrderLineValidationException exception = assertThrows(OrderLineValidationException.class, () -> new Order(orderLine));

        //then
        assertThat(exception.getCode()).isEqualTo("INVALID_ID_FORMAT");
    }

    @Test
    void testInvalidSide() {
        //give
        String orderLine = "1,not_side,100,100";
        //when
        OrderLineValidationException exception = assertThrows(OrderLineValidationException.class, () -> new Order(orderLine));

        //then
        assertThat(exception.getCode()).isEqualTo("INVALID_SIDE_FORMAT");
    }

    @Test
    void testInvalidPriceFormat() {
        //give
        String orderLine = "1,S,no_number,100";
        //when
        OrderLineValidationException exception = assertThrows(OrderLineValidationException.class, () -> new Order(orderLine));

        //then
        assertThat(exception.getCode()).isEqualTo("INVALID_PRICE_FORMAT");
    }

    @Test
    void testInvalidPriceTooHigh() {
        //give
        String orderLine = "1,S,1000000,100";
        //when
        OrderLineValidationException exception = assertThrows(OrderLineValidationException.class, () -> new Order(orderLine));

        //then
        assertThat(exception.getCode()).isEqualTo("INVALID_PRICE");
    }

    @Test
    void testInvalidPriceTooLow() {
        //give
        String orderLine = "1,S,0,100";
        //when
        OrderLineValidationException exception = assertThrows(OrderLineValidationException.class, () -> new Order(orderLine));

        //then
        assertThat(exception.getCode()).isEqualTo("INVALID_PRICE");
    }

    @Test
    void testInvalidQuantityFormat() {
        //give
        String orderLine = "1,S,100,no_number";
        //when
        OrderLineValidationException exception = assertThrows(OrderLineValidationException.class, () -> new Order(orderLine));

        //then
        assertThat(exception.getCode()).isEqualTo("INVALID_QUANTITY_FORMAT");
    }

    @Test
    void testInvalidQuantityTooHigh() {
        //give
        String orderLine = "1,S,100,1000000000";
        //when
        OrderLineValidationException exception = assertThrows(OrderLineValidationException.class, () -> new Order(orderLine));

        //then
        assertThat(exception.getCode()).isEqualTo("INVALID_QUANTITY");
    }

    @Test
    void testInvalidQuantityTooLow() {
        //give
        String orderLine = "1,S,100,0";
        //when
        OrderLineValidationException exception = assertThrows(OrderLineValidationException.class, () -> new Order(orderLine));

        //then
        assertThat(exception.getCode()).isEqualTo("INVALID_QUANTITY");
    }
}
