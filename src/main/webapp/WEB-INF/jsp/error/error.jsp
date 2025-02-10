<%
    String error  = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur</title>
</head>
<body>
    <jsp:include page="menu.jsp" /> 
    <main id="main" class="main">
    
        <section class="section">
            <div class="row">
            <div class="col-lg-6">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <h4 class="alert-heading">Error page</h4>
                    <p><%= error %></p>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </div>
        </section>

    </main>
    <jsp:include page="footer.jsp" /> 
</body>
</html>