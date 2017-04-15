function setModalForFinishTime(raceId, teamId) {
    $('#finishTimeModalInput').val($.trim($('#finishTime' + teamId).text()));
    $("#finishTimeButton").attr('onclick', ' setFinishTime('+ raceId +', '+ teamId +');');
}

function setPointsToModal(raceId, teamId) {
    $('#pointsModalInput').val($.trim($('#points' + teamId).text()));
    $('#bonusModalInput').val($.trim($('#bonus' + teamId).text()));
    $("#setPointsButton").attr('onclick', 'setPoints('+ raceId +', '+ teamId +');');
}

function setStartTimeToModal(raceId, teamId){
    $('#startTimeModalInput').val($.trim($('#startTime' + teamId).text()));
    $("#setStartTimeButton").attr('onclick', 'setStartTime('+ raceId +', '+ teamId +');');
    $("#setGlobalStartTimeButton").attr('onclick', 'setGlobalStartTime('+ raceId +', '+ teamId +');');
}

function setPoints(raceId, teamId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {points:$("#pointsModalInput").val(),bonus:$("#bonusModalInput").val()};

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setPoints",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response != -1) {
                $('#C' + response).remove();
            }
        }
    });

}

function setGlobalStartTime(raceId, teamId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {dateTime:$("#startGlobalTime").val(),teamId:teamId};

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setStartTime",
        data: data,
        dataType: "html",
        success: function (response) {
            //if (response != -1) {
            //    $('#C' + response).remove();
            //}
        }
    });

}

function setStartTime(raceId, teamId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {dateTime:$("#startTimeModalInput").val(),teamId:teamId};

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setStartTime",
        data: data,
        dataType: "html",
        success: function (response) {
            //if (response != -1) {
            //    $('#C' + response).remove();
            //}
        }
    });

}

function setStartTimeForAll(raceId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var data = {dateTime:$("#startGlobalTime").val()};

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setStartTimeAll",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response != -1) {
                $('#C' + response).remove();
            }
        }
    });

}

function setStartTimeNextTen(raceId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var data = {dateTime:$("#startGlobalTime").val()};

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setStartTimeNextTen",
        data: data,
        dataType: "html",
        success: function (response) {
            //if (response != -1) {
            //    $('#C' + response).remove();
            //}
        }
    });

}

function teamFinished(raceId, teamId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {teamId:teamId};

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/finished",
        data: data,
        dataType: "html",
        success: function (response) {
            //if (response != -1) {
            //    $('#C' + response).remove();
            //}
        }
    });

}

function setFinishTime(raceId, teamId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {dateTime: $("#finishTimeModalInput").val(), teamId: teamId};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setFinishTime",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response != -1) {
                $('#C' + response).remove();
            }
        }
    });
}

function setDeadlineToCategory(raceId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {deadline: $("#deadLineTime").val(), categoryId:$("#teamCategory").val()};
    console.log(data.categoryId);
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setDeadlineToCategory",
        data: data,
        dataType: "html",
        success: function (response) {
            //if (response != -1) {
            //    $('#C' + response).remove();
            //}
        }
    });
}

function setDeadlineForAll(raceId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {deadline: $("#deadLineTime").val()};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setDeadlineForAll",
        data: data,
        dataType: "html",
        success: function (response) {
            //if (response != -1) {
            //    $('#C' + response).remove();
            //}
        }
    });
}
