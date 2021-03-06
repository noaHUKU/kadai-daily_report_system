<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose><%-- if-else文 --%>
            <c:when test="${report != null}"><%-- 編集したいidのデータがあるとき --%>
                <h2>日報　編集ページ</h2>
                <form method="POST" action="<c:url value='/reports/update' />"><%-- POSTで更新処理のサーブレットに送られる（＝更新処理のサーブレットはPOSTにかく） --%>
                    <c:import url="_form.jsp" />
                </form>
            </c:when>
            <c:otherwise><%-- else文 --%>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value='/reports/index' />">一覧に戻る</a></p>
    </c:param>
</c:import>
