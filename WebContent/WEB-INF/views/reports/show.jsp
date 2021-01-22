<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--  --%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose><%-- if-else文 --%>
            <c:when test="${report != null}"><%-- 日報の詳細（idをもとにデータベースから取得した１件のデータ）がある時 --%>
                <h2>日報　詳細ページ</h2>

                <table><%-- テーブルで詳細を表示 --%>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${report.employee.name}" /></td><%-- 左がヘッダーで右がデータのテーブル表示の形 --%>
                        </tr>
                        <tr>
                            <th>日付</th>
                            <td><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                        <tr>
                            <th>内容</th>
                            <td>
                                <pre><c:out value="${report.content}" /></pre><%-- ＜pre＞＝ソースに記述されたスペースや改行など、そのまま表示する（＜や＞は例外。&lt;、&gt;で） --%>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${report.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${report.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <c:if test="${sessionScope.login_employee.id == report.employee.id}"><%-- ログインidと、作成したidが等しいとき＝編集できるのは作成者のみ --%>
                    <p><a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a></p>
                </c:if>

            </c:when>
            <c:otherwise><%-- データが無かったとき --%>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>
    </c:param>
</c:import>