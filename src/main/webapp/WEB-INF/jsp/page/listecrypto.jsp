<%@ page import="java.util.List" %>
<%@ page import="com.example.crypto.model.Crypto" %>
<%
    List<Crypto> m = (List<Crypto>) request.getAttribute("cryptos");
    double commissionPourcentage = (double) request.getAttribute("pourcentage");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Cryptomonnaies</title>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

    <!-- Bootstrap JS (including Popper.js) -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>

    <script>
        function updateCryptoValues() {
            $.ajax({
                url: '/cryptos',
                method: 'GET',
                dataType: 'json',
                success: function(response) {
                    response.forEach(function(crypto) {
                        var cryptoValueElement = $('#value_' + crypto.id_crypto);
                        if (cryptoValueElement.length) {
                            cryptoValueElement.text(parseFloat(crypto.valeur_actuelle).toFixed(2) + ' ar');
                        }

                        if ($('#cryptoId').val() == crypto.id_crypto) {
                            $('#cryptoPrix').val(parseFloat(crypto.valeur_actuelle).toFixed(2));
                            calculerTotal();
                        }
                    });
                },
                error: function(err) {
                    console.log('Erreur lors de la mise à jour des cryptos:', err);
                }
            });
        }

        setInterval(updateCryptoValues, 5000);
        $(document).ready(updateCryptoValues);

        var commission = <%= commissionPourcentage%>;

        function openAchatModal(id, nom, prix) {
            $('#cryptoId').val(id);
            $('#cryptoNom').text(nom);
            $('#cryptoPrix').val(prix);
            $('#cryptoTotal').text('0');
            $('#achatModal').modal('show');
        }

        function calculerTotal() {
            let quantite = $('#cryptoQuantite').val();
            let prixUnitaire = $('#cryptoPrix').val();

            if (!quantite || !prixUnitaire) return;

            let total = quantite * prixUnitaire;
            let totalAvecCommission = total + (total * (commission / 100));

            $('#cryptoTotal').text(totalAvecCommission.toFixed(2) + ' ar');
        }

        function acheterCrypto() {
            let idCrypto = $('#cryptoId').val();
            let quantite = $('#cryptoQuantite').val();

            console.log('ID Crypto:', idCrypto);
            console.log('Quantité:', quantite);

            $.post('/achat', { idcrypto: idCrypto, nb: quantite }, function(response) {
                alert('Achat réussi !');
                closeAchatModal();
                updateCryptoValues();
            }).fail(function(err) {
                alert('Erreur lors de l\'achat.');
                console.log('Erreur:', err);
            });
        }

        function closeAchatModal() {
            $('#achatModal').modal('hide');
        }

    </script>

    <style>
        .crypto-card {
            text-align: center;
            padding: 15px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        .crypto-img {
            width: 60px;
            height: 60px;
            object-fit: contain;
            display: block;
            margin: 0 auto 10px;
        }
        .crypto-name {
            font-size: 16px;
            font-weight: bold;
        }
        .crypto-price {
            font-size: 14px;
            color: #555;
        }
    </style>
</head>
<body>

<jsp:include page="../template/menu1.jsp"/>

<main id="main" class="main">
    <section class="section">
        <div class="row">
            <% for (Crypto cr : m) { %>
            <div class="col-md-3">
                <div class="card crypto-card">
                    <img src="assets/img/photos/vola.webp" class="crypto-img" alt="<%= cr.getNom() %>">
                    <div class="card-body">
                        <p class="crypto-name"><%= cr.getNom() %></p>
                        <p class="crypto-price" id="value_<%= cr.getId_crypto() %>"><%= cr.getValeur_actuelle() %> ar</p>
                        <button class="btn btn-primary" onclick="openAchatModal('<%= cr.getId_crypto() %>', '<%= cr.getNom() %>', '<%= cr.getValeur_actuelle() %>')">Acheter</button>
                        <a href="/graph?id_crypto=<%= cr.getId_crypto() %>" class="btn btn-secondary mt-3">Evolution</a>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </section>
</main>


<!-- MODAL ACHAT -->
<div class="modal fade" id="achatModal" tabindex="-1" aria-labelledby="achatModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="achatModalLabel">Acheter <span id="cryptoNom"></span></h5>
                <button type="button" class="close" onclick="closeAchatModal()" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="cryptoId">
                <div class="form-group">
                    <label for="cryptoQuantite">Quantité :</label>
                    <input type="number" class="form-control" id="cryptoQuantite" oninput="calculerTotal()">
                </div>
                <div class="form-group">
                    <label for="cryptoPrix">Prix unitaire :</label>
                    <input type="text" class="form-control" id="cryptoPrix" readonly>
                </div>
                <div class="form-group">
                    <label>Total avec commission :</label>
                    <p id="cryptoTotal" class="font-weight-bold">0 ar</p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeAchatModal()">Fermer</button>
                <button type="button" class="btn btn-success" onclick="acheterCrypto()">Confirmer l'achat</button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="../template/footer.jsp"/>

</body>
</html>
