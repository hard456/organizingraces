function adminTeamRegistrationAjax(race_id) {

    var form = $("#adminTeamRegistrationForm").serialize();

    $.ajax({
        type: "POST",
        url: BASE_URL+"/race/" + race_id + "/adminTeamRegistration",
        data: form,
        async: false,
        dataType: "html",
        success: function (data) {
            if (data.localeCompare("ok") == 0) {
                data = '<div class="alert alert-success">The team was registered.</div>'
            }
            if (data.localeCompare("fail") == 0) {
                data = '<div class="alert alert-danger">Something went wrong.</div>'
            }
            if (data.localeCompare("invalid") == 0) {
                data = '<div class="alert alert-danger">' +
                    'Data are invalid:<br>Firstname (3 - 32 length)<br>Lastname (3 - 32 length)<br>Phone (123456789, 123 456 789, +420123456789, +420 123 456 789)' +
                    'Team name (3 - 32 length)' +
                    '</div>'
            }

            $('#admin_team_result').html(data);
            $('#adminTeamRegistrationForm').get(0).reset();
        }
    });
}


function adminSoloRegistration(race_id) {
    var form = $("#adminSoloContestantForm").serialize();

    $.ajax({
        type: "POST",
        url: BASE_URL+"/race/" + race_id + "/adminSoloRegistration",
        data: form,
        async: true,
        dataType: "html",
        success: function (data) {
            if (data.localeCompare("ok") == 0) {
                data = '<div class="alert alert-success">The user was registered.</div>'
            }
            if (data.localeCompare("fail") == 0) {
                data = '<div class="alert alert-danger">Something went wrong.</div>'
            }
            if (data.localeCompare("invalid") == 0) {
                data = '<div class="alert alert-danger">' +
                    'Data are invalid:<br>Firstname (3 - 32 length)<br>Lastname (3 - 32 length)<br>Phone (123456789, 123 456 789, +420123456789, +420 123 456 789)' +
                    '</div>'
            }
            $('#admin_solo_result').html(data);
            $('#adminSoloContestantForm').get(0).reset();

        },

    });
}