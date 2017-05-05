<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <jsp:include page="fragments/headTag.jsp"/>
<body>
<section>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <h2><a href="${pageContext.request.contextPath}">Home</a></h2>
    <spring:message code="meals.title.add" var="addTitle"/>
    <spring:message code="meals.title.edit" var="editTitle"/>
    <h2>${meal.id == null ? addTitle : editTitle}</h2>
    <hr>
    <form method="post"  action="meals/save">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meals.date"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories"></dd>
        </dl>
        <button type="submit"><spring:message code="meals.save"/></button>
        <button onclick="window.history.back()"><spring:message code="meals.cancel"/></button>
    </form>
</section>
</body>
</html>
