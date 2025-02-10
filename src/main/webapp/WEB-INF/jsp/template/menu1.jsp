

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <script>
        function updateFond() {
            fetch('/fond')
                .then(response => response.text())
                .then(data => {
                    document.getElementById('fondInput').value = data + " Ar";
                })
                .catch(error => console.error('Erreur lors du chargement du fond:', error));
        }

        document.addEventListener('DOMContentLoaded', updateFond);
        setInterval(updateFond, 60000); // Mettre à jour toutes les 60 secondes
    </script>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Responsive Admin &amp; Dashboard Template based on Bootstrap 5">
    <meta name="author" content="AdminKit">
    <meta name="keywords" content="adminkit, bootstrap, bootstrap 5, admin, dashboard, template, responsive, css, sass, html, theme, front-end, ui kit, web">

    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link rel="shortcut icon" href="assets/img/icons/icon-48x48.png" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">

    <link rel="canonical" href="https://demo-basic.adminkit.io/" />

    <title>EEEE</title>

    <link href="assets/css/app.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
</head>

<body>
<div class="wrapper">
    <nav id="sidebar" class="sidebar js-sidebar">
        <div class="sidebar-content js-simplebar">

                <span class="align-middle" style="display: flex; justify-content: center;"><img src="assets/img/photos/1.png" alt="Wallet" style="width:45%; border-radius: 50%"></span>


            <ul class="sidebar-nav">
                <li class="sidebar-header">
                    Crypto
                </li>
                <li class="sidebar-item active">
                    <a class="sidebar-link" href="/portefeuille">
                        <i class="align-middle" data-feather="sliders"></i> <span class="align-middle"> Portefeuille</span>
                    </a>
                </li>

                <li class="sidebar-item active">
                    <a class="sidebar-link" href="/listecrypto">
                        <i class="align-middle" data-feather="sliders"></i> <span class="align-middle"> Voir cryptomonnaies</span>
                    </a>
                </li>
                <li class="sidebar-item active">
                    <a class="sidebar-link" href="/analyse">
                        <i class="align-middle" data-feather="sliders"></i> <span class="align-middle"> Analyse cryptomonnaies</span>
                    </a>
                </li>
            </ul>

            <div class="sidebar-cta">
                <div class="sidebar-cta-content">
                    <strong class="d-inline-block mb-2">Upgrade to Pro</strong>
                    <div class="mb-3 text-sm">
                        Are you looking for more components? Check out our premium version.
                    </div>
                    <div class="d-grid">
                        <a href="#" class="btn btn-primary">Upgrade to Pro</a>
                    </div>
                </div>
            </div>
        </div>
    </nav>

    <div class="main">

        <nav class="navbar navbar-expand navbar-light navbar-bg">

            <a class="sidebar-toggle js-sidebar-toggle">
                <i class="hamburger align-self-center"></i>
            </a>
            <div class="navbar-collapse collapse">
                <ul class="navbar-nav navbar-align">
                    <!-- Affichage du Fond -->
                    <li class="nav-item">
                        <div class="d-flex align-items-center">
                            <input type="text" class="form-control me-2" id="fondInput" readonly style="width: 120px;">
                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#fondModal">
                                <i class="fas fa-plus"></i>
                            </button>
                        </div>
                    </li>

                    <!-- Notification Bell -->
                    <li class="nav-item dropdown">
                        <a class="nav-icon dropdown-toggle" href="#" id="alertsDropdown" data-bs-toggle="dropdown">
                            <div class="position-relative">
                                <i class="align-middle" data-feather="bell"></i>
                                <span class="indicator">4</span>
                            </div>
                        </a>
                    </li>

                    <!-- Autres éléments -->
                    <li class="nav-item dropdown">
                        <a class="nav-icon dropdown-toggle d-inline-block d-sm-none" href="#" data-bs-toggle="dropdown">
                            <i class="align-middle" data-feather="settings"></i>
                        </a>
                        <a class="nav-link d-sm-inline-block" href="/logout">
                            <i class="fas fa-sign-out-alt me-1">logout</i>
                        </a>
                    </li>
                </ul>
            </div>

        </nav>

        <main class="content">
            <!-- Modal pour Ajouter un Fond -->

            <div class="modal fade" id="fondModal" tabindex="-1" aria-labelledby="fondModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="fondModalLabel">Ajouter un Fond</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="/insertfond" method="post">
                                <div class="mb-3">
                                    <label for="montant" class="form-label">Montant</label>
                                    <input type="number" step="0.01" class="form-control" id="montant" name="montant" required>
                                </div>
                                <div class="mb-3">
                                    <label for="motif" class="form-label">Motif</label>
                                    <input type="text" class="form-control" id="motif" name="motif" required>
                                </div>
                                <div class="mb-3">
                                    <label for="type" class="form-label">Type</label>
                                    <select class="form-control" id="type" name="type">
                                        <option value="1">Dépôt</option>
                                        <option value="-1">Retrait</option>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-primary">Valider</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="container-fluid p-0">
                <main class="content">
                <script src="assets/js/app.js"></script>