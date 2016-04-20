function mapping(map, offense, incidents, time, result) {


    var svg = d3.select("#map").select("svg");

    var g = svg.append("g")
        .attr("id", "great");


    proj4.defs("EPSG:2276", "+proj=lcc +lat_1=33.96666666666667 +lat_2=32.13333333333333 +lat_0=31.66666666666667 +lon_0=-98.5 +x_0=600000 +y_0=2000000.0001016 +ellps=GRS80 +datum=NAD83 +to_meter=0.3048006096012192 +no_defs");
    proj4.defs("EPSG:4326", "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");

    var tip = d3.tip()
        .attr('class', 'd3-tip')
        .offset([0, 0])
        .html(function (d) {
            return "<p style='line-height: 30%'><strong>" + d.name + "</strong></p>";
        });
    g.call(tip);


    map.on("moveend", reset);
    reset();
    function reset() {

        g.selectAll(".road")
            .remove();
        var file = "data/streetlevelbigroads.json";
        if (map.getZoom() >= 15) {
            file = "data/streetlevel.json";

        }
        d3.json(file, function (error, collectionA) {
            if (error) throw error;
            bounds = map.getBounds();

            collection = []

            collectionA.forEach(function (d) {
                road = [];
                road.type = d.type;
                road.name = d.name;
                road.path = [];
                d.path.forEach(function (t) {
                    count = 0;
                    if (anyin(t.points, bounds)) {
                        t.incidents.forEach(function (incident) {
                            if (((in_array(incident["offense"], offense)) || (in_array("All", offense))) && (in_array(incident["time"], time) || in_array("All", time)) && (in_array(incident["type"], incidents) || in_array("All", incidents)) && (in_array(incident["result"], result) || in_array("All", result))) {
                                count = count + 1;
                            }
                        });
                        t.count = count;
                        road.path.push(t);
                    }


                });

                if (road.path.length > 0) {
                    collection.push(road);
                }
            });

            console.log(collection.length);

            var toLine = d3.svg.line()
                .interpolate("linear")
                .x(function (d) {
                    return applyLatLngToLayer(d).x
                })
                .y(function (d) {
                    return applyLatLngToLayer(d).y
                });

            function applyLatLngToLayer(d) {
                var y = d.lat
                var x = d.lon
                return map.latLngToLayerPoint(new L.LatLng(y, x))
            }

            draw();

            for (var i = 0; i < collection.length; i++) {
                var ss = d3.selectAll(".path" + i)
                    .attr("d", function (d) {
                        return toLine(d.points)
                    });

            }

            function draw() {
                mark = 0

                var wid = 2;
                if (map.getZoom() > 15) {
                    wid = map.getZoom() - 13;
                }
                var features = g.selectAll(".road")
                    .data(collection)
                    .enter()
                    .append("g")
                    .attr("id", function (d, i) {
                        return "road" + i
                    })
                    .attr("class", function (d) {
                        if (d.name != "") {
                            var reg = new RegExp(" ", "g");
                            return d.name.replace(reg, "_") + " road";
                        } else {
                            return "road";
                        }
                    })
                    .on("mouseover", function (d) {
                        if (d.name != "") {
                            tip.show(d);
                            var reg = new RegExp(" ", "g");
                            var nameclass = "." + d.name.replace(reg, "_");
                            d3.selectAll(nameclass)
                                .style("stroke-width", wid + 5)
                        }
                    })
                    .on("mouseout", function (d) {
                        if (d.name != "") {
                            tip.hide(d);
                            var reg = new RegExp(" ", "g");
                            var nameclass = "." + d.name.replace(reg, "_");
                            d3.selectAll(nameclass)
                                .style("stroke-width", wid)
                        }
                    });

                paths = []
                path = []


                for (var i = 0; i < collection.length; i++) {
                    var temp = g.select("#road" + i);

                    var tt = temp.selectAll(".path" + i)
                        .data(collection[i].path)
                        .enter()
                        .append("path")
                        .attr("class", function () {
                            var reg = new RegExp(" ", "g");
                            var nameclass = collection[i].name.replace(reg, "_");
                            return nameclass + " path" + i;
                        })
                        .style("fill", "none")
                        .style("stroke", function (d) {
                            if (d.count > 20) {
                                return "#c10000";
                            }
                            if (d.count > 3) {
                                return "#e8e443";
                            }
                            return "#92d69a";
                        })
                        .style("stroke-width", wid);
                    for (var j = 0; j < tt[0].length; j++) {
                        path.push(tt[0][j])
                    }
                }
                paths.push(path);
            }
        });
    }


    return this;
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

function anyin(points, bounds) {
    var st = false;
    points.forEach(function (d) {

        po = new L.LatLng(d.lat, d.lon);
        if (bounds.contains(po)) {
            st = true;
        }
    });
    return st;

}