<%@ include file="/init.jsp" %>

<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()+"/css/banner.css"%>'>
<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()+"/css/general.css"%>'>
<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()+"/css/form.css"%>'>


<%@ include file="jsp/header/banner.jsp" %>

<body class="container">
 	<section class="row justify-content-center">
 		<article class="col-10 col-sm-10 col-md-10 col-lg-10 col-xl-10">
 			<%@ include file="jsp/body/form.jsp" %>
 		</article>
 	</section>
</body>
