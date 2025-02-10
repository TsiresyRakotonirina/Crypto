package com.example.crypto.controller;

import com.example.crypto.model.Commission;
import com.example.crypto.model.Crypto;
import com.example.crypto.model.Utilisateur;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UtilisateurController {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurController.class);

    @RequestMapping("/")
    public String home() {
        logger.info("La méthode home a été appelée");
        return "auth/login";
    }

    @RequestMapping("/admin")
    public String h(){
        return "auth/admin";
    }
    @RequestMapping("/login")
    public String hello(Model model, @RequestParam String login, @RequestParam String mdp, HttpServletRequest request) {
        Utilisateur m = new Utilisateur(login,mdp);
        try {
            Vector<Utilisateur> uts=m.find(null);
            if(!uts.isEmpty()) {
                for(Utilisateur ut : uts) {
                    if(ut.getAdmin()!=0) {
                        HttpSession session = request.getSession();
                        session.setAttribute("utilisateur", ut.getId_utilisateur());

                        return "redirect:/listecrypto";
                    }
                }
            }
            model.addAttribute("error", "verifiez les informations");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error/error";
        }
    }
    @RequestMapping("/logadmin")
    public String helloadmin(Model model, @RequestParam String login, @RequestParam String mdp, HttpServletRequest request) {
        Utilisateur m = new Utilisateur(login,mdp);
        try {
            Vector<Utilisateur> uts=m.find(null);
            if(!uts.isEmpty()) {
                for(Utilisateur ut : uts) {
                    if(ut.getAdmin()==0) {
                        HttpSession session = request.getSession();
                        session.setAttribute("utilisateur", ut.getId_utilisateur());
                        return "redirect:/listehisto";
                    }

                }
            }
            model.addAttribute("error", "verifiez les informations");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error/error";
        }
    }
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Récupérer la session
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Invalider la session
            session.invalidate();
        }
        // Redirection vers la page d'accueil ou une autre page après la déconnexion
        return "redirect:/";
    }

}
