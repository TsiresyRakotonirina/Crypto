package com.example.crypto.Service;

import com.example.crypto.model.Crypto;
import com.example.crypto.model.Valeur_crypto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Vector;

@Service
public class CryptoService {
    private final Random random = new Random();

//    @Scheduled(initialDelay = 10000, fixedRate = 10000) // Exécution toutes les 10 secondes
    public void genererValeurCrypto() throws Exception {
        Crypto crypto = new Crypto();
        Vector<Crypto> li = crypto.all(null);

        for (Crypto c : li) {
            // Générer une variation aléatoire entre -5% et +5%
            double variation = (random.nextDouble() * 10 - 5) / 100; // Entre -5% et +5%
            double dernierPrix = c.getValeur_actuelle();

            // Appliquer la variation sur le dernier prix
            dernierPrix += dernierPrix * variation;

            // S'assurer que le prix ne soit jamais négatif ni nul
            if (dernierPrix <= 0) {
                dernierPrix = 1; // Valeur minimale arbitraire
            }

            c.setValeur_actuelle(dernierPrix);
            c.update(null, c, c.getId_crypto());
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            // Créer une nouvelle entrée
            Valeur_crypto valeurCrypto = new Valeur_crypto(c.getId_crypto(), dernierPrix,timestamp);
            valeurCrypto.insert(null);

            System.out.printf("Nouvelle valeur Crypto insérée : %.2f ar%n", dernierPrix);
        }
    }

}
