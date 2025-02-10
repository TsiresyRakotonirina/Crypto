<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Résultats de l'Analyse</title>
</head>
<body>

<jsp:include page="../template/menu.jsp"/>

<main id="main" class="main">
    <section class="section">
        <div class="container">
            <h2>Résultats de l'Analyse</h2>

            <table class="table datatable">
                <thead>
                <tr>
                    <th>Cryptomonnaie</th>
                    <th>Commission Vente</th>
                    <th>Commission Achat</th>
                </tr>
                </thead>
                <tbody>
                <%
                    Map<String, Double[]> resultats = (Map<String, Double[]>) request.getAttribute("resultats");
                    if (resultats != null && !resultats.isEmpty()) {
                        for (Map.Entry<String, Double[]> entry : resultats.entrySet()) {
                %>
                <tr>
                    <td><%= entry.getKey() %></td>
                    <td><%= String.format("%.2f", entry.getValue()[0]) %> Ar</td>
                    <td><%= String.format("%.2f", entry.getValue()[1]) %> Ar</td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr><td colspan="3">Aucune donnée disponible.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>
</main>

<jsp:include page="../template/footer.jsp"/>

</body>
</html>
