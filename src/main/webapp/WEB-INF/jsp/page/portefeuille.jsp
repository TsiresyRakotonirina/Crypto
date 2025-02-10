<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.example.crypto.model.Portefeuille_detail" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Vector<Portefeuille_detail> catcour = (Vector<Portefeuille_detail>) request.getAttribute("cryptos");
    double commissionPourcentage = (double) request.getAttribute("pourcentage"); // Récupère depuis la base en dynamique si nécessaire
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Cryptomonnaies</title>

    <!-- Bootstrap et jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

    <script>
        var commissionPourcentage = <%= commissionPourcentage %>; // Commission récupérée dynamiquement

        // Fonction pour ouvrir la modal de vente
        function ouvrirVente(nom, valeurUnitaire, quantiteMax, id) {
            $("#cryptoNom").val(nom);
            $("#cryptoId").val(id);
            $("#cryptoValeurUnitaire").val(valeurUnitaire);
            $("#cryptoQuantite").attr("max", quantiteMax).val(""); // Réinitialiser l'input
            $("#cryptoTotal").val("");

            $("#venteModal").modal("show"); // Ouvre la boîte de dialogue
        }

        // Fonction pour calculer le total après la commission
        function calculerTotal() {
            var valeurUnitaire = parseFloat($("#cryptoValeurUnitaire").val());
            var quantite = parseFloat($("#cryptoQuantite").val()) || 0;

            if (quantite > parseFloat($("#cryptoQuantite").attr("max"))) {
                alert("Quantité trop élevée !");
                $("#cryptoQuantite").val("");
                $("#cryptoTotal").val("");
                return;
            }

            var totalSansCommission = valeurUnitaire * quantite;
            let totalAvecCommission = totalSansCommission + (totalSansCommission * (commissionPourcentage / 100));

            $("#cryptoTotal").val(totalAvecCommission.toFixed(2));
        }

        // Fonction pour rafraîchir les données du tableau
        function rafraichirCryptos() {
            $.ajax({
                url: '/cryptos', // Remplacez cette URL par celle de votre service qui retourne les cryptos mises à jour
                method: 'GET',
                success: function(response) {
                    var updatedCryptos = JSON.parse(response); // Assurez-vous que le serveur renvoie une réponse JSON
                    var tableBody = $('#cryptoTableBody');
                    tableBody.empty(); // Vider le tableau actuel

                    // Remplir le tableau avec les nouvelles données
                    updatedCryptos.forEach(function(crypto) {
                        var row = `<tr>
                            <td>${crypto.nom}</td>
                            <td>${crypto.valeur_actuelle} ar</td>
                            <td>${crypto.nb}</td>
                            <td>${crypto.montant_total} ar</td>
                            <td>
                                <button class="btn btn-danger" onclick="ouvrirVente('${crypto.nom}', ${crypto.valeur_actuelle}, ${crypto.nb}, ${crypto.id})">
                                    Vendre
                                </button>
                            </td>
                        </tr>`;
                        tableBody.append(row);
                    });
                },
                error: function(error) {
                    console.error("Erreur lors de la mise à jour des cryptos", error);
                }
            });
        }

        // Rafraîchir toutes les 5 secondes
        setInterval(rafraichirCryptos, 5000);
    </script>
</head>
<body>

<jsp:include page="../template/menu1.jsp"/>

<main id="main" class="main">
    <br><br><br>
    <section class="section">
        <div class="row">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-body">
                        <h1>Liste de vos cryptomonnaies</h1>
                        <% if (catcour != null && !catcour.isEmpty()) { %>
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>Nom</th>
                                <th>Valeur unitaire</th>
                                <th>Quantité</th>
                                <th>Total sans commission</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody id="cryptoTableBody">
                            <% for (Portefeuille_detail cr : catcour) { %>
                            <tr>
                                <td><%= cr.getNom() %></td>
                                <td><%=  String.format("%.2f", cr.getValeur_actuelle())%> ar</td>
                                <td><%= cr.getNb() %></td>
                                <td><%=  String.format("%.2f", cr.getMontant_total()) %> ar</td>
                                <td>
                                    <button class="btn btn-danger" onclick="ouvrirVente('<%= cr.getNom() %>', <%= cr.getValeur_actuelle() %>, <%= cr.getNb() %>,'<%= cr.getId_crypto() %>')">
                                        Vendre
                                    </button>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                        <% } else { %>
                        <p class="text-center text-muted">Aucune cryptomonnaie disponible.</p>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<!-- MODAL POUR LA VENTE -->
<div class="modal fade" id="venteModal" tabindex="-1" role="dialog" aria-labelledby="venteModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="venteModalLabel">Vendre une cryptomonnaie</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="/vente" method="POST">
                    <div class="form-group">
                        <label>Nom de la cryptomonnaie</label>
                        <input type="text" class="form-control" id="cryptoNom" name="nom" readonly>
                    </div>
                    <div class="form-group">
                        <label>Valeur unitaire</label>
                        <input type="text" class="form-control" id="cryptoValeurUnitaire" name="valeur_unitaire" readonly>
                    </div>
                    <div class="form-group">
                        <label>Quantité</label>
                        <input type="number" class="form-control" id="cryptoQuantite" name="nb" min="1" step="0.01" oninput="calculerTotal()" required>
                    </div>
                    <div class="form-group">
                        <label>Total après commission</label>
                        <input type="text" class="form-control" id="cryptoTotal" name="total_apres_commission" readonly>
                    </div>
                    <input type="hidden" id="cryptoId" name="idcrypto">

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
                        <button type="submit" class="btn btn-success">Confirmer la vente</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../template/footer.jsp"/>

</body>
</html>
