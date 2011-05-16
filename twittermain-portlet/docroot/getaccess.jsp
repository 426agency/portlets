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

<portlet:defineObjects />

<portlet:actionURL var="submitForm" portletMode="view"/>
<portlet:defineObjects />
<c:if test="${! empty numtweets }">
    <p style="color:red">You have ${numtweets} new Tweet!</p>
</c:if>
<form style="width: 100%" method="post" action="${submitForm}">
    Tweet: <input type="text" size="100" name="tweettext" />
<input type="submit" value="Submit"/>
</form>
<br>
