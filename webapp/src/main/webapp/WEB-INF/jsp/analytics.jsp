<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>
        <spring:message code="analytics.title" var="text"/>
        <spring:message code="title.name" arguments="${text}"/>
    </title>

    <%-- Bootstrap 4.5.2 CSS minified --%>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">

    <%-- jQuery 3.6.0 minified dependency for Bootstrap JS libraries --%>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"
            integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>

    <%-- Popper libraries minified for Bootstrap compatibility --%>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
            integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
            crossorigin="anonymous"></script>

    <%-- Bootstrap 4.5.2 JS libraries minified --%>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"
            integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV"
            crossorigin="anonymous"></script>

    <%-- FontAwesome Icons--%>
    <script src="https://kit.fontawesome.com/108cc44da7.js" crossorigin="anonymous"></script>

    <!--  Bootstrap icons   -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css">

    <link href="${pageContext.request.contextPath}/resources/css/styles.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/css/analytics.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/css/jobcard.css" rel="stylesheet"/>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/icon.svg">
    <link rel="apple-touch-icon" href="${pageContext.request.contextPath}/resources/images/apple-touch-icon.png">
</head>
<body>
<c:set var="path" value="/analytics" scope="request"/>
<c:set var="zoneValues" value="${zoneValues}" scope="request"/>
<%@include file="components/customNavBar.jsp" %>

<div class="content-container-transparent">
    <jsp:include page="components/siteHeader.jsp">
        <jsp:param name="code" value="analytics.title"/>
    </jsp:include>
    <div class="highlights-container">
        <div class="highlight card custom-card">
            <div class="card-body">
                <c:choose>
                    <c:when test="${user.image.string != null}">
                        <c:set var="profilePic" value="data:${user.image.type};base64,${user.image.string}"/>
                    </c:when>
                    <c:otherwise>
                        <c:url var="profilePic" value="/resources/images/defaultavatar.svg"/>
                    </c:otherwise>
                </c:choose>
                <img class="analytics-profile-img"
                     src='${profilePic}'
                     alt="<spring:message code="profile.image"/>">
                <p class="mb-0 analytics-profile-title"><c:out value="${user.username}"/></p>
            </div>
        </div>
        <div class="highlight align-self-center">
            <jsp:include page="components/avgRateAndCompletedContracts.jsp">
                <jsp:param name="avgRate" value="${avgRate}"/>
                <jsp:param name="totalContractsCompleted" value="${totalContractsCompleted}"/>
                <jsp:param name="totalReviewsSize" value="${totalReviewsSize}"/>
            </jsp:include>
        </div>
        <c:forEach begin="1" end="4" var="i">
            <div class="highlight card custom-card">
                <div class="d-flex align-items-center card-body">
                    <div class="w-100">
                        <p class="analytics-highlight-title">
                            <spring:message code="highlight.ranking" arguments="${i}"/>
                        </p>
                        <p class="analytics-highlight-subtitle">
                            <spring:message code="highlight.rankingCategory" arguments="pEasdasd asd asd asd asdpe"/>
                        </p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${jobCards.size() > 0}">
        <%--         TODO: VERIFICAR QE LA LISTA TOTAL SEA VACIA--%>
        <div class="card custom-card mt-5">
            <p class="card-header"><spring:message code="analytics.relatedPosts"/></p>
            <div class="card-body">
                <c:forEach begin="1" end="2" var="i">
                    <c:if test="${jobCards.size() > 0}">
                        <p class="analytics-related-job-type"><spring:message code="analytics.relatedJobType"
                                                                              arguments="testtest${i}"/></p>
                        <%--                TODO: CHECKEAR QUE HAY CARDS EN LA CATEGORIA--%>
                        <hr class="hr1"/>
                        <div class="job-display-container">
                            <c:forEach items="${jobCards}" var="jobCard" varStatus="status">
                                <c:set var="data" value="${jobCard}" scope="request"/>
                                <c:import url="components/jobCard.jsp"/>
                            </c:forEach>
                        </div>
                    </c:if>
                    <br/>
                </c:forEach>
            </div>
        </div>
    </c:if>
</div>
<jsp:include page="components/footer.jsp"/>
</body>
</html>
