<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="footer">
    <div class="container">
        <spring:message code="app.footer"/>
    </div>
    <script>
        var i18n = [];
        <c:forEach var='key' items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus"}%>'>
        i18n['${key}'] = '<spring:message code="${key}"/>';
        </c:forEach>
    </script>
</div>