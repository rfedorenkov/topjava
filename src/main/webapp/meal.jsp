<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
  <title>Back</title>
</head>
<body>
<h3><a href="meals">Back</a></h3>
<hr>
<h2>Edit Meal</h2>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<form method="post" action="meals">
  <input type="hidden" name="id" value="${meal.id}"/>
  <label>Date time:</label>
  <input type="datetime-local" name="date" value="${meal.dateTime}" required><br>
  <label>Description:</label>
  <input type="text" name="description" value="${meal.description}" required placeholder="Description"><br>
  <label>Calories:</label>
  <input type="number" name="calories" value="${meal.calories}" required><br>
  <input type="submit" name="save" value="Save"/>
</form>
</body>
</html>
