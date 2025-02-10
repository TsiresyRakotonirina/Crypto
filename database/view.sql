CREATE VIEW portefeuille_detail AS
SELECT
    pc.id_crypto,
    c.nom,
    pc.id_porte,
    pc.nb,
    c.valeur_actuelle,
    (pc.nb * c.valeur_actuelle) AS montant_total
FROM portefeuille_crypto pc
         JOIN crypto c ON pc.id_crypto = c.id_crypto;

CREATE VIEW analyse_crypto AS
SELECT
    v.id_crypto,
    c.nom,
    v.valeur,
    v.date_heure
FROM valeur_crypto v
         JOIN crypto c ON v.id_crypto = c.id_crypto;
