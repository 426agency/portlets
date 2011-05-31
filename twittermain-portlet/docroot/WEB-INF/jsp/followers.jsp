<%
/**
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
%>

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