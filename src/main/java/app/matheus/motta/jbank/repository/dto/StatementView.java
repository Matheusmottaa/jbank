package app.matheus.motta.jbank.repository.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StatementView {
    String getStatementId();
    String getType();
    BigDecimal getStatementValue();
    String getWalletReceiver();
    String getWalletSender();
    LocalDateTime getSatementDateTime();
}
