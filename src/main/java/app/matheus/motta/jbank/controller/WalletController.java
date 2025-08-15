package app.matheus.motta.jbank.controller;

import app.matheus.motta.jbank.controller.dto.CreateWalletDto;
import app.matheus.motta.jbank.controller.dto.DepositMoneyDto;
import app.matheus.motta.jbank.controller.dto.StatementDto;
import app.matheus.motta.jbank.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(final WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDto dto) {
        var wallet = walletService.createWallet(dto);
        return ResponseEntity.created(URI.create("/wallets/" + wallet.getWalletId().toString())).build();
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable("walletId") UUID walletId) {
        var deleted = walletService.deleteWallet(walletId);
        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/{walletId}/deposits")
    public ResponseEntity<Void> depositMoney(@PathVariable("walletId") UUID walletId,
                                             @RequestBody @Valid  DepositMoneyDto dto,
                                             HttpServletRequest request) {
        walletService.depositMoney(
                walletId,
                dto,
                request.getParameter("x-user-ip"));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{walletId}/statements")
    public ResponseEntity<StatementDto> getStatements(@PathVariable("walletId") UUID walletId,
                                                      @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var statement = walletService.getStatements(walletId, page, pageSize);
        return ResponseEntity.ok(statement);
    }
}
