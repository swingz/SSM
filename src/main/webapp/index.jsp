<%--
  Created by IntelliJ IDEA.
  User: Swingz
  Date: 2020/11/23
  Time: 18:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="/account/findAll">查询所有账户信息</a>

<form method="post" action="/account/save">
    姓名：<input name="name" type="text" /><br>
    金钱：<input name="money" type="text" /><br>
    <input type="submit" value="提交">
</form>
</body>
</html>
