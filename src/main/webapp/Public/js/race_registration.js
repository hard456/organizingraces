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
                $('#adminTeamRegistrationForm').get(0).reset();
                data = '<div class="alert alert-success">The team was registered.</div>'
            }
            else if (data.localeCompare("fail") == 0) {
                data = '<div class="alert alert-danger">Something went wrong.</div>'
            }
            else if (data.localeCompare("team_exists") == 0) {
                data = '<div class="alert alert-danger">Team with this name already exists.</div>'
            }
            else if (data.localeCompare("invalid") == 0) {
                data = '<div class="alert alert-danger">' +
                    'Data are invalid:<br>Firstname (3 - 32 length)<br>Lastname (3 - 32 length)<br>Phone (123456789, 123 456 789, +420123456789, +420 123 456 789)' +
                    'Team name (3 - 32 length)' +
                    '</div>'
            }

            $('#admin_team_result').html(data);
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
                $('#adminSoloContestantForm').get(0).reset();
            }
            else if (data.localeCompare("fail") == 0) {
                data = '<div class="alert alert-danger">Something went wrong.</div>'
            }
            else if (data.localeCompare("invalid") == 0) {
                data = '<div class="alert alert-danger">' +
                    'Data are invalid:<br>Firstname (3 - 32 length)<br>Lastname (3 - 32 length)<br>Phone (123456789, 123 456 789, +420123456789, +420 123 456 789)' +
                    '</div>'
            }
            $('#admin_solo_result').html(data);

        },

    });
}

function soloRegistration(race_id) {
    var form = $("#soloContestantForm").serialize();

    $.ajax({
        type: "POST",
        url: BASE_URL+"/race/"+race_id+"/addSoloContestant",
        data: form,
        async: true,
        dataType: "html",
        success: function (data) {
            if (data.localeCompare("ok") == 0) {
                data = '<div class="alert alert-success">Registration successfully completed..</div>'
            }
            else if (data.localeCompare("something_went_wrong") == 0) {
                data = '<div class="alert alert-danger">Something went wrong.</div>'
            }
            else if (data.localeCompare("registration_disabled") == 0) {
                data = '<div class="alert alert-danger">Registration is disabled.</div>'
            }
            else if (data.localeCompare("registered_already") == 0) {
                data = '<div class="alert alert-danger">You can not register again to this race.</div>'
            }
            $('#solo_reg_result').html(data);

        }

    });
}

function teamRegistration(race_id) {
    var form = $("#teamRegistrationForm").serialize();

    $.ajax({
        type: "POST",
        url: BASE_URL+"/race/"+race_id+"/teamRegistration",
        data: form,
        async: true,
        dataType: "html",
        success: function (data) {
            if (data.localeCompare("ok") == 0) {
                data = '<div class="alert alert-success">Registration successfully completed..</div>'
                $('#teamRegistrationForm').get(0).reset();
            }
            else if (data.localeCompare("something_went_wrong") == 0) {
                data = '<div class="alert alert-danger">Something went wrong.</div>'
            }
            else if (data.localeCompare("registration_disabled") == 0) {
                data = '<div class="alert alert-danger">Registration is disabled.</div>'
            }
            else if (data.localeCompare("registered_already") == 0) {
                data = '<div class="alert alert-danger">You can not register again to this race.</div>'
            }
            else if (data.localeCompare("team_exists") == 0) {
                data = '<div class="alert alert-danger">Team with this name already exists.</div>'
            }
            else if (data.localeCompare("wrong_team_name") == 0) {
                data = '<div class="alert alert-danger">Team name (3 - 32 length)</div>'
            }
            else if (data.localeCompare("values") == 0) {
                data = '<div class="alert alert-danger">' +
                    'Data are invalid:<br>Firstname (3 - 32 length)<br>Lastname (3 - 32 length)<br>Phone (123456789, 123 456 789, +420123456789, +420 123 456 789)' +
                    '<br>Email (6 - 32 length)'+
                    '</div>'
            }
            $('#team_reg_result').html(data);
        }

    });
}