<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<div class="site-header">
    <spring:message code="${param.code}" var="titleString"/>
    <h3>
        ${titleString}
    </h3>

    <svg xmlns="http://www.w3.org/2000/svg" width="1178.008" height="500" viewBox="0 0 1178.008 500">
        <g transform="translate(56.008)">
            <rect width="1178.008" height="500" transform="translate(-56.008)" fill="#485696"/>
            <path d="M101.532,129a74.492,74.492,0,1,1-74.266,74.492A74.492,74.492,0,0,1,101.532,129Z" fill="#fc7a27"
                  fill-rule="evenodd"/>
            <circle cx="52.062" cy="52.062" r="52.062" transform="translate(17.938 247.703)" fill="#f24c00"/>
            <path d="M6.6,177.931a62.6,62.6,0,1,1-.005,0Z" fill="#fcb839" fill-rule="evenodd"/>
            <circle cx="28.484" cy="28.484" r="28.484" transform="translate(139.032 203.594)" fill="#f24c00"/>
            <circle cx="37.5" cy="37.5" r="37.5" transform="translate(101.531 237.781)" fill="#fcb839"/>
        </g>
    </svg>
</div>
