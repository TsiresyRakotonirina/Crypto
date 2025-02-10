INSERT INTO utilisateur (id_utilisateur, login, mdp, admin,email)
VALUES
    ('UTI000', 'A', '000', 0, 'admin@gmail.com'),
    ('UTI001', 'B', '000', 1,'tsiresyrakotonirina02@gmail.com'),
    ('UTI002', 'C', '000', 1, 'tsiresyrakotonirina02@gmail.com'),
    ('UTI003', 'D', '000', 1, 'tsiresyrakotonirina02@gmail.com');
    
    -- admin = 0


INSERT INTO crypto (id_crypto, nom, valeur_actuelle)
VALUES
    ('CRYO01', 'Bitcoin', 45000.00);
INSERT INTO crypto (id_crypto, nom, valeur_actuelle)
VALUES
    ('CRYO02', 'Bitcoin 1', 50000.00),
    ('CRYO03', 'Ethereum', 32000.00),
    ('CRYO04', 'Litecoin', 45000.50),
    ('CRYO05', 'Ripple', 28000.25),
    ('CRYO06', 'Cardano', 73000.15),
    ('CRYO07', 'Polkadot', 52000.00),
    ('CRYO08', 'Dogecoin', 21500.75),
    ('CRYO09', 'Solana', 88000.30),
    ('CRYO10', 'Chainlink', 66500.90);
INSERT INTO fond (id_utilisateur,fond)
VALUES
    ('UTI000', 0.0);
INSERT INTO valeur_crypto (id_crypto, valeur, date_heure)
VALUES
    ('CRYO01', 45000.00, CURRENT_TIMESTAMP);  -- Valeur du Bitcoin à l'instant courant
INSERT INTO valeur_crypto (id_crypto, valeur, date_heure)
VALUES
    ('CRYO02', 50000.00, CURRENT_TIMESTAMP),
    ('CRYO03', 31500.00, '2024-01-01 10:00:00'),
    ('CRYO04', 45500.00, '2024-01-02 11:30:00'),
    ('CRYO05', 27500.00, '2024-01-03 12:15:00'),
    ('CRYO06', 74000.00, '2024-01-04 13:45:00'),
    ('CRYO07', 53000.00, '2024-01-05 14:20:00'),
    ('CRYO08', 22000.00, '2024-01-06 15:10:00'),
    ('CRYO09', 89000.00, '2024-01-07 16:05:00'),
    ('CRYO10', 67000.00, '2024-01-08 17:40:00');
INSERT INTO portefeuille (id_porte,id_utilisateur)
VALUES
    ('POR001', 'UTI001'),
    ('POR002', 'UTI002'),
    ('POR003', 'UTI003');
INSERT INTO commission (type,pourcentage)
VALUES
    ('vente', 10),
    ('achat', 10);



