package com.example.crypto.model;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Vector;

public class Historique_fond {
    @Column
    String id_histofond;
    @Column
    String id_utilisateur;
    @Column
    int types;
    @Column
    double montant;
    @Column
    Timestamp date_heure;
    @Column
    String motif;
    @Column
    int etat;

    public Historique_fond() {}

    public String getId_histofond() {
        return id_histofond;
    }

    public void setId_histofond(String id_histofond) {
        this.id_histofond = id_histofond;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int type) {
        this.types = type;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Timestamp getDate_heure() {
        return date_heure;
    }

    public void setDate_heure(Timestamp date_heure) {
        this.date_heure = date_heure;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public Historique_fond(String id_utilisateur, int type, double montant, Timestamp date_heure, String motif) {
        this.id_utilisateur = id_utilisateur;
        this.types = type;
        this.montant = montant;
        this.date_heure = date_heure;
        this.motif = motif;
    }

    public String insert(Connection con) throws Exception{
        this.setEtat(2);
        this.setId_histofond(Generalisation.generatePrimaryKey(con,"HIS","seq_"+this.getClass().getSimpleName()));
        Generalisation.insert(con,this);
        return this.getId_histofond();
    }
    public Vector<Historique_fond> findAllWithPagination(Connection con, int limit, int offset) throws Exception{
        return Generalisation.select(con,"where etat= -1 limit "+limit+" offset "+offset, Historique_fond.class);
    }
    public Vector<Historique_fond> findnonvalider(Connection con) throws Exception{
        return Generalisation.select(con,"where etat= -1", Historique_fond.class);
    }
    public Vector<Historique_fond> find(Connection con,String id_histofond) throws Exception{
        return Generalisation.select(con,"where id_histofond= '"+id_histofond+"'", Historique_fond.class);
    }

    public void update(Connection con, Object obj,String id_histo)throws Exception{
        Generalisation.update(con, obj, " id_histofond='"+id_histo+"'");
    }

    public void action(int nb,String id_histofond) throws Exception{
        Vector<Historique_fond> fs=find(null,id_histofond);
        this.setId_histofond(id_histofond);
        this.setDate_heure(fs.get(0).getDate_heure());
        this.setMotif(fs.get(0).getMotif());
        this.setId_utilisateur(fs.get(0).getId_utilisateur());
        this.setTypes(fs.get(0).getTypes());
        this.setMontant(fs.get(0).getMontant());
        this.setEtat(nb);
        update(null,this,id_histofond);
        if(nb==1){
            Fond f=new Fond();
            Vector<Fond> fonds= f.find(null,this.getId_utilisateur());
            if(fonds.size()>0) {
                f = fonds.get(0);
                double argent = f.getFond();
                f.setId_utilisateur(this.getId_utilisateur());
                if (this.getTypes() == -1 && argent >= this.getMontant()) {
                    f.setFond(argent - this.getMontant());
                } else if (this.getTypes() == 1) {
                    f.setFond(argent + this.getMontant());
                } else {
                    throw new Exception("Solde insuffisant");
                }
                f.update(null, f, fs.get(0).getId_utilisateur());
            }
            else if (fonds.isEmpty() && this.getTypes() == 1){
                f.setId_utilisateur(fs.get(0).getId_utilisateur());
                f.setFond(this.getMontant());
                f.insert(null);
            }
            else {
                throw new Exception("Solde insuffisant");
            }
        }

    }

}
