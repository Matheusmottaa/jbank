package app.matheus.motta.jbank.mapper;

import app.matheus.motta.jbank.controller.dto.DepositMoneyDto;
import app.matheus.motta.jbank.entities.Deposit;

public record DepositMapper (){

    public static Deposit dtoToEntity(DepositMoneyDto dto) {
        var deposit = new Deposit();
        deposit.setDepositValue(dto.amount());
        return deposit;
    }
}
