/**
 * Pro změnu ID u funkce na tlačítko DELETE v modálním okně podle máčknutí příslušného tlačítka DELETE v seznamu týmů.
 * @param raceId ID závodu
 * @param team ID týmu
 */
function tagDeleteButtons(raceId, team) {
    if (document.getElementById('delete_team')) {
        $("#delete_team").attr('onclick', 'deleteTeam(' + raceId + ', ' + team + ', false)');
    }
    if (document.getElementById('delete_with_contestants')) {
        $("#delete_with_contestants").attr('onclick', 'deleteTeam(' + raceId + ', ' + team + ', true)');
    }
}

/**
 * Ajax pro smazání týmu.
 * @param raceId ID závodu
 * @param team ID týmu
 * @param deleteContestans true - smazat včetně závodníů, false - smazat bez závodníků
 */
function deleteTeam(raceId, team, deleteContestans) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {teamId: team, deleteContestants: deleteContestans};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/teams/deleteTeam",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response != -1) {
                $('#T' + response).remove();
                document.getElementById("delete_result").innerHTML = "<div class='alert alert-success'>Action sucessfully compeleted.<div/>";
                $('#deleteResultModal').modal('show');
                setTimeout(function () {
                    $('#deleteResultModal').modal('hide');
                }, 200);
            }
            else {
                document.getElementById("delete_result").innerHTML = "<div class='alert alert-danger'>Something went wrong.<div/>";
                $('#deleteResultModal').modal('show');
            }
        }
    });
}

/**
 * Ajax pro úpravu jména týmu a podkategorie.
 * @param raceId ID závodu
 * @param teamId ID týmu
 */
function updateTeam(raceId, teamId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {
        teamId: teamId,
        teamName: $("#teamName" + teamId).val(),
        teamCategory: $("#teamCategory" + teamId).val()
    };

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/teams/updateTeam",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response.localeCompare("ok") == 0) {
                $("#save_result").html("<div class='alert alert-success'>Successfully saved.</div>");
                $('#updatetModal').modal('show');
                setTimeout(function () {
                    $('#updatetModal').modal('hide');
                }, 200);
            }
            else {
                if (response.localeCompare("team_exists") == 0) {
                    $("#save_result").html("<div class='alert alert-danger'>Team with this name already exists.</div>");
                }
                else if (response.localeCompare("something_went_wrong") == 0) {
                    $("#save_result").html("<div class='alert alert-danger'>Something went wrong.</div>");
                }
                else if (response.localeCompare("team_name") == 0) {
                    $("#save_result").html("<div class='alert alert-danger'>Team name (3 - 32 length).</div>");
                }
                $('#updatetModal').modal('show');
            }


        }
    });
}