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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<c:out value="${1+1}"/> 
<portlet:defineObjects />

<portlet:actionURL var="submitForm" portletMode="view"/>
<h2>Form view</h2>

<p>Compile the form</p>
<br/>
<c:if test="${! empty msg }">
    <p style="color:red">${msg}</p>
</c:if>
<form method="post"  action="${submitForm}">
First name: <input type="text" name="firstname" /><br />
Last name: <input type="text" name="lastname" /><br />
email: <input type="text" name="email" /><br />
information: <input type="text" name="info" /><br />
<input type="submit" value="Submit"/>
</form>
