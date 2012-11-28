<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<!--[if lt IE 7 ]>
    <html class="ie6 no-js oldie">
<![endif]-->
<!--[if IE 7 ]>
    <html class="ie7 no-js oldie">
<![endif]-->
<!--[if IE 8 ]>
    <html class="ie8 no-js oldie">
<![endif]-->
<!--[if IE 9 ]>
    <html class="ie9 no-js">
<![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html class="no-js" lang="ko">
<!--<![endif]-->
<head>
<script language="JavaScript">
function scrollMap(position)
{
  var la = position.coords.latitude; //위도 불러오는곳
  var lo = position.coords.longitude; // 경도 불러우는곳
  var latlng = new google.maps.LatLng(la, lo);
  
	var address2 = "찾는중....."
	var lat2 = la;
	var lng2 = lo;
	var gpstext2 = "위도 : " + lat2 + " 경도: " + lng2 + " 주소 : " +  address2  + '\r\n';;
	$('#gps').html(gpstext2);
  
  var myOptions = {
		  zoom : 16,
		  center : latlng,
		  mapTypeId : google.maps.MapTypeId.ROADMAP
	}
	var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	var marker = new google.maps.Marker({
		position : latlng, 
		map : map
	});
	
	var geocoder = new google.maps.Geocoder();
	
	google.maps.event.addListener(map, 'click', function(event) 
			{
		var location = event.latLng;
		geocoder.geocode({
			'latLng' : location
		},
		function(results, status){
			if( status == google.maps.GeocoderStatus.OK )
			{	    			
				var address = results[0].formatted_address;
				var lat = results[0].geometry.location.lat();
				var lng = results[0].geometry.location.lng();
				var gpstext = "위도 : " + lat + " 경도: " + lng + " 주소 : " +  address + '\r\n';
				
				var gpstexttemp = $('#gps').val();
				var gpstext3 = gpstext3 + gpstext + gpstexttemp;
				
				$('#gps').html(gpstexttemp + gpstext);
				
			}
			else {
				alert("Geocoder failed due to: " + status);
			}
		});
		if( !marker ) {
			marker = new google.maps.Marker({
				position : location, 
				map : map
			});
		}
		else {
			marker.setMap(null);
			marker = new google.maps.Marker({
				position : location, 
				map : map
			});
		}
		map.setCenter(location);
	});
}
</script>
<script src="http://maps.google.com/maps/api/js?v=3.5&sensor=true"></script>
</head>
<body>
<td colspan="2"><div id="map_canvas" style="width: 800px; height: 400px;"></div></td>
</body>
</html>




