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
@Table(name = "tb_transfers", schema = "public")
@NoArgsConstructor @AllArgsConstructor
public class Transfer {

    @Id
    @Column(name = "transfer_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transferId;

    @ManyToOne
    @JoinColumn(name = "wallet_receiver_id")
    private Wallet receiver;

    @ManyToOne
    @JoinColumn(name = "wallet_sender_id")
    private Wallet sender;

    @Column(name = "transfer_value")
    private BigDecimal transferValue;

    @Column(name = "transfer_date_time")
    private LocalDateTime transferDateTime;
}
