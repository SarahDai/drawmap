/**
 * Created by JLou on 4/21/2016.
 */

function dashmap(map, offense, incidents, time, result) {

    var svg = d3.select("#map").select("svg");

    var g = svg.append("g")
        .attr("id", function () {
            return "great";
        });


    proj4.defs("EPSG:2276", "+proj=lcc +lat_1=33.96666666666667 +lat_2=32.13333333333333 +lat_0=31.66666666666667 +lon_0=-98.5 +x_0=600000 +y_0=2000000.0001016 +ellps=GRS80 +datum=NAD83 +to_meter=0.3048006096012192 +no_defs");
    proj4.defs("EPSG:4326", "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");

    map.on("moveend", render);

    render();

    function render() {

        // g.selectAll(".road").remove();

        var file = "data/testdata.json";

        d3.json(file, function (error, data) {

            if (error) throw error;

            // draw a line based on the longitude and latitude
            var line = d3.svg.line()
                .interpolate("linear")
                .x(function (d) {
                    console.log("here");
                    return applyLatLngToLayer(d).x
                })
                .y(function (d) {
                    return applyLatLngToLayer(d).y
                });

            // 返回地图上的那个点
            function applyLatLngToLayer(d) {
                var y = d.lat;
                var x = d.lon;
                return map.latLngToLayerPoint(new L.LatLng(y, x))
            }


            g.selectAll('.path')
                .data(data)
                .enter()
                .append('path')
                .style('stroke', function (d) {
                    return "#c10000";
                })
                .style("stroke-width", 20);

            g.selectAll('.path')
                .attr("d", function (d) {
                    // console.log(d);
                    return line(d);
                });
        });


    }
}