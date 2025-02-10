<%@ page import="java.util.List" %>
<%@ page import="com.example.crypto.model.Historique_fond" %>

<%
    List<Historique_fond> histo = (List<Historique_fond>) request.getAttribute("histos");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer pageSize = (Integer) request.getAttribute("pageSize");
%>

<jsp:include page="../template/menu.jsp"/>
<main id="main" class="main">
    <section class="section">
        <div class="row">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-body">
                        <div class="modal-dialog modal-dialog-scrollable">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h2 style="align-items: center;">Voir les fonds :</h2>
                                </div>

                                <!-- Table -->
                                <table class="table datatable">
                                    <thead>
                                    <tr>
                                        <th>ID Utilisateur</th>
                                        <th>Montant</th>
                                        <th>Motif</th>
                                        <th>Type</th>
                                        <th>Date et heure</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <% if (histo != null && !histo.isEmpty()) { %>
                                    <% for (Historique_fond ce : histo) { %>
                                    <tr>
                                        <td><%= ce.getId_utilisateur() %></td>
                                        <td><%= ce.getMontant() %></td>
                                        <td><%= ce.getMotif() %></td>
                                        <td><%= ce.getTypes() == 1 ? "Dépôt" : (ce.getTypes() == -1 ? "Retrait" : "Autre") %></td>
                                        <td><%= ce.getDate_heure() %></td>
                                        <td>
                                            <a href="<%= request.getContextPath() %>/actionhisto?nb=1&idhisto=<%= ce.getId_histofond() %>"
                                               class="btn btn-success btn-sm">
                                                Valider
                                            </a>
                                            <a href="<%= request.getContextPath() %>/actionhisto?nb=0&idhisto=<%= ce.getId_histofond() %>"
                                               class="btn btn-danger btn-sm">
                                                Refuser
                                            </a>
                                        </td>
                                    </tr>
                                    <% } %>
                                    <% } %>
                                    </tbody>
                                </table>

                                <!-- Pagination -->
                                <% if (totalPages != null && totalPages > 1) { %>
                                <nav aria-label="Page navigation">
                                    <div class="row justify-content-center">
                                        <div class="col-auto">
                                            <% if (currentPage > 0) { %>
                                            <a href="?page=<%= currentPage - 1 %>&pageSize=<%= pageSize %>" aria-label="Previous">&laquo;</a>
                                            <% } %>
                                        </div>
                                        <% for (int i = 1; i <= totalPages; i++) { %>
                                        <div class="col-auto">
                                            <% if (i - 1 == currentPage) { %>
                                            <span><%= i %></span>
                                            <% } else { %>
                                            <a href="?page=<%= i - 1 %>&pageSize=<%= pageSize %>"><%= i %></a>
                                            <% } %>
                                        </div>
                                        <% } %>
                                        <div class="col-auto">
                                            <% if (currentPage < totalPages - 1) { %>
                                            <a href="?page=<%= currentPage + 1 %>&pageSize=<%= pageSize %>" aria-label="Next">&raquo;</a>
                                            <% } %>
                                        </div>
                                    </div>
                                </nav>
                                <% } %>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<jsp:include page="../template/footer.jsp"/>
