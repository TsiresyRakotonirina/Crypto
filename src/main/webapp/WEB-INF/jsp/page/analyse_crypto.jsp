<%@ page import="java.util.Vector" %>
<%@ page import="com.example.crypto.model.Analyse_crypto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<body>
<jsp:include page="../template/menu1.jsp"/>

<main id="main" class="main">
    <section class="section">
        <div class="row">
            <%
                // Récupération du type d'analyse
                String typeAnalyse = (String) request.getAttribute("typeAnalyse");
            %>

            <h2 class="text-center">Résultats de l'Analyse - <%= typeAnalyse != null ? typeAnalyse.toUpperCase() : "Inconnu" %></h2>

            <table class="table">
                <thead>
                <tr>
                    <th>Crypto</th>
                    <th>Valeur</th>
                </tr>
                </thead>
                <tbody>
                <%
                    Vector<Analyse_crypto> resultats = (Vector<Analyse_crypto>) request.getAttribute("resultats");
                    if (resultats != null && !resultats.isEmpty()) {
                        for (Analyse_crypto analyse : resultats) {
                %>
                <tr>
                    <td><%= analyse.getNom() %></td>
                    <td><%= String.format("%.2f", analyse.getValeur()) %> Ar</td> <%-- Format à 2 décimales --%>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="2" class="text-center">Aucun résultat trouvé.</td>
                </tr>
                <% } %>
                </tbody>
            </table>

            <a href="/analyse" class="btn btn-secondary mt-3">Retour</a>
        </div>
    </section>
</main>

<jsp:include page="../template/footer.jsp"/>
</body>
</html>
