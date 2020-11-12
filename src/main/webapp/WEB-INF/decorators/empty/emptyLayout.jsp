<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="dec" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title><dec:title default="BlueOctober" /></title>
		<link rel="shortcut icon" href="/resources/image/icon/favicon.ico">
		<!-- Bootstrap -->
		<link href="/resources/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css"/>
		<script src="/resources/bootstrap/js/jquery.js"></script>
		<script src="/resources/bootstrap/js/bootstrap.min.js"></script>
		<script src="/resources/bootstrap/js/respond.js"></script>
		<script src="/resources/bootstrap/js/jquery.number.min.js"></script>
	</head>
	<!--[if lte IE 8]>
		<div id="newBrowser" style="width:100%;text-align:center;background-color:#FAED7D;">Internet Explorer의 이 버전은 더 이상 지원되지 않습니다. <a href="http://browsehappy.com/" target="_blank">지원되는 브라우저</a>로 업그레이드하세요.</div>
	<![endif]-->
	<body>
		<dec:body />
    </body>
</html>