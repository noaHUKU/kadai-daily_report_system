<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}"><%-- フラッシュメッセージがnullでない＝存在する時 --%>
            <div id="flush_success">
                <c:out value="${flush}"></c:out><%-- フラッシュメッセージ表示する（cssの名前flush_success） --%>
            </div>
        </c:if>
        <h2>従業員　一覧</h2>
        <table id="employee_list"><%-- テーブルemployee_list作成 --%>
            <tbody>
                <tr><%-- テーブル行。ラインのｒ --%>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="employee" items="${employees}" varStatus="status"><%-- for文繰り返し???? --%>
                    <tr class="row${status.count % 2}"><%-- テーブルライン.。行???? --%>
                        <td><c:out value="${employee.code}" /></td><%-- 社員番号 --%>
                        <td><c:out value="${employee.name}" /></td><%-- 社員名 --%>
                        <td>
                            <c:choose><%-- if文のelseあり --%>
                                <c:when test="${employee.delete_flag == 1}"><%-- ？？？？1と等しい時 --%>
                                    （削除済み）<%-- 表示 --%>
                                </c:when>
                                <c:otherwise><%-- 条件が合わない時１と等しくない時？ --%>
                                    <a href="<c:url value='/employees/show?id=${employee.id}' />">詳細を表示</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table><%-- テーブル終了 --%>

        <div id="pagination"><%-- ここからまた違うくくり --%>
            （全 ${employees_count} 件）<br /><%-- 件数表示して改行 --%>
            <c:forEach var="i" begin="1" end="${((employees_count - 1) / 15) + 1}" step="1"><%-- ページを15件まとめで１から順にページ数の表示。繰り返し --%>
                <c:choose><%-- if-else文 --%>
                    <c:when test="${i == page}"><%-- ページ（パラメータで取得した）とカウント変数？が等しい時 --%>
                        <c:out value="${i}" />&nbsp;<%-- カウント変数表示 + 空欄。　これページ数だ　リンク貼らずにそのまま数字表示するってことか --%>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/employees/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;<%-- そのページ数のページへのリンクの数字表示 --%>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/employees/new' />">新規従業員の登録</a></p><%-- 新規登録するサーブレットへリンク --%>

    </c:param>
</c:import>