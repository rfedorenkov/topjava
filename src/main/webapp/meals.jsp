<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meal list</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<p>
    <a href="meals?action=add">Add Meal</a>
</p>
<div style="margin: 20px">
    <table border="1" style="border-collapse: collapse; border: 1px solid black">
        <jsp:useBean id="meals" scope="request" type="java.util.List"/>
        <tr>
            <td>Date</td>
            <td>Description</td>
            <td>Calories</td>
            <td></td>
            <td></td>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <c:set var="lineColor" value="${meal.excess ? 'red' : 'green'}"/>
            <tr style="color: ${lineColor}">
                <td>${meal.date} ${meal.time}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
