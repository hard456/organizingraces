function addCooperator(race_id) {

    var form = $("#addCooperatorForm").serialize();

    $.ajax({
        type: "POST",
        url: "/race/" + race_id + "/addCooperator",
        data: form,
        async: true,
        dataType: "html",
        success: function (data) {
            if (data.localeCompare("ok") == 0) {
                data = '<div class="alert alert-success">The cooperator was added.</div>'
            }
            if (data.localeCompare("fail") == 0) {
                data = '<div class="alert alert-danger">Something went wrong.</div>'
            }
            if (data.localeCompare("wrongname") == 0) {
                data = '<div class="alert alert-danger">User is not exist.</div>'
            }
            if (data.localeCompare("owner") == 0) {
                data = '<div class="alert alert-danger">You can not add yourself.</div>'
            }
            if (data.localeCompare("iscooperator") == 0) {
                data = '<div class="alert alert-danger">User is already cooperator.</div>'
            }
            $('#add_cooperator_result').html(data);
            $('#addCooperatorForm').get(0).reset();
        }
    });
}