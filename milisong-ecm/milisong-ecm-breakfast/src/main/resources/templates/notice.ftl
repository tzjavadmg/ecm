<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <title>订单通知</title>
</head>
<body>
<div style="text-align: center;">
    <p>
        <div style="font-size: 45px">订单通知结果:</div>
    </p>
    <#if success == true >
        <div style="color: limegreen;font-size: 50px;font-weight:bold">成功!</div>
    <#else>
        <div style="color: red;font-size: 50px;font-weight:bold">失败! </div>
    <br/>
        <div style="color: red;font-size: 40px">
            ${message}
        </div>
    </#if>
    <br>
</div>
</body>
</html>