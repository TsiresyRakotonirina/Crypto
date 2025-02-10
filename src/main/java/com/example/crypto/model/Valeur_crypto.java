package com.example.crypto.model;

import java.sql.Connection;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Vector;
import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;
public class Valeur_crypto {
    @Column
    String id_crypto;
    @Column
    double valeur;
    @Column
    Timestamp date_heure;

    public String getId_crypto() {
        return id_crypto;
    }

    public double getValeur () {
        return this.valeur;
    }

    public Timestamp getDate_heure() {
        return date_heure;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public void setDate_heure(Timestamp dateHeure) {
        this.date_heure = dateHeure;
    }

    public Valeur_crypto() {
    }

    public Valeur_crypto(String id_crypto, double valeur, Timestamp date_heure) {
        this.id_crypto = id_crypto;
        this.valeur = valeur;
        this.date_heure = date_heure;
    }
    public String insert(Connection con) throws Exception{
        Generalisation.insert(con,this);
        return this.getId_crypto();
    }
    public Vector<Valeur_crypto> find(Connection con) throws Exception{
        return Generalisation.select(con,"where id_crypto='"+ this.getId_crypto()+ "'", Valeur_crypto.class);
    }


}
