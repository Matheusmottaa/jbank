package app.matheus.motta.jbank.controller;

import app.matheus.motta.jbank.controller.dto.TransferMoneyDto;
import app.matheus.motta.jbank.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(final TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(final @RequestBody @Valid TransferMoneyDto dto) {
        transferService.transferMoney(dto);
        return ResponseEntity.ok().build();
    }
}
