<%@ page import="java.util.List" %>
<%@ page import="com.example.crypto.model.Analyse_crypto" %>
<%@ page import="com.example.crypto.model.Crypto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<body>

<jsp:include page="../template/menu1.jsp"/>

<main id="main" class="main">
    <section class="section">
        <div class="row">

                <h2 class="text-center">Analyse :</h2>

                <form action="/analysecrypto" method="post">
                    <!-- Cryptomonnaies en checkbox -->
                    <label class="form-label">Sélectionnez les cryptomonnaies :</label>
                    <%
                        List<Crypto> cryptos = (List<Crypto>) request.getAttribute("cryptos");
                        for (Crypto crypto : cryptos) {
                    %>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="id_cryptos" value="<%= crypto.getId_crypto() %>">
                        <label class="form-check-label"><%= crypto.getNom() %></label>
                    </div>
                    <% } %>

                    <!-- Type d'analyse -->
                    <label class="form-label mt-3">Type d'analyse :</label>
                    <select class="form-select" name="type_analyse" required>
                        <option selected disabled>Choisissez une analyse</option>
                        <option value="1er_quartile">1er quartile</option>
                        <option value="max">Max</option>
                        <option value="min">Min</option>
                        <option value="moyenne">Moyenne</option>
                        <option value="ecart-type">Écart-type</option>
                    </select>

                    <!-- Date et heure min/max -->
                    <label class="form-label mt-3">Date et heure min :</label>
                    <input type="datetime-local" class="form-control" name="date_heure_min" required>

                    <label class="form-label mt-3">Date et heure max :</label>
                    <input type="datetime-local" class="form-control" name="date_heure_max" required>

                    <button type="submit" class="btn btn-primary mt-3">Voir</button>
                </form>
            </div>

    </section>
</main>

<jsp:include page="../template/footer.jsp"/>
</body>
</html>
