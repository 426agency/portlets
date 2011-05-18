<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>

<portlet:defineObjects />

<h2>You are Logged in as ${username}</h2>
<portlet:actionURL var="logoutForm" portletMode="view"/>
<portlet:defineObjects />

<form style="width: 100%" method="post" action="${logoutForm}">
    <input type="hidden" value="logout" name="logout" />
<input type="submit" value="Logout"/>
</form>
