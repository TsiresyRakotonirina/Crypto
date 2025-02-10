package com.example.crypto.controller;

import com.example.crypto.model.*;
import com.example.crypto.utils.Connexion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import org.springframework.ui.Model;

@Controller
public class CryptoController {
    @GetMapping("/cryptos")
    @ResponseBody
    public List<Crypto> showCryptos() throws Exception {
        Vector<Crypto> licry = new Crypto().all(null);
        System.out.println("atooo");
        return licry; // Renvoie la liste des cryptos au format JSON
    }
    @RequestMapping("/listecrypto")
    public String home(Model model)  {
        try {
            Vector<Crypto> licry = new Crypto().all(null);
            model.addAttribute("cryptos", licry);
            Vector<Commission> commissions = new Commission().find(null, "achat");
            if (commissions.isEmpty()) {
                model.addAttribute("error", "Commission d'achat non définie.");
                return "error";
            }

            double pourcentage = commissions.get(0).getPourcentage();
            model.addAttribute("pourcentage", pourcentage);
            return "page/listecrypto";
        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error/error";
        }
    }
    @RequestMapping("/analyse")
    public String action(Model model,String idcrypto, HttpServletRequest request) {
        try {

            // Récupérer les cryptos associées au portefeuille
            Vector<Crypto> listeCrypto = new Crypto().all(null);
            model.addAttribute("cryptos", listeCrypto);

            return "page/analyse";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
    @RequestMapping("/analysecrypto")
    public String analyserCrypto(@RequestParam(name = "id_cryptos", required = false) String[] idCryptos,
                                 @RequestParam("type_analyse") String typeAnalyse,
                                 @RequestParam("date_heure_min") String dateHeureMin,
                                 @RequestParam("date_heure_max") String dateHeureMax,
                                 Model model) {
        try {
            // Vérifier si l'utilisateur a sélectionné des cryptos
            if (idCryptos == null || idCryptos.length == 0) {
                model.addAttribute("error", "Veuillez sélectionner au moins une cryptomonnaie.");
                return "page/analyse";
            }

            // Convertir les IDs en liste
            List<String> idsCryptoList = Arrays.asList(idCryptos);

            // Connexion à la base de données (à adapter selon ton projet)
            Connexion c=new Connexion();
            Connection con=c.connect();

            // Appel de la fonction d'analyse
            Analyse_crypto dao = new Analyse_crypto();
            Vector<Analyse_crypto> resultats = dao.findByCriteria(con, idsCryptoList, typeAnalyse, dateHeureMin, dateHeureMax);

            // Ajouter les résultats au modèle pour affichage
            model.addAttribute("resultats", resultats);
            model.addAttribute("cryptos", new Crypto().all(null));
            model.addAttribute("typeAnalyse", typeAnalyse);
            return "page/analyse_crypto"; // Une nouvelle page pour afficher les résultats
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
    @RequestMapping(value = "/evolution", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getGraphData(@RequestParam("id_crypto") String idCrypto) {
        try {
            // Fetch all data for the given cryptocurrency
            Vector<Analyse_crypto> analyse = new Analyse_crypto().find(null, idCrypto);

            if (analyse != null && !analyse.isEmpty()) {
                return ResponseEntity.ok(analyse);  // Return all historical data as a JSON array
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune donnée trouvée");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }


    @RequestMapping("/graph")
    public String getGraphData(@RequestParam("id_crypto") String idCrypto, Model model) {
        try {
            model.addAttribute("id_crypto", idCrypto);
            return "page/evolution";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


}
