function mapping(map, offense, incidents, time, result) {

    var svg = d3.select("#map").select("svg");

    var g = svg.append("g")
        .attr("id", "great");


    /**
     *
     * Taxi ID, Time, Latitude, Longitude,
     * Occupancy Status, Speed;
     * Occupancy Status: 1-with passengers & 0-with passengers;
     *
     * 22223,21:09:38,114.138535,22.609266,1,19
     * 22223,11:14:18,114.137871,22.575317,0,0
     * 22223,01:18:28,114.137131,22.575983,0,0
     * 22223,13:11:42,114.136269,22.545851,1,18
     * 22223,02:05:47,114.135948,22.578917,0,29
     * 22223,02:05:24,114.135834,22.577433,0,20
     * 22223,11:37:32,114.135681,22.566566,1,59
     */

    // proj4.defs("EPSG:2276", "+proj=lcc +lat_1=33.96666666666667 +lat_2=32.13333333333333 +lat_0=31.66666666666667 +lon_0=-98.5 +x_0=600000 +y_0=2000000.0001016 +ellps=GRS80 +datum=NAD83 +to_meter=0.3048006096012192 +no_defs");
    // proj4.defs("EPSG:4326", "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");

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

        // road 从哪里来的?
        g.selectAll(".road").remove();

        var file = "data/streetlevelbigroads.json";
        if (map.getZoom() >= 15) {
            file = "data/streetlevel.json";
        }

        // process data
        d3.json(file, function (error, collectionA) {

            if (error) throw error;

            // 一个指定了 southwest 和 northeast 的矩形
            var bounds = map.getBounds();

            var collection = [];

            collectionA.forEach(function (d) {
                // road = [];
                road = {};
                road.type = d.type;
                road.name = d.name;
                road.path = [];

                // 统计在那个范围内的点
                d.path.forEach(function (t) {

                    var count = 0;

                    if (anyin(t.points, bounds)) {
                        t.incidents.forEach(function (incident) {
                            if (((in_array(incident["offense"], offense))
                                || (in_array("All", offense))) && (in_array(incident["time"], time)
                                || in_array("All", time)) && (in_array(incident["type"], incidents)
                                || in_array("All", incidents)) && (in_array(incident["result"], result)
                                || in_array("All", result))) {
                                count = count + 1;
                            }
                        });
                        road.path.push(t);
                    }
                    t.count = count;

                });

                // 重新设置过 path 放入 collection 中
                if (road.path.length > 0) {
                    collection.push(road);
                    // console.log(collection[0]);
                }
            });

            // draw a line based on the longitude and latitude
            var toLine = d3.svg.line()
                .interpolate("linear")
                .x(function (d) {
                    console.log('executed...')
                    return applyLatLngToLayer(d).x
                })
                .y(function (d) {
                    return applyLatLngToLayer(d).y
                });

            // 返回地图上的那个点
            function applyLatLngToLayer(d) {
                var y = d.lat
                var x = d.lon
                return map.latLngToLayerPoint(new L.LatLng(y, x))
            }

            draw();

            for (var i = 0; i < collection.length; i++) {
                var ss = d3.selectAll(".path" + i)
                    .attr("d", function (d) { // 获得 path 中的数据
                        // path 中的一个个经纬度传递进去
                        return toLine(d.points)
                    });
            }

            function draw() {

                var mark = 0;

                var wid = 2;
                if (map.getZoom() > 15) {
                    wid = map.getZoom() - 13;
                }

                var features = g.selectAll(".road")
                    .data(collection)
                    .enter()
                    .append("g")
                    .attr("id", function (d, i) {
                        return "road" + i; // 在这添加了 road 的 id
                    })
                    .attr("class", function (d) {
                        // 给 road 添加上属性
                        if (d.name != "") {
                            // 全局 match
                            var reg = new RegExp(" ", "g");
                            // match 的空格替换为下划线
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

                var paths = [];
                var path = [];

                // build path data
                for (var i = 0; i < collection.length; i++) {
                    var temp = g.select("#road" + i);

                    var tt = temp.selectAll(".path" + i)
                        .data(collection[i].path) // 之前的 road 里面存放了 path
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
        var thisEntry = arrayToSearch[s].toString();
        if (thisEntry === stringToSearch) {
            return true;
        }
    }
    return false;
}

function anyin(points, bounds) {
    var st = false;
    points.forEach(function (d) {

        // po = new L.LatLng(d.lat, d.lon);
        var po = new L.LatLng(d.lat, d.lon);
        if (bounds.contains(po)) {
            st = true;
        }
    });
    return st;

}