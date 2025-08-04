package app.matheus.motta.jbank.repository;

import app.matheus.motta.jbank.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByCpfOrEmail(String cpf, String email);
}
