<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}"><%-- フラッシュメッセージがある時 --%>
            <div id="flush_success">
                <c:out value="${flush}"></c:out><%-- フラッシュメッセージ表示 --%>
            </div>
        </c:if>
        <h2>日報　一覧</h2>
        <table id="report_list"><%-- ここからテーブル --%>
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status"><%-- 繰り返し表示 --%>
                    <tr class="row${status.count % 2}"><%-- 繰り返しをtrでまとめずにやったら１行目の横に繋がっていってしまった --%>
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}" step="1"><%-- ページネーション --%>
                <c:choose><%-- if-else文 --%>
                    <c:when test="${i == page}"><%-- カウント変数がページ数と同じ時、そのまま表示 --%>
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise><%-- それ以外の時リンクを貼ったページ数を表示 --%>
                        <a href="<c:url value='/reports/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/reports/new' />">新規日報の登録</a></p>

    </c:param>
</c:import>