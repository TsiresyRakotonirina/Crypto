<%@ page import="java.util.Vector" %>
<%@ page import="com.example.crypto.model.Analyse_crypto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Évolution de la Cryptomonnaie</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<jsp:include page="../template/menu1.jsp"/>

<main id="main" class="main">
    <section class="section">
        <div class="row">
            <h2 class="text-center">Évolution de la cryptomonnaie</h2>

            <canvas id="cryptoChart" width="400" height="200"></canvas>

            <a href="/listecrypto" class="btn btn-secondary mt-3">Retour</a>
        </div>
    </section>
</main>

<jsp:include page="../template/footer.jsp"/>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var idCrypto = "<%= request.getAttribute("id_crypto") %>";
        var ctx = document.getElementById('cryptoChart').getContext('2d');

        // Initial data structure for the chart
        var data = {
            labels: [],
            datasets: [{
                label: 'Valeur de la crypto',
                data: [],
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
                fill: false
            }]
        };

        var chart = new Chart(ctx, {
            type: 'line', // Line chart type
            data: data,
            options: {
                scales: {
                    x: {
                        type: 'linear',
                        position: 'bottom'
                    }
                }
            }
        });

        // Function to fetch initial data (historical data)
        function fetchInitialData() {
            fetch('/evolution?id_crypto=' + idCrypto)
                .then(response => response.json())
                .then(data => {
                    console.log('Réponse historique : ', data);  // Log historical data
                    if (data && Array.isArray(data)) {
                        data.forEach(item => {
                            let time = new Date(item.date_heure).toLocaleTimeString();
                            chart.data.labels.push(time);
                            chart.data.datasets[0].data.push(item.valeur);
                        });
                        chart.update();  // Update the chart with initial data
                    } else {
                        console.error('Aucune donnée reçue');
                    }
                })
                .catch(error => console.error('Erreur lors de la récupération des données initiales:', error));
        }

        // Function to fetch new data every 10 seconds (real-time data)
        function fetchDataAndUpdateChart() {
            fetch('/evolution?id_crypto=' + idCrypto)
                .then(response => response.json())
                .then(data => {
                    console.log('Réponse nouvelle donnée : ', data);  // Log new data
                    if (data) {
                        let time = new Date(data.date_heure).toLocaleTimeString();
                        chart.data.labels.push(time);
                        chart.data.datasets[0].data.push(data.valeur);
                        chart.update();  // Update the chart with new data
                    } else {
                        console.error('Aucune nouvelle donnée reçue');
                    }
                })
                .catch(error => console.error('Erreur lors de la récupération des nouvelles données:', error));
        }

        // Fetch initial data when the page loads
        fetchInitialData();

        // Update the chart every 10 seconds with new data
        setInterval(fetchDataAndUpdateChart, 10000); // 10000 ms = 10 secondes
    });

</script>


</body>
</html>
