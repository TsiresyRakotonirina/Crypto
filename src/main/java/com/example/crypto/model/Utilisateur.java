package com.example.crypto.model;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;


import java.sql.Connection;
import java.util.Vector;

public class Utilisateur {
    @Column
    String id_utilisateur;
    @Column
    String email;
    @Column
    String login;
    @Column
    String mdp;
    @Column
    int admin;

    public Utilisateur(String login, String mdp) {
        setLogin(login);
        setMdp(mdp);
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public Utilisateur() {
    }

    public Utilisateur( String email,String login, String mdp, int admin) {
        this.email=email;
        this.login = login;
        this.mdp = mdp;
        this.admin = admin;
    }
    public String insert(Connection con) throws Exception{
        this.setId_utilisateur(Generalisation.generatePrimaryKey(con,"UTI","seq_"+this.getClass().getSimpleName()));
        Generalisation.insert(con,this);
        return this.getId_utilisateur();
    }
    public String insert(Connection con, String nom) throws Exception{
        Vector<Utilisateur> ee=findequ(con,nom);
        if(findequ(null,nom).isEmpty()) {
            return this.insert(con);
        }else{
            return ee.get(0).getId_utilisateur();
        }
    }
    public Vector<Utilisateur> find(Connection con) throws Exception{
        return Generalisation.select(con,"where login='"+ this.getLogin() +"' and mdp='"+ this.getMdp()+ "'", Utilisateur.class);
    }

    public Vector<Utilisateur> findeid(Connection con,String id) throws Exception{
        return Generalisation.select(con,"where id_utilisateur='"+ id +"'", Utilisateur.class);
    }
    public Vector<Utilisateur> findequ(Connection con,String nom) throws Exception{
        return Generalisation.select(con,"where email='"+ nom +"'", Utilisateur.class);
    }
    public Vector<Utilisateur> all(Connection con) throws Exception{
        return Generalisation.select(con,"where admin=1", Utilisateur.class);
    }
}
