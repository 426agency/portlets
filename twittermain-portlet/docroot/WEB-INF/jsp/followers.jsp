<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring"
    uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form"
    uri="http://www.springframework.org/tags/form" %>

<portlet:defineObjects />


<portlet:actionURL var="submitForm" portletMode="view"/>

<c:choose>
    <c:when test="${! empty errormsg}">
        <h2>You are not allowed to see this content.</h2>
<h3>Please login using the Twitter Login portlet</h3>
    </c:when>
    <c:otherwise>
<c:if test="${! empty msg }">
    <p style="color:red">${msg}</p>
</c:if>

    <p>${toprint}</p>

<form:form style="width: 100%" method="post" action="${submitForm}">
Follow: <input type="text" name="followername" />
<input type="submit" value="Submit"/>
</form:form>
    </c:otherwise>
    </c:choose>