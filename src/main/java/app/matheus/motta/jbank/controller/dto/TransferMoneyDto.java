package app.matheus.motta.jbank.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferMoneyDto(
                               @NotNull(message = "The sender id is required")
                               UUID sender,
                               @NotNull(message = "The amount of the transfer is required") @DecimalMin("5.00")
                               BigDecimal value,
                               @NotNull(message = "The receiver id is required")
                               UUID receiver) {
}
