package com.example.crypto.model;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Connexion;
import com.example.crypto.utils.Generalisation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Action {
    @Column
    String id_utilisateur;
    @Column
    String type;
    @Column
    double montant;
    @Column
    Timestamp date_heure;
    @Column
    String id_crypto;
    @Column
    int nombre;
    @Column
    double commission;

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getId_crypto() {
        return id_crypto;
    }

    public void setId_crypto(String id_crypto) {
        this.id_crypto = id_crypto;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public Action() {
    }

    public Action(String id_utilisateur, String type, double montant, Timestamp date_heure, String id_crypto, int nombre,double commission) {
        this.id_utilisateur = id_utilisateur;
        this.type = type;
        this.montant = montant;
        this.date_heure = date_heure;
        this.id_crypto = id_crypto;
        this.nombre = nombre;
        this.commission=commission;
    }
    public String insert(Connection con) throws Exception{
        Generalisation.insert(con,this);
        return this.getId_utilisateur();
    }
        // Méthode pour récupérer les actions filtrées
        public Vector<Action> findByCryptoAndDate(String idCrypto, Timestamp dateMin, Timestamp dateMax) throws Exception {
            Vector<Action> actions = new Vector<>();
            Connexion c=new Connexion();
            Connection con=c.connect();
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                // Connexion à la base de données

                // Requête SQL de base
                String query = "SELECT a.*,c.nom FROM action a JOIN crypto c on a.id_crypto=c.id_crypto WHERE date_heure BETWEEN ? AND ?";

                // Si une crypto spécifique est sélectionnée, on ajoute une condition
                if (!"all".equalsIgnoreCase(idCrypto)) {
                    query += " AND a.id_crypto = ?";
                }

                stmt = con.prepareStatement(query);
                stmt.setTimestamp(1, dateMin);
                stmt.setTimestamp(2, dateMax);

                // Ajouter idCrypto si nécessaire
                if (!"all".equalsIgnoreCase(idCrypto)) {
                    stmt.setString(3, idCrypto);
                }
                System.out.println("ito ndray : "+query);

                rs = stmt.executeQuery();

                // Récupération des résultats
                while (rs.next()) {
                    Action action = new Action(rs.getString("id_utilisateur"), rs.getString("type"), rs.getDouble("montant"), rs.getTimestamp("date_heure"), rs.getString("nom"),rs.getInt("nombre"),rs.getDouble("commission"));
                    actions.add(action);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return actions;
        }
    public Map<String, Double[]> calculerCommissionParType(String typeAnalyse, String idCrypto, String dateMin, String dateMax) throws Exception {
        Map<String, Double[]> resultats = new HashMap<>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Timestamp timestampMin = new Timestamp(dateFormat.parse(dateMin).getTime());
            Timestamp timestampMax = new Timestamp(dateFormat.parse(dateMax).getTime());

            Vector<Action> actions = new Action().findByCryptoAndDate(idCrypto, timestampMin, timestampMax);

            for (Action action : actions) {
                String crypto = action.getId_crypto();
                Double[] valeurs = resultats.getOrDefault(crypto, new Double[]{0.0, 0.0});

                if ("vente".equalsIgnoreCase(action.getType())) {
                    valeurs[0] += action.getCommission();
                } else if ("achat".equalsIgnoreCase(action.getType())) {
                    valeurs[1] += action.getCommission();
                }

                resultats.put(crypto, valeurs);
            }

            if ("moyenne".equalsIgnoreCase(typeAnalyse)) {
                for (String crypto : resultats.keySet()) {
                    Double[] valeurs = resultats.get(crypto);
                    int count = actions.size();
                    valeurs[0] = Math.round((valeurs[0] / count) * 100.0) / 100.0; // Moyenne vente, formatée
                    valeurs[1] = Math.round((valeurs[1] / count) * 100.0) / 100.0; // Moyenne achat, formatée

                    resultats.put(crypto, valeurs);
                }
            } else {
                // Formater les commissions totales pour la vente et l'achat
                for (String crypto : resultats.keySet()) {
                    Double[] valeurs = resultats.get(crypto);
                    valeurs[0] = Math.round(valeurs[0] * 100.0) / 100.0; // Total vente, formatée
                    valeurs[1] = Math.round(valeurs[1] * 100.0) / 100.0; // Total achat, formatée
                    resultats.put(crypto, valeurs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erreur lors de l'analyse des commissions.");
        }

        return resultats;
    }

}
