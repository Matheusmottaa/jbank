package app.matheus.motta.jbank.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementItemDto(String statementId,
                               String type,
                               String literal,
                               BigDecimal value,
                               LocalDateTime datetime,
                               StatementOperation operation) {
}
