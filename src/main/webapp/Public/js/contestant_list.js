function changePaidValue(race_id,contestant){

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {contestantId:contestant};

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + race_id + "/contestants/changePaidValue",
        data: data,
        dataType: "html",
        success: function (response) {
                if(response == 1){
                    $("#P"+contestant).css("background-color","#5cb85c");
                    $("#P"+contestant).attr("value","YES");
                }
                if(response == 0){
                    $("#P"+contestant).css("background-color","#d9534f");
                    $("#P"+contestant).attr("value","NO");
                }
        }
    });

}