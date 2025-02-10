<%@ page import="java.util.List" %>
<%@ page import="com.example.crypto.model.Historique_fond" %>
<%@ page import="com.example.crypto.model.Commission" %>

<%
    List<Commission> histo = (List<Commission>) request.getAttribute("commissions");
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
                                    <h2 style="align-items: center;">Commissions :</h2>
                                </div>

                                <!-- Table -->
                                <table class="table datatable">
                                    <thead>
                                    <tr>
                                        <th>Type</th>
                                        <th>Pourcentage</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <% if (histo != null && !histo.isEmpty()) { %>
                                    <% for (Commission ce : histo) { %>
                                    <tr>
                                        <td><%= ce.getType() %></td>
                                        <td><%= ce.getPourcentage() %></td>

                                        <td>
                                            <!-- Button to trigger the modal -->
                                            <button class="btn btn-success btn-sm" data-bs-toggle="modal" data-bs-target="#editModal<%= ce.getType() %>">
                                                Modifier
                                            </button>
                                        </td>
                                    </tr>

                                    <!-- Modal for editing commission -->
                                    <div class="modal fade" id="editModal<%= ce.getType() %>" tabindex="-1" aria-labelledby="editModalLabel<%= ce.getType() %>" aria-hidden="true">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="editModalLabel<%= ce.getType() %>">Modifier la Commission</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <form action="/modificomm" method="get">
                                                        <!-- Readonly type input -->
                                                        <div class="mb-3">
                                                            <label for="type" class="form-label">Type</label>
                                                            <input type="text" class="form-control" id="type" name="type" value="<%= ce.getType() %>" readonly>
                                                        </div>

                                                        <!-- Pourcentage input -->
                                                        <div class="mb-3">
                                                            <label for="pourcentage" class="form-label">Pourcentage</label>
                                                            <input type="number" class="form-control" id="pourcentage" name="pourcentage" value="<%= ce.getPourcentage() %>" step="0.01" required>
                                                        </div>

                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                                                            <button type="submit" class="btn btn-primary">Enregistrer</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <% } %>
                                    <% } %>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<jsp:include page="../template/footer.jsp"/>
