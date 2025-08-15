package app.matheus.motta.jbank.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDto(UUID walletId,
                        String cpf,
                        String email,
                        String name,
                        BigDecimal balance) {
}
