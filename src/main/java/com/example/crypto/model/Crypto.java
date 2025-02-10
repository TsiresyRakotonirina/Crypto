package com.example.crypto.model;

import java.sql.Connection;
import java.util.Vector;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;

public class Crypto {
    @Column
    String id_crypto;
    @Column
    String nom;
    @Column
    double valeur_actuelle;

    public String getId_crypto() {
        return id_crypto;
    }

    public void setId_crypto(String id_crypto) {
        this.id_crypto = id_crypto;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getValeur_actuelle() {
        return valeur_actuelle;
    }

    public void setValeur_actuelle(double valeur_actuelle) {
        this.valeur_actuelle = valeur_actuelle;
    }

    public Crypto() {
    }
    public Vector<Crypto> all(Connection con) throws Exception{
        return Generalisation.select(con," ", Crypto.class);
    }
    public Vector<Crypto> findcry(Connection con,String nom) throws Exception{
        return Generalisation.select(con,"where nom='"+ nom +"'", Crypto.class);
    }
    public Vector<Crypto> findid(Connection con,String id) throws Exception{
        return Generalisation.select(con,"where id_crypto='"+ id +"'", Crypto.class);
    }
    public String insert(Connection con) throws Exception{
        this.setId_crypto(Generalisation.generatePrimaryKey(con,"CRY","seq_"+this.getClass().getSimpleName()));
        Generalisation.insert(con,this);
        return this.getId_crypto();
    }
    public String insert(Connection con, String nom) throws Exception{
        Vector<Crypto> ee=findcry(con,nom);
        if(findcry(null,nom).isEmpty()) {
            return this.insert(con);
        }else{
            return ee.get(0).getId_crypto();
        }
    }
    public void update(Connection con, Object obj,String id_crypto)throws Exception{
        Generalisation.update(con, obj, " id_crypto='"+id_crypto+"'");
    }

}
