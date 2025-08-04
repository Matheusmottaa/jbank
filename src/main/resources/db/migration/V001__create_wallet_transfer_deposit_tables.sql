CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE tb_wallets
(
  wallet_id UUID DEFAULT gen_random_uuid(),
  cpf       VARCHAR(11),
  email     VARCHAR(80),
  name      VARCHAR(100),
  balance   NUMERIC(19,4),
  CONSTRAINT pk_tb_wallets        PRIMARY KEY (wallet_id),
  CONSTRAINT uq_tb_wallets_cpf    UNIQUE (cpf),
  CONSTRAINT uq_tb_wallets_email  UNIQUE (email)
);

CREATE TABLE tb_transfers
(
	transfer_id UUID DEFAULT gen_random_uuid(),
	wallet_receiver_id UUID,
	wallet_sender_id UUID,
	transfer_value NUMERIC(19, 4),
	transfer_date_time TIMESTAMP,

	CONSTRAINT pk_tb_transfers_transfer_id PRIMARY KEY (transfer_id),

	CONSTRAINT fk_tb_transfers_wallet_sender_id FOREIGN KEY (wallet_receiver_id)
	REFERENCES tb_wallets (wallet_id)
	ON DELETE RESTRICT,

	CONSTRAINT fk_tb_transfers_wallet_receiver_id FOREIGN KEY (wallet_sender_id)
	REFERENCES tb_wallets (wallet_id)
	ON DELETE RESTRICT
);

CREATE TABLE tb_deposits
(
    deposit_id         UUID DEFAULT gen_random_uuid(),
    wallet_id          UUID,
    deposit_value      NUMERIC(19, 4),
    deposit_date_time  TIMESTAMP,
    ip_address         VARCHAR(15),

    CONSTRAINT pk_tb_deposits PRIMARY KEY (deposit_id),

    CONSTRAINT fk_tb_deposits_wallet_id__tb_wallets
        FOREIGN KEY (wallet_id) REFERENCES tb_wallets (wallet_id) ON DELETE RESTRICT,

    CONSTRAINT ck_tb_deposits_value_positive
        CHECK (deposit_value > 0)
);