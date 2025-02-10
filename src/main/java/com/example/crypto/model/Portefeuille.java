package com.example.crypto.model;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Vector;

public class Portefeuille {
    @Column
    String id_porte;
    @Column
    String id_utilisateur;

    public String getId_porte() {
        return id_porte;
    }

    public void setId_porte(String id_porte) {
        this.id_porte = id_porte;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public Portefeuille() {
    }

    public Portefeuille(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public Vector<Portefeuille> find(Connection con, String id_utilisateur) throws Exception{
        return Generalisation.select(con,"where id_utilisateur='"+ id_utilisateur +"'", Portefeuille.class);
    }
    public String insert(Connection con) throws Exception{
        this.setId_porte(Generalisation.generatePrimaryKey(con,"POR","seq_"+this.getClass().getSimpleName()));
        Generalisation.insert(con,this);
        return this.getId_porte();
    }
    public void achat(String idCrypto, int quantite, String idUtilisateur) throws Exception {
        // Récupération des informations nécessaires
        Vector<Crypto> cryptoList = new Crypto().findid(null, idCrypto);
        Vector<Fond> fondList = new Fond().find(null, idUtilisateur);

        // Vérification que la crypto et les fonds existent
        if (!cryptoList.isEmpty() && !fondList.isEmpty()) {
            Crypto crypto = cryptoList.get(0);
            Fond fond = fondList.get(0);

            double prixTotal = crypto.getValeur_actuelle() * quantite;

            // Vérification des fonds disponibles
            if (fond.getFond() < prixTotal) {
                throw new Exception("Solde insuffisant");
            }

            double commission= (prixTotal * new Commission().find(null,"achat").get(0).getPourcentage())/100;
            Fond fondadmin= new Fond().find(null,"UTI000").get(0);
            fondadmin.setFond(fondadmin.getFond()+commission);
            fondadmin.update(null,fondadmin,"UTI000");
            // Débit du compte utilisateur
            prixTotal += commission;
            fond.setFond(fond.getFond() - prixTotal);
            fond.update(null, fond, idUtilisateur);

            // Récupération ou création de l'entrée dans le portefeuille_crypto
            Vector<Portefeuille_crypto> portefeuilleCryptoList = new Portefeuille_crypto().find(null, idCrypto, getId_porte());

            if (portefeuilleCryptoList.isEmpty()) {
                // Ajout de la crypto au portefeuille
                Portefeuille_crypto nouveauPortefeuilleCrypto = new Portefeuille_crypto(getId_porte(), idCrypto, quantite);
                nouveauPortefeuilleCrypto.insert(null);
            } else {
                // Mise à jour de la quantité existante
                Portefeuille_crypto portefeuilleCrypto = portefeuilleCryptoList.get(0);
                portefeuilleCrypto.setNb(portefeuilleCrypto.getNb() + quantite);
                portefeuilleCrypto.update(null, portefeuilleCrypto, getId_porte(), idCrypto);
            }
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            Action a= new Action(idUtilisateur,"achat",prixTotal,timestamp,idCrypto,quantite,commission);
            a.insert(null);
        }
    }
    public void vente(String idCrypto, int quantite, String idPorte, String idUtilisateur) throws Exception {
        // Récupération des informations nécessaires
        Vector<Portefeuille_crypto> portefeuilleList = new Portefeuille_crypto().find(null, idCrypto, idPorte);
        Vector<Crypto> cryptoList = new Crypto().findid(null, idCrypto);
        Vector<Fond> fondList = new Fond().find(null, idUtilisateur);
        Vector<Commission> commissionList = new Commission().find(null, "vente");

        // Vérification que les données existent
        if (portefeuilleList.isEmpty()) {
            throw new Exception("Aucune crypto trouvée dans le portefeuille.");
        }
        if (cryptoList.isEmpty()) {
            throw new Exception("Crypto non trouvée.");
        }
        if (fondList.isEmpty()) {
            throw new Exception("Fonds utilisateur introuvables.");
        }
        if (commissionList.isEmpty()) {
            throw new Exception("Commission introuvable.");
        }

        Portefeuille_crypto portefeuilleCrypto = portefeuilleList.get(0);
        Crypto crypto = cryptoList.get(0);
        Fond fondUtilisateur = fondList.get(0);
        Commission commission = commissionList.get(0);

        // Vérification de la quantité disponible
        if (portefeuilleCrypto.getNb() < quantite) {
            throw new Exception("Quantité insuffisante dans le portefeuille.");
        }

        // Calcul du prix total et de la commission
        double prixTotal = crypto.getValeur_actuelle() * quantite;
        double montantCommission = (prixTotal * commission.getPourcentage()) / 100;
        double montantFinal = prixTotal + montantCommission;

        // Mise à jour du portefeuille : réduction ou suppression
        int nouvelleQuantite = portefeuilleCrypto.getNb() - quantite;
        if (nouvelleQuantite > 0) {
            portefeuilleCrypto.setNb(nouvelleQuantite);
            portefeuilleCrypto.update(null, portefeuilleCrypto, idPorte, idCrypto);
        } else {
            portefeuilleCrypto.delete(null, idCrypto, idPorte);
        }

        // Mise à jour des fonds de l'utilisateur
        fondUtilisateur.setFond(fondUtilisateur.getFond() + montantFinal);
        fondUtilisateur.update(null, fondUtilisateur, idUtilisateur);

        // Enregistrement de l'action
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        Action action = new Action(idUtilisateur, "vente", montantFinal, timestamp, idCrypto, quantite,montantCommission);
        action.insert(null);
    }


}
