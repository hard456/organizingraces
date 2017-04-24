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
                    $("#save_result").html("<div class='alert alert-success'>Successfully saved.</div>");
            }
            else{
                $("#save_result").html("<div class='alert alert-danger'>Firstname (3 - 32 length<br>Lastname (3 - 32 length)<br>+" +
                    "Email (6 - 32 length)<br>Phone (+420123456789,789456123)</div>");
            }
            $('#updatetModal').modal('show');
        }
    });

}