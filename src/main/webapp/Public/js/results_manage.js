function setModalForFinishTime(raceId, teamId) {
    $('#finishTimeModalInput').val($.trim($('#finishTime' + teamId).text()));
    $("#finishTimeButton").attr('onclick', ' setFinishTime(' + raceId + ', ' + teamId + ');');
}

function setPointsToModal(raceId, teamId) {
    $('#pointsModalInput').val($.trim($('#points' + teamId).text()));
    $('#bonusModalInput').val($.trim($('#bonus' + teamId).text()));
    $("#setPointsButton").attr('onclick', 'setPoints(' + raceId + ', ' + teamId + ');');
}

function setStartTimeToModal(raceId, teamId) {
    $('#startTimeModalInput').val($.trim($('#startTime' + teamId).text()));
    $("#setStartTimeButton").attr('onclick', 'setStartTime(' + raceId + ', ' + teamId + ');');
    $("#setGlobalStartTimeButton").attr('onclick', 'setGlobalStartTime(' + raceId + ', ' + teamId + ');');
}

function setPoints(raceId, teamId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");


    if (!$.isNumeric($("#pointsModalInput").val()) || !$.isNumeric($("#bonusModalInput").val()) || !$.isNumeric(teamId)) {
        $("#resultDanger").html("Number format expected");
        $('#resultDangerModal').modal('show');
    }
    else {
        var numbers = new Array();
        numbers.push(teamId);
        numbers.push(parseInt($("#pointsModalInput").val()));
        numbers.push(parseInt($("#bonusModalInput").val()));

        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });

        $.ajax({
            type: "POST",
            url: "/race/" + raceId + "/results/setPoints",
            contentType: 'application/json',
            data: JSON.stringify(numbers),
            dataType: "html",
            success: function (response) {
                if (response.localeCompare("ok") == 0) {
                    $('#points' + teamId).html(numbers[1]);
                    $('#bonus' + teamId).html(numbers[2]);
                }
                else {
                    if (response.localeCompare("wrong_parameter_count") == 0) {
                        $("#resultDanger").html("Wrong number of Integer parameters");
                    }
                    if (response.localeCompare("something_went_wrong") == 0) {
                        $("#resultDanger").html("Something went wrong");
                    }
                    if (response.localeCompare("team") == 0) {
                        $("#resultDanger").html("Team is no exists");
                    }
                    if (response.localeCompare("negative_number") == 0) {
                        $("#resultDanger").html("Number can't be negative");
                    }
                    if (response.localeCompare("not_number") == 0) {
                        $("#resultDanger").html("Number format expected");
                    }
                    $('#resultDangerModal').modal('show');
                }
            }
        });
    }
}

function setGlobalStartTime(raceId, teamId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {dateTime: $("#startGlobalTime").val(), teamId: teamId};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setStartTime",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response.localeCompare("ok") == 0) {
                $('#startTime' + teamId).html(data.dateTime);
            }
            else {
                if (response.localeCompare("wrong_format") == 0) {
                    $("#resultDanger").html("Wrong date format");
                }
                if (response.localeCompare("team") == 0) {
                    $("#resultDanger").html("Team is not exists");
                }
                if (response.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                if (response.localeCompare("cant_be_empty") == 0) {
                    $("#resultDanger").html("Start time can't be empty");
                }
                if (response.localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start time is not before finish time");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });

}

function setStartTime(raceId, teamId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {dateTime: $("#startTimeModalInput").val(), teamId: teamId};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setStartTime",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response.localeCompare("ok") == 0) {
                $('#startTime' + teamId).html(data.dateTime);
            }
            else {
                if (response.localeCompare("wrong_format") == 0) {
                    $("#resultDanger").html("Wrong date format");
                }
                if (response.localeCompare("team") == 0) {
                    $("#resultDanger").html("Team is not exists");
                }
                if (response.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                if (response.localeCompare("cant_be_empty") == 0) {
                    $("#resultDanger").html("Start time can't be empty");
                }
                if (response.localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start time is not before finish time");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });

}

function setStartTimeForAll(raceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var data = {dateTime: $("#startGlobalTime").val()};

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/setStartTimeAll",
        data: data,
        dataType: "json",
        success: function (response) {
            console.log(response.string);
           if(response.string.localeCompare("ok") == 0){
               $.each(response.list, function(k, v) {
                   $('#startTime' + v).html(data.dateTime);
               });
           }
           else{
               if (response.string.localeCompare("something_went_wrong") == 0) {
                   $("#resultDanger").html("Something went wrong");
               }
               else if (response.string.localeCompare("not_null_finishtime") == 0) {
                   $("#resultDanger").html("Can't set null where finish time is not null");
               }
               else if (response.string.localeCompare("wrong_format") == 0) {
                   $("#resultDanger").html("Wrong datetime format");
               }
               else if (response.string.localeCompare("start_time_before") == 0) {
                   $("#resultDanger").html("Start time is not before finish time somewhere");
               }
               $('#resultDangerModal').modal('show');
           }
        }
    });

}

function setStartTimeNextTen(raceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var data = {dateTime: $("#startGlobalTime").val()};

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

function teamFinished(raceId, teamId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {teamId: teamId};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: "/race/" + raceId + "/results/finished",
        data: data,
        dataType: "json",
        success: function (response) {
            if (response[0].localeCompare("ok") == 0) {
                $('#finishTime' + teamId).html(response[1]);
            }
            else {
                if (response[0].localeCompare("start_time_missing") == 0) {
                    $("#resultDanger").html("Misssing start time");
                }
                else if (response[0].localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start time is not before finish time");
                }
                else if (response[0].localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                $('#resultDangerModal').modal('show');
            }
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
            if (response.localeCompare("ok") == 0) {
                $('#finishTime' + teamId).html(data.dateTime);
            }
            else {
                if (response.localeCompare("wrong_format") == 0) {
                    $("#resultDanger").html("Wrong date format");
                }
                else if (response.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response.localeCompare("team") == 0) {
                    $("#resultDanger").html("Team is not exists");
                }
                else if (response.localeCompare("start_time_missing") == 0) {
                    $("#resultDanger").html("Missing start time");
                }
                else if (response.localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start time is not before finish time");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });
}

function setDeadlineToCategory(raceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {deadline: $("#deadLineTime").val(), categoryId: $("#teamCategory").val()};
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

function setDeadlineForAll(raceId) {
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
