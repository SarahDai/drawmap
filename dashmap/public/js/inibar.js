function newNavi() {
    $(document).ready(function () {


        offenseNavi();
        incidentNavi();
        timeNavi();
        resultNavi();
        $(".All")
            .toggleClass("active");

    });
}

function incidentNavi() {
    var offenseIncidentType = returncrime();
    var otype = $("<li></li>");
    var otypeul = $("<ul></ul>")
        .attr("id", "incidentTypeul");
    otype.attr("id", "inciType")
        .append(function () {
            return $("<a></a>")
                .text("Incident Type")
                .append(function () {
                    return $("<i></i>")
                        .attr("class", "fa fa fa-fire leftNavIcon");
                });
        })
        .append(otypeul);

    offenseIncidentType.forEach(function (d) {

        var li = $("<li></li>")
            .attr("class", "offenseCrime " + d)
            .attr("id", "offenseCrime" + d)
            .append(function () {
                return $("<a></a>")
                    .text(d)
                    .attr("class", "offenseCrime")
                    .append(function () {
                        return $("<i></i>")
                            .attr("class", "fa fa-angle-right leftNavIcon");
                    });
            });
        li.appendTo(otypeul);
    });

    $("#leftNavigation").append(otype);


}

function offenseNavi() {
    var offenseType = returnoffense();
    var otype = $("<li></li>");
    var otypeul = $("<ul></ul>")
        .attr("id", "offenseTypeul");

    otype.attr("id", "otype")
        .append(function () {
            return $("<a></a>")
                .text("Offense Type")
                .append(function () {
                    return $("<i></i>")
                        .attr("class", "fa fa-balance-scale leftNavIcon");
                });
        })
        .append(otypeul);

    offenseType.forEach(function (d) {

        var li = $("<li></li>")
            .attr("class", "offense " + d)
            .attr("id", "offense" + d)
            .append(function () {
                return $("<a></a>")
                    .text(d)
                    .attr("class", "offense")
                    .append(function () {
                        return $("<i></i>")
                            .attr("class", "fa fa-angle-right leftNavIcon");
                    });
            });
        li.appendTo(otypeul);
    });

    $("#leftNavigation").append(otype);
}

function timeNavi() {
    var time = returntime();
    var otype = $("<li></li>");
    var otypeul = $("<ul></ul>")
        .attr("id", "timeul");
    otype.attr("id", "otype")
        .append(function () {
            return $("<a></a>")
                .text("Time")
                .append(function () {
                    return $("<i></i>")
                        .attr("class", "fa fa-calendar-check-o leftNavIcon");
                });
        })
        .append(otypeul);

    time.forEach(function (d) {

        var li = $("<li></li>")
            .attr("class", "time " + d)
            .attr("id", "time" + d)
            .append(function () {
                return $("<a></a>")
                    .text(d)
                    .attr("class", "time")
                    .append(function () {
                        return $("<i></i>")
                            .attr("class", "fa fa-angle-right leftNavIcon");
                    });
            });
        li.appendTo(otypeul);
    });

    $("#leftNavigation").append(otype);
}

function resultNavi() {
    var result = returnresult();
    var otype = $("<li></li>");
    var otypeul = $("<ul></ul>")
        .attr("id", "resultul");
    otype.attr("id", "otype")
        .append(function () {
            return $("<a></a>")
                .text("Result")
                .append(function () {
                    return $("<i></i>")
                        .attr("class", "fa fa-level-down leftNavIcon");
                });
        })
        .append(otypeul);

    result.forEach(function (d) {

        var li = $("<li></li>")
            .attr("class", "result " + d)
            .attr("id", "result" + d)
            .append(function () {
                return $("<a></a>")
                    .text(d)
                    .attr("class", "result")
                    .append(function () {
                        return $("<i></i>")
                            .attr("class", "fa fa-angle-right leftNavIcon");
                    });
            });
        li.appendTo(otypeul);
    });

    $("#leftNavigation").append(otype);
}


function returncrime() {
    return ["Violent Crime", "Property Crime", "Traffic Accident", "Prostitution", "Liquor&Drugs", "Property Found", "Accident", "Others", "All"]
}

function returnoffense() {
    return ["Offense", "No Offense", "All"];

}

function returntime() {
    return ["midnight", "before dawn", "morning", "noon", "afternoon", "evening", "night", "All"];
}

function returnresult() {
    return ["Clear by Arrest", "Clear by Exceptional Arrest", "Closed/Cleared", "Open", "Returned for Correction", "Suspended", "All"];
}