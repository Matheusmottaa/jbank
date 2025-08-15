package app.matheus.motta.jbank.service;

import app.matheus.motta.jbank.controller.dto.*;
import app.matheus.motta.jbank.entities.Wallet;
import app.matheus.motta.jbank.exception.DeleteWalletException;
import app.matheus.motta.jbank.exception.StatementException;
import app.matheus.motta.jbank.exception.WalletDataAlreadyExistsException;
import app.matheus.motta.jbank.exception.WalletNotFoundException;
import app.matheus.motta.jbank.mapper.DepositMapper;
import app.matheus.motta.jbank.mapper.WalletMapper;
import app.matheus.motta.jbank.repository.DepositRepository;
import app.matheus.motta.jbank.repository.WalletRepository;
import app.matheus.motta.jbank.repository.dto.StatementView;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public StatementDto getStatements(UUID walletId, Integer page, Integer pageSize) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(()-> new WalletNotFoundException("There is no wallet with this id"));

        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "statement_date_time");

        var statements = walletRepository.findStatements(walletId, pageRequest).map(view -> mapToDto(walletId, view));

        return new StatementDto(
                new WalletDto(
                        wallet.getWalletId(),
                        wallet.getCpf(),
                        wallet.getEmail(),
                        wallet.getName(),
                        wallet.getBalance()
                ),
                statements.getContent(),
                new PaginationDto(statements.getNumber(), statements.getSize(), statements.getTotalElements(), statements.getTotalPages())
        );
    }

    private StatementItemDto mapToDto(UUID walletId, StatementView view) {
        if(view.getType().equalsIgnoreCase("deposit")) {
            return mapToDeposit(view);
        }
        if(view.getType().equalsIgnoreCase("transfer")
                && view.getWalletSender().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferSent(walletId, view);
        }

        if(view.getType().equalsIgnoreCase("transfer")
                && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferReceived(walletId, view);
        }

        throw new StatementException("Invalid Type " + view.getType());
    }

    private StatementItemDto mapWhenTransferReceived (UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money received from " + view.getWalletSender(),
                view.getStatementValue(),
                view.getSatementDateTime(),
                StatementOperation.CREDIT
        );
    }

    private static StatementItemDto mapToDeposit(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money deposit",
                view.getStatementValue(),
                view.getSatementDateTime(),
                StatementOperation.CREDIT
        );
    }

    private StatementItemDto mapWhenTransferSent(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money sent to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getSatementDateTime(),
                StatementOperation.DEBIT
        );
    }
}
