ALTER TABLE tb_wallets ADD COLUMN version BIGINT;
UPDATE tb_wallets SET version = 0;