package app.matheus.motta.jbank.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_deposits", schema = "public")
@NoArgsConstructor @AllArgsConstructor
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "deposit_id")
    private UUID depositId;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "deposit_value")
    private BigDecimal depositValue;

    @Column(name = "deposit_date_time")
    private LocalDateTime depositDateTime;

    @Column(name = "ip_address")
    private String ipAddress;
}
