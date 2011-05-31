<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring"
    uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form"
    uri="http://www.springframework.org/tags/form" %>

<h2>Twitter settings</h2>
<portlet:actionURL var="actionUrl">
  <portlet:param name="action" value="edit"/>
</portlet:actionURL>
    
<form:form action="${actionUrl}" method="POST" commandName="preferences">
<b>Tweets to download:</b>
<select name="pageSize">

<c:forEach begin="1" end="40" varStatus="status">
  <c:if test="${status.index == preferences.pageSize}">
    <option value="${status.index}" selected>${status.index}
  </c:if>
  <c:if test="${status.index != preferences.pageSize}">
    <option value="${status.index}">${status.index}
  </c:if>
</c:forEach>
</select>
<center><input type="submit" value="Save"></center>
</form:form>
