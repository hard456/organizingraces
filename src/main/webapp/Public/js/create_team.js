function addToTeamList(id){
    $("#B"+id).css("background-color","#d9534f");
    $("#B"+id).attr("value","-");
    $("#B"+id).attr("onclick",'removeFromTeamList('+id+')');
    $("#ID"+id).attr("value",id);
}

function removeFromTeamList(id){
    $("#B"+id).css("background-color","#5cb85c");
    $("#B"+id).attr("value","+");
    $("#B"+id).attr("onclick",'addToTeamList('+id+')');
    $("#ID"+id).attr("value","");
}

function createTeamAjax(race_id){

    var form = $("#createTeamForm").serialize();
    var data;
    $.ajax({
        type: "POST",
        url: BASE_URL+"/race/" + race_id + "/createTeam",
        data: form,
        dataType: "json",
        success: function (response) {
            if(response.validation.localeCompare("ok") == 0){
                data = '<div class="alert alert-success">The team was successfully created.</div>';
                $.each(response.contestantId, function(k, v) {
                    $('#C' + v).remove();
                });
                $('#createTeamForm').get(0).reset();
            }
            else{
                data = '<div class="alert alert-danger">'+response.validation+'</div>';
            }

            $('#create_team_result').html(data);

        }
    });

}

function deleteSoloContestant(id, race_id){

//nutnost pro poslání CSRF
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {contestant:id};

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: BASE_URL+"/race/" + race_id + "/contestants/deleteSoloContestant",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response != -1) {
                $('#C' + response).remove();
            }
        }
    });

}