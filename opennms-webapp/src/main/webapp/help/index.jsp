<%--

//
// This file is part of the OpenNMS(R) Application.
//
// Copyright (C) 2010 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//

--%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page language="java" contentType="text/html" session="true"  %>

<jsp:include page="/includes/header.jsp" flush="false">
	<jsp:param name="title" value="Web Console" />
</jsp:include>


<c:redirect url="/support/index.htm" />

<jsp:include page="/includes/footer.jsp" flush="false" />
