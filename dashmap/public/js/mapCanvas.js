function mapping(offense,incidents,time,result) {
	document.getElementById('mapcontain').innerHTML="<div id='map'></div>";
	var map = new L.Map("map", {center: [32.740, -96.889], zoom: 12})
		.addLayer(new L.TileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"));
	
	var toLine = d3.svg.line()
			.interpolate("linear")
			.x(function(d) {
				return applyLatLngToLayer(d).x
			})
			.y(function(d) {
				return applyLatLngToLayer(d).y
			});

	function applyLatLngToLayer(d) {
			var y = d.lat
			var x = d.lon
			return map.latLngToContainerPoint(new L.LatLng(y, x))
		}
    L.canvasOverlay()
		.drawing(drawingOnCanvas)
		.addTo(map);
	
	function drawingOnCanvas(canvasOverlay, params){
		var ctx = params.canvas.getContext('2d');
            ctx.clearRect(0, 0, params.canvas.width, params.canvas.height);
            ctx.fillStyle = "None";
		
		var detachedContainer = document.createElement("custom");
		var dataContainer = d3.select(detachedContainer);
		
		var data=[]
		
		
		d3.json("data/streetlevelbigroads.json", function(error, collection) {
			if (error) throw error;
			
			collection.forEach(function (d){
				d.path.forEach(function (t){
					count=0;
					t.incidents.forEach(function (incident){
						if ((in_array(incident["offense"],offense))&&(in_array(incident["time"],time))&&(in_array(incident["type"],incidents))&&(in_array(incident["result"],result))){
							count=count+1;
						}
						
					});
					t.count=count;
				});
			});
			
		
		
		
		
		draw();
		map.on("viewreset", reset);
		reset();
		function reset() {
		for (var i=0;i<collection.length;i++){
			var ss=dataContainer.selectAll(".path"+i)
					
			
					
		}
		
	}
		drawCanvas();
		

		function draw(){
			mark=0
			var features = dataContainer.selectAll(".road")
				.data(collection)
				.enter()
				.append("g")
				.attr("id",function (d,i){
					return "road"+i
				})
				.attr("class",function (d){
					if (d.name!=""){
						var reg=new RegExp(" ","g"); 
						return d.name.replace(reg,"_")+" road";
					} else {
						return "road";
					}
				});

			paths=[]
			path=[]
			for (var i=0;i<collection.length;i++){
				var temp=dataContainer.select("#road"+i);
				
				var tt=temp.selectAll(".path"+i)
					.data(collection[i].path)
					.enter()
					.append("path")
					.attr("class",function (){
						var reg=new RegExp(" ","g");
						var nameclass=collection[i].name.replace(reg,"_");
						return nameclass+" path path"+i;
					})
					.attr("d",function (d){return toLine(d.points)});
					
				for (var j=0;j<tt[0].length;j++){
					path.push(tt[0][j])
				}
			}
			paths.push(path);
			
		}
		function drawCanvas(){
			var fakeelements = dataContainer.selectAll(".path");
			fakeelements.each(function(d) {
				var node = d3.select(this);
				styleS=node.attr("style");

				ctx.beginPath();
				ctx.lineWidth = 2;
				if (d.count>20){
					color="red";
				} else if (d.count>3){
					color="yellow";
				} else {
					color="green";
				}
				ctx.strokeStyle = color;
				var p = new Path2D(node.attr("d"));
				console.log();
				ctx.stroke(p);
				ctx.closePath();

		  });
		}
		});
	}
}

function in_array(stringToSearch, arrayToSearch) {
 for (s = 0; s < arrayToSearch.length; s++) {
  thisEntry = arrayToSearch[s].toString();
  if (thisEntry == stringToSearch) {
   return true;
  }
 }
 return false;
}
