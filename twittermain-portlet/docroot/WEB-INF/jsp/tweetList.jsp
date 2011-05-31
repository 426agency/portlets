<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ taglib prefix="spring"
    uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form"
    uri="http://www.springframework.org/tags/form" %>

<portlet:defineObjects/>

<portlet:actionURL var="submitForm" portletMode="view"/>

<c:choose>
    <c:when test="${! empty toprint}">
        <h2>You are not allowed to see this content.</h2>
<h3>Please login using the Twitter Login portlet</h3>
    </c:when>
    <c:otherwise>
 <c:if test="${! empty numtweets }">
    <p style="color:red">You have ${numtweets} new Tweet!</p>
</c:if>
<form:form style="width: 100%" method="post" action="${submitForm}">
    Tweet: <input type="text" size="75" maxlength="140" name="tweettext" />
<input type="submit" value="Submit"/>
</form:form>
<hr/>

<display:table name="stats"
    pagesize="${pageSize}" export="false"
    id="status" style="width:100%" sort="external"
    defaultsort="1">

 <display:column sortable="false" title="User">
    <c:out value="${status.user.name}"/>
  </display:column>


  <display:column sortable="false" title="Tweet">
    <c:out value="${status.text}"/>
  </display:column>
</display:table>
    </c:otherwise>
</c:choose>


