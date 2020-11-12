<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="azurepassion" />
<title>JH's Playground</title>
<link rel="icon" type="image/x-icon" href="assets/img/favicon.ico" />
<!-- Font Awesome icons (free version)-->
<script src="https://use.fontawesome.com/releases/v5.13.0/js/all.js" crossorigin="anonymous"></script>
<!-- Google fonts-->
<link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css" />
<link href="https://fonts.googleapis.com/css?family=Droid+Serif:400,700,400italic,700italic" rel="stylesheet" type="text/css" />
<link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700" rel="stylesheet" type="text/css" />
<!-- Core theme CSS (includes Bootstrap)-->
<link href="/resources/bootstrap/css/styles.css" rel="stylesheet" />
<!-- Bootstrap core JS-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"></script>
<!-- Third party plugin JS-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min.js"></script>
<!-- Contact form JS-->
<script src="/resources/bootstrap/assets/mail/jqBootstrapValidation.js"></script>
<script src="/resources/bootstrap/assets/mail/contact_me.js"></script>
<!-- Core theme JS-->
<script src="/resources/bootstrap/js/scripts.js"></script>
<link type="text/css" href="/resources/css/loading.css" rel="stylesheet" />


<script type="text/javascript">
$(document).ajaxStart(function () {
	$("#loadingDiv").show() 
}); 
$(document).ajaxStop(function () {
	$("#loadingDiv").hide()
});
</script>

<script type="text/javascript">
	//F12 버튼 방지
	$(document).ready(function() {
		$(document).bind('keydown',function(e) {
			if (e.keyCode == 123 /* F12 */) {
				e.preventDefault();
				e.returnValue = false;
			}
		});
	});
	// 우측 클릭 방지
	document.onmousedown=disableclick;
	status="Right click is not available.";
	function disableclick(event) {
		if (event.button==2) {
			alert(status);
			return false; 
		}
	}

	//모바일 여부
	var isMobile = false;
	// PC 환경
	var filter = "win16|win32|win64|wince|mac|macintel|mac68k|macppc|linux i686|hp-ux|sunos";
	if (navigator.platform) {
		var os = navigator.platform.toLowerCase();
	    isMobile = filter.indexOf(navigator.platform.toLowerCase()) < 0;
	}

    function init() {
    	Object.defineProperty(console, '_commandLineAPI', { get : function() { throw '콘솔을 사용할 수 없습니다.' } });
    	
        
        var params = {
            page : "main",
        }
        $.ajax({
            url			:	"/getPredictationNumber",
            type 		:	"post",
            dataType	:	"json",
            contentType :	"application/x-www-form-urlencoded; charset=UTF-8",
            data		:	params,
            success 	:	function(result) {
            	var predictionNumberAreaResult = "";
                for(var i=0; i<result.length; i++) {
                	predictionNumberAreaResult += result[i].drwt_no1+"&nbsp; "+result[i].drwt_no2+"&nbsp; "+result[i].drwt_no3+"&nbsp; "+result[i].drwt_no4+"&nbsp; "+result[i].drwt_no5+"&nbsp; "+result[i].drwt_no6+"&nbsp; ";
					if(i!=(result.length-1)) {
						predictionNumberAreaResult += "<br><hr>";
					}
				}
                $("#predictionNumberArea").html(predictionNumberAreaResult);
                document.getElementById("predictionNumberArea").style.color = "#008B8B";
                                
            },
            error       :	function(request, status, error) {
                console.log("textStatus: "+status+", error: "+error);
                alert("ERROR!");
            }
        });
    }

 	// replaceAll 함수
    function replaceAll(str, searchStr, replaceStr) {
        return str.split(searchStr).join(replaceStr);
    }

    function isBrowserCheck() { 
    	const agt = navigator.userAgent.toLowerCase(); 
    	if (agt.indexOf("chrome") != -1) return 'Chrome'; 
    	if (agt.indexOf("opera") != -1) return 'Opera'; 
    	if (agt.indexOf("staroffice") != -1) return 'Star Office'; 
    	if (agt.indexOf("webtv") != -1) return 'WebTV'; 
    	if (agt.indexOf("beonex") != -1) return 'Beonex'; 
    	if (agt.indexOf("chimera") != -1) return 'Chimera'; 
    	if (agt.indexOf("netpositive") != -1) return 'NetPositive'; 
    	if (agt.indexOf("phoenix") != -1) return 'Phoenix'; 
    	if (agt.indexOf("firefox") != -1) return 'Firefox'; 
    	if (agt.indexOf("safari") != -1) return 'Safari'; 
    	if (agt.indexOf("skipstone") != -1) return 'SkipStone'; 
    	if (agt.indexOf("netscape") != -1) return 'Netscape'; 
    	if (agt.indexOf("mozilla/5.0") != -1) return 'Mozilla'; 
    	if (agt.indexOf("msie") != -1) { 
        	let rv = -1; 
    		if (navigator.appName == 'Microsoft Internet Explorer') { 
    			let ua = navigator.userAgent; var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})"); 
    		if (re.exec(ua) != null) 
    			rv = parseFloat(RegExp.$1); 
    		} 
    		return 'Internet Explorer '+rv; 
    	} 
    }

    init();
    

