function importTeams(raceId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var formData = new FormData();
    formData.append('file', $('input[type=file]')[0].files[0]);

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/importTeams",
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
            //console.log(response);
            $('#importTeams').modal('hide');
        }
    });
}