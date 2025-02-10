package com.example.crypto.model;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;

import java.sql.Connection;
import java.util.Vector;

public class Portefeuille_crypto {
    @Column
    String id_porte;
    @Column
    String id_crypto;
    @Column
    int nb;

    public String getId_porte() {
        return id_porte;
    }

    public void setId_porte(String id_porte) {
        this.id_porte = id_porte;
    }

    public String getId_crypto() {
        return id_crypto;
    }

    public void setId_crypto(String id_crypto) {
        this.id_crypto = id_crypto;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }

    public Portefeuille_crypto() {
    }

    public Portefeuille_crypto(String id_porte, String id_crypto, int nb) {
        this.id_porte = id_porte;
        this.id_crypto = id_crypto;
        this.nb = nb;
    }
    public Vector<Portefeuille_crypto> select(Connection con, String id_porte) throws Exception{
        return Generalisation.select(con,"where id_porte='"+ id_porte +"'", Portefeuille_crypto.class);
    }
    public Vector<Portefeuille_crypto> find(Connection con, String id_crypto, String id_porte) throws Exception{
        return Generalisation.select(con,"where id_crypto='"+ id_crypto +"' and id_porte='"+id_porte+"'", Portefeuille_crypto.class);
    }
    public void update(Connection con, Object obj,String id_porte,String id_crypto)throws Exception{
        Generalisation.update(con, obj, " id_porte='"+id_porte+"' and id_crypto='"+id_crypto+"'");
    }
    public String insert(Connection con) throws Exception{
        Generalisation.insert(con,this);
        return this.getId_porte();
    }
    public void delete(Connection con, String id_crypto, String id_porte)throws Exception{
        Generalisation.delete(con, this.getClass().getSimpleName(), "id_crypto='"+id_crypto+"' and id_porte='"+id_porte+"'");
    }
}
