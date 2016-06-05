/**
 * Requests to StackExchange.
 */
var lastPage = 0;

function requestMoreRows(startWithpage, pagesize, site, tagged, nottagged, intitle) {

    var page = parseInt(startWithpage) + 1;
    if (lastPage != 0) {
        page = parseInt(lastPage) + 1;
    }
    lastPage = page;

    //ajax call
    var v_url = "http://api.stackexchange.com/2.2/search";
    v_url += "?page=" + page;
    v_url += "&pagesize=" + pagesize;
    v_url += "&order=desc&sort=activity";
    v_url += "&site=" + site;
    if (tagged != null) {
        v_url += "&tagged=" + tagged;
    }
    if (nottagged != 'null') {
        v_url += "&nottagged=" + nottagged;
    }
    if (intitle != 'null') {
        v_url += "&intitle=" + intitle;
    }

    console.log(v_url);

    $.ajax({
        url: v_url,
        type: "GET",
        dataType: "json"
    })
        .done(function (data, textStatus, jqXHR) {
            addrowsToTable(data);
        });
};

function addrowsToTable(data) {
    console.log(data);
    data.items.forEach(function(item) {
        appendRow(item);
    });

    if (!data.has_more) {
        $('#has_more').hide();
    }
}

function appendRow(item) {
    //convert date
    var date = formatDate(item.creation_date);

    var str =
    "<tr" + (item.is_answered ? ' class="success">' : '>') +
    "<td>" + date + "</td>" +
    "<td><a href=\"" + item.link + "\">" + item.title + "</a></td>" +
    "<td><a href=\"" + item.owner.link +"\">" + item.owner.display_name + "</a></td>" +
    "<td>" + item.owner.reputation + "</td>" +
    "</tr>";

    $('#result-table tbody').append(str);

}

function formatDate(in_date) {
    var utcSeconds = parseInt(in_date);
    var date = new Date(0); // The 0 there is the key, which sets the date to the epoch
    date.setUTCSeconds(utcSeconds);

    year = "" + date.getFullYear();
    month = "" + (date.getMonth() + 1); if (month.length == 1) { month = "0" + month; }
    day = "" + date.getDate(); if (day.length == 1) { day = "0" + day; }
    hour = "" + date.getHours(); if (hour.length == 1) { hour = "0" + hour; }
    minute = "" + date.getMinutes(); if (minute.length == 1) { minute = "0" + minute; }
    second = "" + date.getSeconds(); if (second.length == 1) { second = "0" + second; }
    return year + "-" + month + "-" + day + " " + hour + ":" + minute;
}


