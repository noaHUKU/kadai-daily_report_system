<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>日報管理システム</title>
        <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
        <link rel="stylesheet" href="<c:url value='/css/style.css' />">
    </head>
    <body>
        <div id="wrapper"><%-- cssで使う名前タグ付け --%>
            <div id="header">
                <div id="header_menu"><%-- 各ページにとぶリンクと名前。ヘッダーのところ --%>
                    <h1><a href="<c:url value='/' />">日報管理システム</a></h1>&nbsp;&nbsp;&nbsp;<%-- 空欄。/のurlはトップページへのリンク --%>
                    <c:if test="${sessionScope.login_employee != null}"><%-- login_employeeがある＝ログインした --%>
                        <c:if test="${sessionScope.login_employee.admin_flag == 1}"><%-- 管理者であるとき --%>
                            <a href="<c:url value='/employees/index' />">従業員管理</a>&nbsp;
                        </c:if>
                        <a href="<c:url value='/reports/index' />">日報管理</a>&nbsp;
                    </c:if>
                </div>

                <c:if test="${sessionScope.login_employee != null}">
                    <div id="employee_name">
                        <c:out value="${sessionScope.login_employee.name}" />&nbsp;さん&nbsp;&nbsp;&nbsp;
                        <a href="<c:url value='/logout' />">ログアウト</a>
                    </div>
                </c:if>


            </div>
            <div id="content">
                ${param.content}<%-- ここにいろいろ入る --%>
            </div>
            <div id="footer">
                by Taro Kitameki.
            </div>
        </div>

    </body>
</html>