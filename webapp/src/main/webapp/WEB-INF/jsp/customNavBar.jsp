<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/css/customnavbar.css" rel="stylesheet"/>

</head>
<body>
<!-- Sacado de https://getbootstrap.com/docs/4.6/components/navbar/ -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">
        <img src='<c:url value="${pageContext.request.contextPath}/resources/images/hirenet-logo-v3-invert.png" />'
        alt="<spring:message code="navigation.logo"/>"/>
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item ${requestScope['javax.servlet.forward.request_uri'] == "/" ? 'active': ''}">
                <a class="nav-link" href="${pageContext.request.contextPath}/">
                    <spring:message code="navigation.index"/>
                </a>
            </li>
            <li class="nav-item ${requestScope['javax.servlet.forward.request_uri'] == "/create-job-post" ? 'active': ''}">
                <a class="nav-link" href="${pageContext.request.contextPath}/create-job-post">
                    <spring:message code="navigation.publish"/>
                </a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="search" placeholder="<spring:message code="navigation.search"/>" aria-label="Search">
            <button class="btn btn-outline-light my-2 my-sm-0" type="submit">
                <spring:message code="navigation.search"/>
            </button>
        </form>
    </div>
</nav>

</body>
</html>
