package com.example.crypto.controller;

import com.example.crypto.model.Action;
import com.example.crypto.model.Commission;
import com.example.crypto.model.Crypto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Vector;

@Controller
public class CommissionController {
    @RequestMapping("/paramcom")
    public String param(Model model) {
        try {
            Vector<Commission> li = new Commission().all(null);
            model.addAttribute("commissions", li);
            return "admin/paramcomm";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
    @RequestMapping("/modificomm")
    public String modif(Model model, @RequestParam String type, @RequestParam double pourcentage) {
        try {
            Commission commission = new Commission(type, pourcentage);
            commission.update(null,commission,type);
            return "redirect:/paramcom";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/analysecommission")
    public String afficherAnalyse(Model model) {
        try {
            Vector<Crypto> listeCrypto = new Crypto().all(null);
            model.addAttribute("cryptos", listeCrypto);
            return "admin/analysecommission";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/analysecom")
    public String analyserCommission(
            @RequestParam("typeAnalyse") String typeAnalyse,
            @RequestParam("idCrypto") String idCrypto,
            @RequestParam("dateMin") String dateMin,
            @RequestParam("dateMax") String dateMax,
            Model model) {

        try {

            Map<String, Double[]> resultats = new Action().calculerCommissionParType(typeAnalyse, idCrypto, dateMin, dateMax);

            model.addAttribute("resultats", resultats);
            return "admin/resultatcom";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
}
