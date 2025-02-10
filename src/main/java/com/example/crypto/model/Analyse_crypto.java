package com.example.crypto.model;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

public class Analyse_crypto {
    @Column
    String id_crypto;
    @Column
    String nom;
    @Column
    double valeur;
    @Column
    Timestamp date_heure;

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

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public Timestamp getDate_heure() {
        return date_heure;
    }

    public void setDate_heure(Timestamp date_heure) {
        this.date_heure = date_heure;
    }

    public Analyse_crypto(double valeur, String id_crypto, String nom, Timestamp date_heure) {
        this.valeur = valeur;
        this.id_crypto = id_crypto;
        this.nom = nom;
        this.date_heure = date_heure;
    }

    public Analyse_crypto() {
    }

    public Vector<Analyse_crypto> find(Connection con, String id_crypto) throws Exception{
        return Generalisation.select(con,"where id_crypto='"+ id_crypto +"' order by date_heure asc", Analyse_crypto.class);
    }
    public Vector<Analyse_crypto> findByCriteria(Connection con, List<String> idsCrypto, String typeAnalyse, String dateMin, String dateMax) throws Exception {
        Vector<Analyse_crypto> analyses = new Vector<>();

        if (idsCrypto == null || idsCrypto.isEmpty() || typeAnalyse == null || dateMin == null || dateMax == null) {
            throw new IllegalArgumentException("Les paramètres fournis sont invalides.");
        }

        String query = generateQuery(typeAnalyse, idsCrypto.size());

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            int index = 1;
            for (String id : idsCrypto) {
                pstmt.setString(index++, id);
            }
            pstmt.setString(index++, dateMin);
            pstmt.setString(index++, dateMax);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Analyse_crypto analyse = new Analyse_crypto();
                    analyse.setId_crypto(rs.getString("id_crypto"));
                    analyse.setNom(rs.getString("nom"));
                    analyse.setValeur(rs.getDouble("valeur"));
                    analyses.add(analyse);
                }
            }
        }
        return analyses;
    }

    private String generateQuery(String typeAnalyse, int nbIds) {
        StringBuilder query = new StringBuilder("SELECT id_crypto,nom,");

        switch (typeAnalyse) {
            case "1er_quartile":
                query.append("PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY valeur) AS valeur ");
                break;
            case "max":
                query.append("MAX(valeur) AS valeur ");
                break;
            case "min":
                query.append("MIN(valeur) AS valeur ");
                break;
            case "moyenne":
                query.append("AVG(valeur) AS valeur ");
                break;
            case "ecart-type":
                query.append("STDDEV(valeur) AS valeur ");
                break;
            default:
                throw new IllegalArgumentException("Type d'analyse non reconnu.");
        }

        query.append(" FROM Analyse_crypto WHERE id_crypto IN (");
        query.append("?, ".repeat(nbIds));
        query.setLength(query.length() - 2); // Remove the last comma

        // Explicit type casting for date_heure
        query.append(") AND date_heure BETWEEN CAST(? AS timestamp) AND CAST(? AS timestamp) GROUP BY id_crypto,nom");

        return query.toString();
    }

}
