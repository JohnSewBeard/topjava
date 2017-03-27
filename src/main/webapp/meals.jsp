<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>User Meals</title>
</head>
<body>
    <h2>Meal list</h2>
    <table border="1px" cellpadding="0" cellspacing="0" >
        <thead>
        <tr>
            <th width="15%">Date and time</th>
            <th width="15%">Description</th>
            <th width="15%">Calories</th>
        </tr>
        <c:forEach var="meal" items='${requestScope.meals}' >
        <%--if exceed - change text color--%>
        <tr style="${meal.exceed ? "color: red" : ""}">
            <td>${fn:replace(meal.dateTime, 'T', ' ')}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
        </c:forEach>
    </table>
    <h2><a href="index.html">Home</a></h2>
</body>
</html>
