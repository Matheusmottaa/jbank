package app.matheus.motta.jbank.mapper;

import app.matheus.motta.jbank.controller.dto.CreateWalletDto;
import app.matheus.motta.jbank.entities.Wallet;

import java.math.BigDecimal;

public record WalletMapper() {

    public static Wallet dtoToEntity(CreateWalletDto dto) {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCpf(dto.cpf());
        wallet.setName(dto.name());
        wallet.setEmail(dto.email());
        return wallet;
    }
}