</script>

<!-- Header -->
<header class="masthead" oncontextmenu='return false' onselectstart='return false' ondragstart='return false'>
  <div class="container d-flex h-100 align-items-center">
    <div class="mx-auto text-center">
      <!-- <h1 class="mx-auto my-0 text-uppercase"><font color="blue">BlueOctober</font></h1> -->
      <h2 class="text-white-50 mx-auto mt-2 mb-4">Blue October</h2>
      <!-- <button type="button" class="btn btn-primary js-scroll-trigger btn-lg" id="refreshBtn" name="refreshBtnNM">Refresh</button> -->
      <!-- <button type="button" class="btn btn-primary js-scroll-trigger btn-lg" id="getTestLogicResult" name="getTestLogicResult">Refresh</button> -->
    </div>
  </div>
</header>

<section class="page-section" id="services">
    <div class="container">
        <div class="text-center">
            <h2 class="section-heading text-uppercase">Services</h2>
            <h3 class="section-subheading text-muted">로또 당첨 예상번호 조회 서비스</h3>
            <!-- <button type="button" class="btn btn-primary js-scroll-trigger btn-lg" id="refreshBtn" name="refreshBtnNM">Refresh</button> -->
        </div>
        <div class="row text-center">
			<div class="col-md-12">
                <h3 class="my-3"><span id="recommendationNum" style="color:#DDA0DD;">이번주 추천번호</span></h3>
                <!-- <p class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Minima maxime quam architecto quo inventore harum ex magni, dicta impedit.</p> -->
            </div>
            <div class="col-md-12">
                <h4 class="my-3"><span id="predictionNumberArea"></span></h4>
                <!-- <p class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Minima maxime quam architecto quo inventore harum ex magni, dicta impedit.</p> -->
            </div>
        </div>
        
        <c:if test="${isMobile != false}">
        	<div class="row text-center">
	        	<div class="col-md-12">
        			<div class="alert alert-warning">
						<strong>모바일 디바이스에 최적화 되어있습니다.^^</strong>
					</div>
				</div> 
	        </div>
        </c:if>
        <c:if test="${isMobile == false}">
	        <div class="row text-center">
	        	<div class="col-md-12">
	        		<p class="text-muted">It is optimized for mobile device screens.</p>
	        	</div> 
	        </div>
        </c:if>
    </div>
</section>


<div id="loadingDiv">
	<img id="loadingDiv-image" src="/resources/img/loading.gif">
</div>



<!-- <div class="alert alert-warning alert-dismissible fade show" role="alert">
  <strong>Holy guacamole!</strong> You should check in on some of those fields below.
  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
    <span aria-hidden="true">&times;</span>
  </button>
</div> -->