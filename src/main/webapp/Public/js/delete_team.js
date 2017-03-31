function tagDeleteButtons(raceId,team){
    if(document.getElementById('delete_team')){
        $("#delete_team").attr('onclick', 'deleteTeam('+raceId+', '+team+', false)');
    }
    if(document.getElementById('delete_with_contestants')){
        $("#delete_with_contestants").attr('onclick', 'deleteTeam('+raceId+', '+team+', true)');
    }
}

function deleteTeam(raceId, team, deleteContestans){

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {teamId:team, deleteContestants:deleteContestans};

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/teams/deleteTeam",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response != -1) {
                $('#T' + response).remove();
                document.getElementById("delete_result").innerHTML = "<div class='alert alert-success'>Action sucessfully compeleted.<div/>";
                $('#deleteResultModal').modal('show');
            }
            else{
                document.getElementById("delete_result").innerHTML = "<div class='alert alert-danger'>Something went wrong.<div/>";
                $('#deleteResultModal').modal('show');
            }
        }
    });

}