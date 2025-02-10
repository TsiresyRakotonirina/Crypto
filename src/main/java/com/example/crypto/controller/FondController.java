package com.example.crypto.controller;

import com.example.crypto.Service.EmailService;
import com.example.crypto.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Vector;

@Controller
public class FondController {
    private final EmailService emailService;
    public FondController(EmailService emailService) {
        this.emailService = emailService;
    }
    @RequestMapping("/actionhisto")
    public String action(Model model, @RequestParam int nb, @RequestParam String idhisto, HttpServletRequest request){
        try {
            new Historique_fond().action(nb,idhisto);
            return "redirect:/listehisto";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
    @RequestMapping("/listehisto")
    public String hoe(Model model,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize,HttpServletRequest request){
        try {
            int offset = page * pageSize;
            model.addAttribute("histos", new Historique_fond().findAllWithPagination(null,pageSize,offset));
            int total = new Historique_fond().findnonvalider(null).size();
            int totalPages = (int) Math.ceil((double) total / pageSize);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", pageSize);
            return "admin/listehistofond";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
    @RequestMapping("/insertfond")
    public String inserthisto(Model model, @RequestParam double montant, @RequestParam String motif, @RequestParam int type, HttpServletRequest request){
        try {
            HttpSession session = request.getSession();
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            String id=(String) session.getAttribute("utilisateur");
            Vector<Utilisateur> uts= new Utilisateur().findeid(null,id);

            Historique_fond hf=new Historique_fond(id,type,montant, timestamp,motif);
            String token=hf.insert(null);
            // Envoyer un e-mail de confirmation avec le token
            emailService.sendConfirmationEmail(uts.get(0).getEmail(), token, type, montant);

            return "redirect:/listecrypto";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
    @GetMapping("/confirm")
    public String confirmInsertFond(@RequestParam String token, Model model) {

        try {
            Vector<Historique_fond> historiqueFond = new Historique_fond().find(null,token);
            if (!historiqueFond.isEmpty()) {
                Historique_fond hf = historiqueFond.get(0);
                hf.setEtat(-1);
                // Insérer définitivement le fond
                hf.update(null,hf,token);// Déjà sauvegardé, juste une confirmation
                model.addAttribute("message", "Votre transaction a été validée avec succès.");


                return "redirect:/listecrypto";
            } else {
                model.addAttribute("error", "Erreur.");
                return "error";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/fond")
    @ResponseBody
    public double fond(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String id = (String) session.getAttribute("utilisateur");
            Vector<Fond> f=new Fond().find(null, id);
            double solde=0;

            if(f.isEmpty()){
                session.setAttribute("fond", solde); // Stocke en session
                return solde;
            }
            solde= f.get(0).getFond();
            System.out.println("pppppppppppp " +Math.round(solde * 100.0) / 100.0);
            return Math.round(solde * 100.0) / 100.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

}
