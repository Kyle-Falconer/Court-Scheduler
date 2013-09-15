<%@ page import="org.optaplanner.webexamples.vehiclerouting.VrpWebAction" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>OptaPlanner webexamples: vehicle routing</title>
  <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap.css" rel="stylesheet">
  <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap-responsive.css" rel="stylesheet">
  <link href="<%=application.getContextPath()%>/website/css/optaplannerWebexamples.css" rel="stylesheet">
  <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->

  <!-- HACK to refresh this page automatically every 2 seconds -->
  <!-- TODO: it should only refresh the image -->
  <meta http-equiv="REFRESH" content="2;url=<%=application.getContextPath()%>/vehiclerouting/solving.jsp"/>
</head>
<body>

<div class="container-fluid">
<div class="row-fluid">
<div class="span2">
  <div class="benchmark-report-nav">
    <a href="http://www.optaplanner.org"><img src="../website/img/optaPlannerLogo.png" alt="OptaPlanner"/></a>
  </div>
</div>
<div class="span10">
  <header class="main-page-header">
    <h1>Vehicle routing</h1>
  </header>
  <p>Pick up all items of all customers with a few vehicles in the shortest route possible.</p>
  <p>Solving... Below is a temporary solution, refreshed every 2 seconds.</p>
  <div>
    <button class="btn" onclick="window.location.href='terminateEarly.jsp'"><i class="icon-stop"></i> Terminate early</button>
  </div>
  <img id="showSchedule" src="showSchedule.png"/>
</div>
</div>
</div>

<script src="<%=application.getContextPath()%>/twitterbootstrap/js/jquery.js"></script>
<script src="<%=application.getContextPath()%>/twitterbootstrap/js/bootstrap.js"></script>
</body>
</html>
