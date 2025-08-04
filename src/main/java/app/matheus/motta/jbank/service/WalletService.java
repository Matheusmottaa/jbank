package app.matheus.motta.jbank.service;

import app.matheus.motta.jbank.controller.dto.CreateWalletDto;
import app.matheus.motta.jbank.controller.dto.DepositMoneyDto;
import app.matheus.motta.jbank.entities.Wallet;
import app.matheus.motta.jbank.exception.DeleteWalletException;
import app.matheus.motta.jbank.exception.WalletDataAlreadyExistsException;
import app.matheus.motta.jbank.exception.WalletNotFoundException;
import app.matheus.motta.jbank.mapper.DepositMapper;
import app.matheus.motta.jbank.mapper.WalletMapper;
import app.matheus.motta.jbank.repository.DepositRepository;
import app.matheus.motta.jbank.repository.WalletRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;

    public WalletService(final WalletRepository walletRepository,
                         final DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
    }

    @Transactional
    public Wallet createWallet(CreateWalletDto dto) {
        var walletDb = walletRepository.findByCpfOrEmail(dto.cpf(), dto.email());

        if(walletDb.isPresent()) {
            throw new WalletDataAlreadyExistsException("cpf or email already exists");
        }

        var wallet = WalletMapper.dtoToEntity(dto);

        return walletRepository.saveAndFlush(wallet);
    }

    @Transactional
    public boolean deleteWallet(UUID walletId) {

        var wallet = walletRepository.findById(walletId);

        if(wallet.isPresent()) {

            if(wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new DeleteWalletException("The balance is not zero. The current amount is $" + wallet.get().getBalance());
            }

            walletRepository.deleteById(walletId);
        }
        return wallet.isPresent();
    }

    @Transactional
    public void depositMoney(UUID walletId,
                             DepositMoneyDto dto,
                             String ipAddress) {

        var wallet = walletRepository.findById(walletId)
                        .orElseThrow(() -> new WalletNotFoundException("There is no wallet with this id!"));

        var deposit = DepositMapper.dtoToEntity(dto);
        deposit.setWallet(wallet);
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        depositRepository.saveAndFlush(deposit);
        wallet.setBalance(wallet.getBalance().add(dto.amount()));
        walletRepository.saveAndFlush(wallet);
    }
}
