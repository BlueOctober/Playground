<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="dec" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="utf-8">
		<!-- meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/ -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title><dec:title default="xxxCompany" /></title>
		<!-- Bootstrap -->
		<link href="/resources/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css"/>
		<!-- jQuery (부트스트랩의 자바스크립트 플러그인을 위해 필요한) -->
		<script src="//code.jquery.com/jquery.js"></script>
		<!-- 모든 합쳐진 플러그인을 포함하거나 (아래) 필요한 각각의 파일들을 포함하세요 -->
		<script src="/resources/bootstrap/js/bootstrap.min.js"></script>
		<!-- Respond.js 으로 IE8 에서 반응형 기능을 활성화하세요 (https://github.com/scottjehl/Respond) -->
		<script src="/resources/bootstrap/js/respond.js"></script>
	</head>
	<body>
		<div class="container"><!-- 좌우측의 공간 확보 -->
			<!-- 헤더 들어가는 부분 -->
			<%@include file="../menu/header.jsp" %>

			<!-- 상단 메뉴 들어가는 부분 -->
			<%--<%@include file="../menu/header.jsp" %>--%>
		    <hr/>
		    	<!-- 본문 들어가는 부분 -->
				<dec:body />
				<!--// 본문 들어가는 부분 -->
		    <hr/>
		    <!-- 푸터 들어가는 부분 -->
		    <%@include file="../menu/footer.jsp" %>
			<!--// 푸터 들어가는 부분 -->
	    </div>
    </body>
</html>