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
            <th width="5%">Id</th>
            <th width="15%">Date and time</th>
            <th width="15%">Description</th>
            <th width="15%">Calories</th>
            <th width="10%">Action</th>
        </tr>
        <c:forEach var="meal" items='${meals}' >
        <%--if exceed - change text color--%>
        <tr style="${meal.exceed ? "color: red" : "color: green"}">
            <td>${meal.id}</td>
            <td>${fn:replace(meal.dateTime, 'T', ' ')}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <a href="meals?action=update&id=${meal.id}">Edit</a>   <br>
                <a href="meals?action=delete&id=${meal.id}">Delete</a> <br>
            </td>
        </tr>
        </c:forEach>
    </table>
    <p><a href="meals?action=add"><button type="submit">Add meal</button></a></p>
    <h2><a href="index.html">Home</a></h2>
</body>
</html>
