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
<h3>Please login to Liferay first!</h3>
    </c:when>
      <c:when test="${! empty toprint}">
          <font color="red">Please visit <a href="${toprint}" target="_blank">Twitter</a></font>
          <form:form style="width: 100%" method="post" action="${submitForm}">
            Pin: <input type="text" size="10" name="twitterpin" />
            <input type="submit" value="Submit"/>
          </form:form>
              
      </c:when>
        <c:when test="${! empty username}">
            <h2>You are Logged in as ${username}</h2>

<form:form style="width: 100%" method="post" action="${submitForm}">
    <input type="hidden" value="logout" name="logout" />
<input type="submit" value="Logout"/>
</form:form>
      </c:when>
      <c:otherwise>
      </c:otherwise>
    </c:choose>



