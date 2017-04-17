function changePaidValue(race_id,contestant){

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {contestantId:contestant};

    if($.isNumeric(contestant) && $.isNumeric(race_id))
    {

        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });

        $.ajax({
            type: "POST",
            url: BASE_URL+"/race/" + race_id + "/contestants/changePaidValue",
            data: data,
            dataType: "html",
            success: function (response) {
                if (response == 1) {
                    $("#P" + contestant).css("background-color", "#5cb85c");
                    $("#P" + contestant).attr("value", "YES");
                }
                if (response == 0) {
                    $("#P" + contestant).css("background-color", "#d9534f");
                    $("#P" + contestant).attr("value", "NO");
                }
            }
        });
    }
}

function updateContestant(race_id,contestant){

    var form = $("#C"+contestant).serialize();

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    //var form = {'contestan.firstname':'honziiik'};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: BASE_URL+"/race/" + race_id + "/contestants/updateContestant",
        data: form,
        dataType: "json",
        success: function (response) {
            if (response == true) {
                $("#S" + contestant).css("background-color", "#5cb85c");
            }
            else{
                $("#S" + contestant).css("background-color", "#d9534f");
                alert("Firstname (3 - 32 length\nLastname (3 - 32 length)\nEmail (6 - 32 length)\nPhone (+420123456789,789456123)");
            }

        }
    });

}