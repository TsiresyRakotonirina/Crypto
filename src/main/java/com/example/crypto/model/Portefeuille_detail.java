package com.example.crypto.model;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;

import java.sql.Connection;
import java.util.Vector;

public class Portefeuille_detail {
    @Column
    String id_crypto;
    @Column
    String nom;
    @Column
    String id_porte;
    @Column
    int nb;
    @Column
    double valeur_actuelle;
    @Column
    double montant_total;
    public Portefeuille_detail() {}

    public String getId_crypto() {
        return id_crypto;
    }

    public void setId_crypto(String id_crypto) {
        this.id_crypto = id_crypto;
    }

    public String getId_porte() {
        return id_porte;
    }

    public void setId_porte(String id_porte) {
        this.id_porte = id_porte;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }

    public double getValeur_actuelle() {
        return valeur_actuelle;
    }

    public void setValeur_actuelle(double valeur_actuelle) {
        this.valeur_actuelle = valeur_actuelle;
    }

    public double getMontant_total() {
        return montant_total;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMontant_total(double montant_total) {
        this.montant_total = montant_total;
    }
    public Vector<Portefeuille_detail> all(Connection con,String id_porte) throws Exception{
        return Generalisation.select(con," where id_porte='"+id_porte+"'", Portefeuille_detail.class);
    }
}
