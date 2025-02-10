<%@ page import="java.util.Map" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.example.crypto.model.Crypto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Analyse des Commissions</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<jsp:include page="../template/menu.jsp"/>

<main id="main" class="main">
    <section class="section">
        <div class="container">
            <h2 class="text-center">Analyse Commissions</h2>

            <!-- Formulaire pour l'analyse -->
            <form action="/analysecom" method="POST" id="analyseForm">
                <div class="form-group">
                    <label for="typeAnalyse">Type d'Analyse :</label>
                    <select id="typeAnalyse" name="typeAnalyse">
                        <option value="somme">Somme</option>
                        <option value="moyenne">Moyenne</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="idCrypto">Cryptomonnaie :</label>
                    <select id="idCrypto" name="idCrypto">
                        <option value="all">Tous</option>
                        <% for (Crypto crypto : (Vector<Crypto>) request.getAttribute("cryptos")) { %>
                        <option value="<%= crypto.getId_crypto() %>"><%= crypto.getNom() %></option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="dateMin">Date et Heure Min :</label>
                    <input type="datetime-local" id="dateMin" name="dateMin">
                </div>

                <div class="form-group">
                    <label for="dateMax">Date et Heure Max :</label>
                    <input type="datetime-local" id="dateMax" name="dateMax">
                </div>

                <div class="form-group">
                    <button type="submit">Valider</button>
                </div>
            </form>
        </div>
    </section>
</main>

<jsp:include page="../template/footer.jsp"/>

</body>
</html>
