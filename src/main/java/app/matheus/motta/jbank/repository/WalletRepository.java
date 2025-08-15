package app.matheus.motta.jbank.repository;

import app.matheus.motta.jbank.entities.Wallet;
import app.matheus.motta.jbank.repository.dto.StatementView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    String SQL_STATEMENT = """
                 SELECT
                        transfer_id         AS statement_id,
                        'transfer'          AS type,
                        transfer_value      AS statement_value,
                        wallet_receiver_id  AS wallet_receiver,
                        wallet_sender_id    AS wallet_sender,
                        transfer_date_time  AS statement_date_time
                    FROM
                        tb_transfers
                    WHERE wallet_receiver_id = ?1 OR wallet_sender_id = ?1
                    UNION ALL
                    SELECT
                        deposit_id          AS statement_id,
                        'deposit'           AS type,
                        deposit_value       AS statement_value,
                        wallet_id           AS wallet_receiver,
                        NULL                AS wallet_sender,
                        deposit_date_time   AS statement_date_time
                    FROM
                        tb_deposits
                    WHERE wallet_id = ?1
            """;

    String SQL_COUNT_STATEMENT = """
                SELECT COUNT(*) 
                FROM 
                (
                 """ + SQL_STATEMENT + """
                )
                AS total;
            """;


    Optional<Wallet> findByCpfOrEmail(String cpf, String email);

    @Query(nativeQuery = true, value = SQL_STATEMENT, countQuery = SQL_COUNT_STATEMENT)
    Page<StatementView> findStatements(UUID walletId, PageRequest page);
}
