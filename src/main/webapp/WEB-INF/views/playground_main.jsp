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

<script type="text/javascript">
  $( document ).ajaxStart(function() {
    $( "#loading" ).show();
  });
  $( document ).ajaxStop(function() {
    $( "#loading" ).show();
  });
</script>

<script type="text/javascript">
    // replaceAll 함수
    function replaceAll(str, searchStr, replaceStr) {
        return str.split(searchStr).join(replaceStr);
    }

    $(".press").keydown(function (e) {
       if(e.keyCode==13) {
           $("button[name='refreshBtnNM']").click();
       }
    });

    $(document).on("click", "button[name='refreshBtnNM']", function() {
		console.log("JH : Click..");
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
                console.log('JH : result = '+JSON.stringify(result));
            },
            error       :	function(request, status, error) {
                console.log("textStatus: "+status+", error: "+error);
                alert("ERROR!");
            }
        });
    });

</script>

<!-- Header -->
<header class="masthead">
  <div class="container d-flex h-100 align-items-center">
    <div class="mx-auto text-center">
      <!-- <h1 class="mx-auto my-0 text-uppercase"><font color="blue">BlueOctober</font></h1> -->
      <h2 class="text-white-50 mx-auto mt-2 mb-4">Blue October</h2>
      <!-- <button type="button" class="btn btn-primary js-scroll-trigger btn-lg" id="refreshBtn" name="refreshBtnNM">Refresh</button> -->
    </div>
  </div>
</header>

<section class="page-section" id="services">
    <div class="container">
        <div class="text-center">
            <h2 class="section-heading text-uppercase">Services</h2>
            <h3 class="section-subheading text-muted">로또 당첨 예상번호 조회 서비스</h3>
            <button type="button" class="btn btn-primary js-scroll-trigger btn-lg" id="refreshBtn" name="refreshBtnNM">Refresh</button>
        </div>
        <div class="row text-center">
            <div class="col-md-4">
                <h4 class="my-3">10 10 10 10 10</h4>
                <!-- <p class="text-muted">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Minima maxime quam architecto quo inventore harum ex magni, dicta impedit.</p> -->
            </div>
        </div>
    </div>
</section>