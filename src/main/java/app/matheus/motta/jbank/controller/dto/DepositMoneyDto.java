package app.matheus.motta.jbank.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositMoneyDto(@NotNull @DecimalMin(value = "5.00",
        message = "The value should be greater or equal to 5.00") BigDecimal amount) {
}
