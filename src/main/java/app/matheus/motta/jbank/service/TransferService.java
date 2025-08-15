package app.matheus.motta.jbank.service;

import app.matheus.motta.jbank.controller.dto.TransferMoneyDto;
import app.matheus.motta.jbank.entities.Transfer;
import app.matheus.motta.jbank.entities.Wallet;
import app.matheus.motta.jbank.exception.TransferException;
import app.matheus.motta.jbank.exception.WalletNotFoundException;
import app.matheus.motta.jbank.repository.TransferRepository;
import app.matheus.motta.jbank.repository.WalletRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(final TransferRepository transferRepository,
                           final WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void transferMoney(final TransferMoneyDto dto) {

        var sender = walletRepository.findById(dto.sender())
                .orElseThrow(()-> new WalletNotFoundException("Sender does not exists!"));

        var receiver = walletRepository.findById(dto.receiver())
                .orElseThrow(() -> new WalletNotFoundException("Receiver does not exists!"));

        if(sender.getBalance().compareTo(dto.value()) == -1) {
            throw new TransferException(
                    "Insufficient balance. Your current balance is $" + sender.getBalance());
        }

        presistTransfer(dto, receiver, sender);
        updateWallets(dto, sender, receiver);
    }

    private void updateWallets(TransferMoneyDto dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));

        walletRepository.saveAllAndFlush(List.of(sender, receiver));
    }

    private void presistTransfer(TransferMoneyDto dto, Wallet receiver, Wallet sender) {
        var transfer = new Transfer();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setTransferValue(dto.value());
        transfer.setTransferDateTime(LocalDateTime.now());
        transferRepository.saveAndFlush(transfer);
    }
}
