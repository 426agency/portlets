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

<portlet:defineObjects />

<portlet:renderURL var="urlForm" portletMode="view">
    <portlet:param name="nextview" value="showform">
    </portlet:param>
</portlet:renderURL>

<h2>Main View</h2>

<a href="${urlForm}">Request information</a><br/>


Public Render Parameter test:
<br/>
Tag read from parameter:${param.tag}
<br/>

<portlet:renderURL var="pubTagUrl" portletMode="view">
        <portlet:param name="tag" value="sport">
    </portlet:param>
</portlet:renderURL>
<a href="${pubTagUrl}">url with tag parameter set to sport</a><br/>
