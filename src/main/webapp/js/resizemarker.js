function changeCarMarker(marker){
		
		var size;

		if(map.getZoom()>=17){
    		size = 35;
    	}
    	else if(map.getZoom() <17 && map.getZoom() > 11){
    		size = map.getZoom() * 2;
    	}
    	else if(map.getZoom() < 12){
    		size = 0;
    	}
		var image = {
				url:'https://dev1.financial-link.com.my/golaimg/driver_car.png',
				scaledSize: new google.maps.Size(size,size)
		}
		marker.setIcon(image);
}

function changeDefaultMarker(marker){
	
	var sizeW;
	var sizeH;
	var x;
	var y;
	var text = "";
	
	if(marker.getTitle() != null && marker.getTitle() != ""){
		//Assign the Label content based on the title
		if(marker.getTitle().localeCompare("Start Point") == 0){
			text = 'A';
		}
		else if(marker.getTitle().localeCompare("End Point") == 0){
			text = 'B';
		}
	}
	//Adjust the size of marker, position of label
	if(map.getZoom() >= 21){
		sizeW = 27;
		sizeH = 45;
		x = 15;
		y = 15;
		marker.setLabel(text);
	}
	else if(map.getZoom() <21 && map.getZoom() > 11){
		sizeH = (map.getZoom() * 2);
		sizeW = map.getZoom() + 5;
		x = map.getZoom() - 5;
		y = map.getZoom() - 5;
		marker.setLabel(text);
	}
	else if(map.getZoom() < 12){
		sizeW = 0;
		sizeH = 0;
		marker.setLabel("");
	}
	
	var image = {
			url:'https://maps.gstatic.com/mapfiles/api-3/images/spotlight-poi-dotless2_hdpi.png',
			scaledSize: new google.maps.Size(sizeW,sizeH),
			labelOrigin: new google.maps.Point(x,y)
	}
	marker.setIcon(image);
}