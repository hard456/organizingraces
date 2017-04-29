function importTeams(raceId, size) {
    if (document.getElementById("my-file-selector").files.length == 0) {
        console.log("no files selected");
    }
    else {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        var url;
        if(size == 1){
            url = "/race/" + raceId + "/importOnePersonTeam"
        }
        else{
            url = "/race/" + raceId + "/importTeams";
        }

        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });

        var formData = new FormData();
        if ($('input[type=file]')[0].files[0].size > 1000000) {
            data = '<div class="alert alert-danger">Error: file size > 1MB</div>';
            $('#importResult').html(data);
            $('#importTeams').modal('hide');
        }
        else {
            $('#loader').css("display", "block");
            formData.append('file', $('input[type=file]')[0].files[0]);

            $.ajax({
                type: "POST",
                url: BASE_URL+url,
                data: formData,
                contentType: false,
                processData: false,
                success: function (response) {
                    if (response.localeCompare("ok") == 0) {
                        data = '<div class="alert alert-success">Excel file successfully imported. REFRESH PAGE!</div>';
                    }
                    else {
                        data = '<div class="alert alert-danger">Error: ' + response + '</div>';
                    }
                    $('#importResult').html(data);
                    $('#importTeams').modal('hide');
                    $('#loader').css("display", "none");
                }
            });

        }
    }
}