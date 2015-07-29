<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="script/jquery-1.11.2.js"></script>
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=cd55379ff7d4747ed9bfb729cccac824"></script>
<link type="text/css" href="${pageContext.request.contextPath }/style/map.css" rel="stylesheet"/>
<link type="text/css" href="${pageContext.request.contextPath }/style/weather.css" rel="stylesheet"/>
<title>天气查询</title>
</head>
<body>

<div id="mapContainer" class="mapContainer"></div>

<div id="realtimeWeather" class="realtimeWeather"></div>

<div class="weather">
<span>查询城市天气:</span>
<form  id="weatherForm" class="weatherForm" method="get">
<input type="text" name="queryCity" id="queryCity" class="queryCity" style="width:300px;height:30px" placeholder="西安市"/>
<input type="button" id="button" class="button" style="width:50px;height:30px" value="查询"/>
</form>
</div>

<div id="weatherResult" class="weatherResult"></div>

</body>

<!--ajax请求weather信息-->
<script type="text/javascript">
	$("#button").click(function(){
		$.get("${pageContext.request.contextPath}/weatherServlet", $("#weatherForm").serializeArray(),
		function(data, statusText){
			$("#weatherResult").empty();
			$("#weatherResult").append(data);
		},"html");
	});	
</script>

<!--定时请求当前温度信息-->
<script type="text/javascript">
	setInterval(function() {
		$("#realtimeWeather").load("realtimeWeatherServlet");
	}, 3000); //每3000秒请求一次最新天气
</script>

<!--加载地图-->
<script type="text/javascript">
	var map = new AMap.Map('mapContainer',{
	    view:new AMap.View2D({
	      center:new AMap.LngLat(108.90272856, 34.3685904),//设置中心点
	      zoom:18
	    }),
	    zooms:[8,18]  //设置缩放级别
	});
  
	var marker = new AMap.Marker({
		map:map,
		content:"<font color='red'>我在这做课设</font>"
	});
</script>


</html>