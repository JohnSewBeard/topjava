<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>

<html>
<head>
    <title>Update meal</title>
</head>
    <body>
        <form action="meals?action=update" method="post">
            <table>
                <tr hidden>
                    <td>Id</td>
                    <td><input name="id" type="number" value="${meal.id}" /></td>
                </tr>
                <tr>
                    <td>Description</td>
                    <td><input name="description" type="text" value="${meal.description}" required/></td>
                </tr>
                <tr>
                    <td>Calories</td>
                    <td><input name="calories" type="number" min="1" max="2000" value="${meal.calories}" required></td>
                </tr>
                <tr>
                    <td>Date and time</td>
                    <td><input name="dateTime" type="datetime-local" value="${meal.dateTime}" required></td>
                </tr>
            </table>
            <p><input type="submit" value="Edit"></p>
        </form>
    </body>
</html>
