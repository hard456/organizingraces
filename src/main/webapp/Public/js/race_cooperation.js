function addCooperator(race_id) {

    var form = $("#addCooperatorForm").serialize();
    var tmp;
    $.ajax({
        type: "POST",
        url: "/race/" + race_id + "/addCooperator",
        data: form,
        dataType: "json",
        success: function (response) {
            console.log(response.validation);
            if (response.validation.localeCompare("ok") == 0) {
                tmp = "<div class='alert alert-success'>The cooperator was added.</div>";
                $("#deleteCooperatorForm").append('<div id="C' + response.user.id + '">' + '<hr>' +'<div class="row">' +
                    '<div class="col-sm-9" name="login">' + response.user.login +
                    '</div>' +
                    '<div class="col-sm-3" style="text-align: right;">' +
                    '<input id="U'+response.user.id+'" type="button" class="btn btn-danger" onclick="deleteCooperator(' + race_id + ', \'' + response.user.login + '\');" value="Delete cooperator">' +
                    '</div>' +
                    '</div>' +
                    '</div>');
            }
            if (response.validation.localeCompare("fail") == 0) {
                tmp = '<div class="alert alert-danger">Something went wrong.</div>'
            }
            if (response.validation.localeCompare("wrongname") == 0) {
                tmp = '<div class="alert alert-danger">The user is not exist.</div>'
            }
            if (response.validation.localeCompare("owner") == 0) {
                tmp = '<div class="alert alert-danger">You can not add yourself.</div>'
            }
            if (response.validation.localeCompare("iscooperator") == 0) {
                tmp = '<div class="alert alert-danger">Theuser is already cooperator.</div>'
            }

            $('#add_cooperator_result').html(tmp);
            $('#addCooperatorForm').get(0).reset();
        }
    });
}

function deleteCooperator(race_id, login) {

    //nutnost pro poslání CSRF
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var form = {login:login};

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + race_id + "/deleteCooperator",
        data: form,
        dataType: "json",
        success: function (response) {
            if (response != -1) {
                $('#C' + response).remove();
            }
        }
    });

}