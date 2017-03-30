<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Add meal</title>
</head>
<body>
    <h2>Create new meal</h2>
    <form action="meals?action=add" method="post">
        <table>
            <tr>
                <td>Description</td>
                <td><input name="description" type="text" required /></td>
            </tr>
            <tr>
                <td>Calories</td>
                <td><input name="calories" type="number" min="1" max="2000" required ></td>
            </tr>
            <tr>
                <td>Date and time</td>
                <td><input name="dateTime" type="datetime-local" required ></td>
            </tr>
        </table>
        <p><input type="submit" value="Add"></p>
    </form>
</body>
</html>
