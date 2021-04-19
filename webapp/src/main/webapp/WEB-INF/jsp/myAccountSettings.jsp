<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<title>
		<spring:message code="account.settings.title" var="text"/>
		<spring:message code="title.name" arguments="${text}"/>
	</title>
	<!-- Bootstrap 4.5.2 CSS minified -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
		  integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
	<!-- jQuery 3.6.0 minified dependency for Bootstrap JS libraries -->
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"
			integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
	<!-- Bootstrap 4.5.2 JS libraries minified -->
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"
			integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV"
			crossorigin="anonymous"></script>
	<script src="https://kit.fontawesome.com/108cc44da7.js" crossorigin="anonymous"></script>
	<link href="${pageContext.request.contextPath}/resources/css/styles.css" rel="stylesheet"/>
	<link href="${pageContext.request.contextPath}/resources/css/myaccount.css" rel="stylesheet"/>
	<link rel="icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
	<link rel="icon" href="${pageContext.request.contextPath}/resources/images/icon.svg">
	<link rel="apple-touch-icon" href="${pageContext.request.contextPath}/resources/images/apple-touch-icon.png">
</head>
<body class="body">
	<jsp:include page="components/customNavBar.jsp"/>
	<div class="content-container-transparent">
		<jsp:include page="components/siteHeader.jsp">
			<jsp:param name="code" value="account.settings.title"/>
		</jsp:include>
		<div class="main-body">
			<div class="account-sections content-container">
				<jsp:include page="components/accountOptions.jsp">
					<jsp:param name="selected" value="0"/>
				</jsp:include>
			</div>
			<div class="account-settings content-container">
				<h4 class="options-title">
					<spring:message code="account.settings.options.data"/>
				</h4>
				<hr class="divider-bar"/>
			</div>
		</div>
	</div>
	<jsp:include page="components/footer.jsp"/>
</body>
</html>
