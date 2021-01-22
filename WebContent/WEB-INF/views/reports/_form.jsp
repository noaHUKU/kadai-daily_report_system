<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${errors != null}"><%-- エラーメッセージがある時 --%>
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}"><%-- 繰り返し --%>
            ・<c:out value="${error}" /><br /><%-- エラーメッセージを全て表示 --%>
        </c:forEach>

    </div>
</c:if>
<label for="report_date">日付</label><br /><%-- ブラウザによっては date にすると日付の入力補助機能が表示 --%>
<input type="date" name="report_date" value="<fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' />" />
<br /><br />

<label for="name">氏名</label><br />
<c:out value="${sessionScope.login_employee.name}" /><%-- 作成者は決まっているので入力しない＝<input>ではない --%>
<br /><br />

<label for="title">タイトル</label><br />
<input type="text" name="title" value="${report.title}" />
<br /><br />

<label for="content">内容</label><br />
<textarea name="content" rows="10" cols="50">${report.content}</textarea>
<br /><br />

<input type="hidden" name="_token" value="${_token}" /><%-- セッションIDを隠しデータとして渡す --%>
<button type="submit">投稿</button>

