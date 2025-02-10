package com.example.crypto.model;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;

import java.sql.Connection;
import java.util.Vector;

public class Fond {
    @Column
    String id_utilisateur;
    @Column
    double fond;

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public double getFond() {
        return fond;
    }
    public String insert(Connection con) throws Exception{
        Generalisation.insert(con,this);
        return this.getId_utilisateur();
    }
    public void setFond(double fond) {
        this.fond = fond;
    }
    public Fond(){}

    public Fond(String id_utilisateur, double fond) {
        this.id_utilisateur = id_utilisateur;
        this.fond = fond;
    }

    public Vector<Fond> find(Connection con, String id_utilisateur) throws Exception{
        return Generalisation.select(con,"where id_utilisateur='"+ id_utilisateur +"'", Fond.class);
    }
    public void update(Connection con, Object obj,String id_utilisateur)throws Exception{
        Generalisation.update(con, obj, " id_utilisateur='"+id_utilisateur+"'");
    }


}
