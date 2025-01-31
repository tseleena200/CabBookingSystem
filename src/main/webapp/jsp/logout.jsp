<%--
  Created by IntelliJ IDEA.
  User: tsele
  Date: 31/01/2025
  Time: 2:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%
    // Invalidate session on logout
    HttpSession userSession = request.getSession(false);
    if (userSession != null) {
        userSession.invalidate();
    }

    // Redirect back to login page
    response.sendRedirect("../html/login.html?message=logged_out");
%>
