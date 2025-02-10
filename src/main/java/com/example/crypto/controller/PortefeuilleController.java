package com.example.crypto.controller;

import com.example.crypto.model.Commission;
import com.example.crypto.model.Historique_fond;
import com.example.crypto.model.Portefeuille;
import com.example.crypto.model.Portefeuille_detail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Vector;

@Controller
public class PortefeuilleController {
    @PostMapping("/achat")
    public String achat(Model model, @RequestParam int nb, @RequestParam String idcrypto, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String id = (String) session.getAttribute("utilisateur");

            if (id == null) {
                model.addAttribute("error", "Utilisateur non authentifié.");
                return "error";
            }

            Vector<Portefeuille> portefeuilles = new Portefeuille().find(null, id);
            if (portefeuilles.isEmpty()) {
                model.addAttribute("error", "Aucun portefeuille trouvé pour cet utilisateur.");
                return "error";
            }

            Portefeuille portefeuille = portefeuilles.get(0);
            portefeuille.achat(idcrypto, nb, id);

            return "redirect:/portefeuille";
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/vente")
    public String vente(Model model, @RequestParam int nb, @RequestParam String idcrypto, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String id = (String) session.getAttribute("utilisateur");

            if (id == null) {
                model.addAttribute("error", "Utilisateur non authentifié.");
                return "error";
            }

            Vector<Portefeuille> portefeuilles = new Portefeuille().find(null, id);
            if (portefeuilles.isEmpty()) {
                model.addAttribute("error", "Aucun portefeuille trouvé pour cet utilisateur.");
                return "error";
            }

            Portefeuille portefeuille = portefeuilles.get(0);
            portefeuille.vente(idcrypto, nb, portefeuille.getId_porte(), id);

            return "redirect:/portefeuille";
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/portefeuille")
    public String action(Model model, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String idUtilisateur = (String) session.getAttribute("utilisateur");

            // Récupération du portefeuille de l'utilisateur
            Vector<Portefeuille> portefeuilles = new Portefeuille().find(null, idUtilisateur);
            String idPorte;

            if (portefeuilles.isEmpty()) {
                // Si aucun portefeuille, en créer un
                idPorte = new Portefeuille(idUtilisateur).insert(null);
            } else {
                // Sinon, récupérer l'ID du portefeuille existant
                idPorte = portefeuilles.get(0).getId_porte();
            }

            // Récupérer les cryptos associées au portefeuille
            Vector<Portefeuille_detail> listeCrypto = new Portefeuille_detail().all(null, idPorte);
            model.addAttribute("cryptos", listeCrypto);
            Vector<Commission> commissions = new Commission().find(null, "vente");
            model.addAttribute("pourcentage", commissions.get(0).getPourcentage());
            return "page/portefeuille";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

}
