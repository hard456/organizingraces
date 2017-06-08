/**
 * Připravení modálního okna pro zadání času dokončení závodu u týmu.
 * @param raceId ID závodu
 * @param teamId ID týmu
 */
function setModalForFinishTime(raceId, teamId) {
    $('#finishTimeModalInput').val($.trim($('#finishTime' + teamId).text()));
    $('#finishTimeModalHidden').val($.trim($('#finishTime' + teamId).text()));
    $("#finishTimeButton").attr('onclick', ' setFinishTime(' + raceId + ', ' + teamId + ');');

    setTimeout(function () {
        $('#finishTimeModalInput').focus();
    }, 100);
}

/**
 * Připravení modálního okna pro zadání bodů týmu.
 * @param raceId ID závodu
 * @param teamId ID týmu
 */
function setPointsToModal(raceId, teamId) {
    $('#pointsModalInput').val($.trim($('#points' + teamId).text()));
    $('#bonusModalInput').val($.trim($('#bonus' + teamId).text()));
    $('#pointsModalHidden').val($.trim($('#points' + teamId).text()));
    $('#bonusModalHidden').val($.trim($('#bonus' + teamId).text()));
    $("#setPointsButton").attr('onclick', 'setPoints(' + raceId + ', ' + teamId + ');');

    setTimeout(function () {
        $('#pointsModalInput').focus();
    }, 100);
}

/**
 * Připravení modálního okna pro zadání startovního času u týmu.
 * @param raceId ID závodu
 * @param teamId ID týmu
 */
function setStartTimeToModal(raceId, teamId) {
    $('#startTimeModalInput').val($.trim($('#startTime' + teamId).text()));
    $('#startTimeModalHidden').val($.trim($('#startTime' + teamId).text()));
    $("#setStartTimeButton").attr('onclick', 'setStartTime(' + raceId + ', ' + teamId + ');');
    $("#setGlobalStartTimeButton").attr('onclick', 'setGlobalStartTime(' + raceId + ', ' + teamId + ');');

    setTimeout(function () {
        $('#startTimeModalInput').focus();
    }, 100);
}

/**
 * Ajax pro přiřazení bodů k týmu.
 * @param raceId ID závodu
 * @param teamId ID týmu
 */
function setPoints(raceId, teamId) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {
        teamId: teamId, newPoints: $("#pointsModalInput").val(),
        newBonus: $("#bonusModalInput").val(), points: $("#pointsModalHidden").val(),
        bonus: $("#bonusModalHidden").val()
    };

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/setPoints",
        data: data,
        dataType: "html",
        success: function (response) {
            $('#pointsModal').modal('hide');
            if (response.localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                table.cell({row: rowIndex, column: 3}).data(data.newPoints);
                table.cell({row: rowIndex, column: 4}).data(data.newBonus);
                //$('#points' + teamId).html(numbers[1]);
                //$('#bonus' + teamId).html(numbers[2]);
            }
            else {
                if (response.localeCompare("wrong_parameter_count") == 0) {
                    $("#resultDanger").html("Wrong number of Integer parameters");
                }
                else if (response.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response.localeCompare("team") == 0) {
                    $("#resultDanger").html("Team does not exist");
                }
                else if (response.localeCompare("not_number") == 0) {
                    $("#resultDanger").html("Number format expected");
                }
                else if (response.localeCompare("collision") == 0) {
                    $("#resultDanger").html("Data collision detected");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });

}

/**
 * Ajax pro přiřazeního času týmu z globalního okna pro zadání startovního času.
 * @param raceId ID závodu
 * @param teamId ID týmu
 */
function setGlobalStartTime(raceId, teamId) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {
        dateTime: $("#startTimeModalHidden").val(), teamId: teamId,
        newDateTime: $("#startGlobalTime").val()
    };

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/setStartTime",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response.localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                table.cell({row: rowIndex, column: 5}).data(data.newDateTime);
                //$('#startTime' + teamId).html(data.dateTime);
            }
            else {
                if (response.localeCompare("wrong_format") == 0) {
                    $("#resultDanger").html("Wrong date format");
                }
                else if (response.localeCompare("team") == 0) {
                    $("#resultDanger").html("Team does not exist");
                }
                else if (response.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response.localeCompare("cant_be_empty") == 0) {
                    $("#resultDanger").html("Start time can't be empty");
                }
                else if (response.localeCompare("collision") == 0) {
                    $("#resultDanger").html("Data collision detected");
                }
                else if (response.localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start time is not before finish time");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });

}

/**
 * Ajax pro přiřazení startovního času týmu.
 * @param raceId ID závodu
 * @param teamId ID týmu
 */
function setStartTime(raceId, teamId) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {
        newDateTime: $("#startTimeModalInput").val(), teamId: teamId,
        dateTime: $("#startTimeModalHidden").val()
    };

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/setStartTime",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response.localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                table.cell({row: rowIndex, column: 5}).data(data.newDateTime);
                //$('#startTime' + teamId).html(data.dateTime);
            }
            else {
                if (response.localeCompare("wrong_format") == 0) {
                    $("#resultDanger").html("Wrong date format");
                }
                else if (response.localeCompare("team") == 0) {
                    $("#resultDanger").html("Team does not exist");
                }
                else if (response.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response.localeCompare("cant_be_empty") == 0) {
                    $("#resultDanger").html("Start time can't be empty");
                }
                else if (response.localeCompare("collision") == 0) {
                    $("#resultDanger").html("Data collision detected");
                }
                else if (response.localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start time is not before finish time");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });

}

/**
 * Ajax pro přiřazení startovního času všem týmům podle kategorie
 * @param raceId ID závodu
 */
function setStartTimeToCategory(raceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {datetime: $("#startGlobalTime").val(), categoryId: $("#teamCategoryStartTime").val()};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    $('#loaderStartTime').css("display", "block");
    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/setStartTimeToCategory",
        data: data,
        dataType: "json",
        success: function (response) {
            $('#loaderStartTime').css("display", "none");
            $('#startTimeTeamsModal').modal('hide');
            if (response.validation.localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                $.each(response.teamIdList, function (k, v) {
                    var row = table.row('#TEAM' + v);
                    table.cell({row: row.index(), column: 5}).data(data.datetime);
                });
            }
            else {
                if (response.validation.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response.validation.localeCompare("not_team") == 0) {
                    $("#resultDanger").html("Not team with that category");
                }
                else if (response.validation.localeCompare("wrong_format") == 0) {
                    $("#resultDanger").html("Wrong datetime format");
                }
                else if (response.validation.localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start time is not before finish time somewhere");
                }
                else if (response.validation.localeCompare("empty_datetime") == 0) {
                    $("#resultDanger").html("Empty datetime");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });
}

/**
 * Ajax pro přiřazení globálního startovního času všem týmům.
 * @param raceId ID závodu
 */
function setStartTimeForAll(raceId) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var data = {dateTime: $("#startGlobalTime").val()};
    $('#loaderStartTime').css("display", "block");
    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/setStartTimeAll",
        data: data,
        dataType: "html",
        success: function (response) {
            $('#loaderStartTime').css("display", "none");
            $('#startTimeTeamsModal').modal('hide');
            if (response.localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                table.rows().every(function (rowIdx, tableLoop, rowLoop) {
                    var row = this.data();
                    row[5] = data.dateTime;
                    this.data(row);
                });
            }
            else {
                $('#startTimeTeamsModal').modal('hide');
                if (response.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response.localeCompare("not_null_finishtime") == 0) {
                    $("#resultDanger").html("Can't set null where finish time is not null");
                }
                else if (response.localeCompare("wrong_format") == 0) {
                    $("#resultDanger").html("Wrong datetime format");
                }
                else if (response.localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start time is not before finish time somewhere");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });

}

/**
 * Ajax pro přiřazení startovního času dalším 10 týmům bez času.
 * @param raceId ID závodu
 */
function setStartTimeNextTen(raceId) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var data = {dateTime: $("#startGlobalTime").val()};
    $('#loaderStartTime').css("display", "block");
    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/setStartTimeNextTen",
        data: data,
        dataType: "json",
        success: function (response) {
            $('#loaderStartTime').css("display", "none");
            $('#startTimeTeamsModal').modal('hide');
            if (response.validation.localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                $.each(response.teamIdList, function (k, v) {
                    var row = table.row('#TEAM' + v);
                    table.cell({row: row.index(), column: 5}).data(data.dateTime);
                });
            }
            else {
                if (response.validation.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response.validation.localeCompare("empty_datetime") == 0) {
                    $("#resultDanger").html("Empty datetime");
                }
                else if (response.validation.localeCompare("wrong_format") == 0) {
                    $("#resultDanger").html("Wrong datetime format");
                }
                else if (response.validation.localeCompare("not_team") == 0) {
                    $("#resultDanger").html("Empty list of teams");
                }
                else if (response.validation.localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start time is not before finish time");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });

}

/**
 * Ajax pro přiřazení týmu aktuální čas jako čas dokončení.
 * @param raceId ID závodu
 * @param teamId ID týmu
 */
function teamFinished(raceId, teamId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {teamId: teamId, dateTime: $.trim($('#finishTime' + teamId).text())};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/finished",
        data: data,
        dataType: "json",
        success: function (response) {
            if (response[0].localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                table.cell({row: rowIndex, column: 6}).data(response[1]);
                //$('#finishTime' + teamId).html(response[1]);
            }
            else {
                if (response[0].localeCompare("start_time_missing") == 0) {
                    $("#resultDanger").html("Misssing start time");
                }
                else if (response[0].localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start must be before finish time");
                }
                else if (response[0].localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response[0].localeCompare("collision") == 0) {
                    $("#resultDanger").html("Data collision detected");
                }
                else if (response[0].localeCompare("team") == 0) {
                    $("#resultDanger").html("Team does not exist");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });

}

/**
 * Ajax pro přiřazeního času dokončení týmu.
 * @param raceId ID závodu
 * @param teamId ID týmu
 */
function setFinishTime(raceId, teamId) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {
        newDateTime: $("#finishTimeModalInput").val(), teamId: teamId,
        dateTime: $("#finishTimeModalHidden").val()
    };

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/setFinishTime",
        data: data,
        dataType: "html",
        success: function (response) {
            if (response.localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                table.cell({row: rowIndex, column: 6}).data(data.newDateTime);
                //$('#finishTime' + teamId).html(data.dateTime);
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
                else if (response.localeCompare("collision") == 0) {
                    $("#resultDanger").html("Data collision detected");
                }
                else if (response.localeCompare("start_time_before") == 0) {
                    $("#resultDanger").html("Start must be before finish time");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });
}

/**
 * Ajax pro přiřazení času pro dokončení závodu (v minutách) podle podkategorie (týmové).
 * @param raceId ID závodu
 */
function setDeadlineToCategory(raceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {datetime: $("#deadLineTime").val(), categoryId: $("#teamCategoryDeadlineTime").val()};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $('#loaderDeadline').css("display", "block");

    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/setDeadlineToCategory",
        data: data,
        dataType: "json",
        success: function (response) {
            $('#loaderDeadline').css("display", "none");
            $('#deadlineTimeModal').modal('hide');
            if (response.validation.localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                $.each(response.teamIdList, function (k, v) {
                    var row = table.row('#TEAM' + v);
                    table.cell({row: row.index(), column: 7}).data(data.datetime);
                });
            }
            else {
                if (response.validation.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response.validation.localeCompare("empty_time") == 0) {
                    $("#resultDanger").html("Empty time");
                }
                else if (response.validation.localeCompare("number_format") == 0) {
                    $("#resultDanger").html("Not a number");
                }
                else if (response.validation.localeCompare("negative_number") == 0) {
                    $("#resultDanger").html("Not positive number");
                }
                else if (response.validation.localeCompare("not_team") == 0) {
                    $("#resultDanger").html("List of teams with that category is empty");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });
}

/**
 * Ajax pro přiřazení času na dokončení závodu pro všechny týmy.
 * @param raceId ID závodu
 */
function setDeadlineForAll(raceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {deadline: $("#deadLineTime").val()};

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    $('#loaderDeadline').css("display", "block");
    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/setDeadlineForAll",
        data: data,
        dataType: "html",
        success: function (response) {
            $('#loaderDeadline').css("display", "none");
            $('#deadlineTimeModal').modal('hide');
            if (response.localeCompare("ok") == 0) {
                var table = $('#myTable').DataTable();
                table.rows().every(function (rowIdx, tableLoop, rowLoop) {
                    var row = this.data();
                    row[7] = data.deadline;
                    this.data(row);
                })
            }
            else {
                if (response.localeCompare("something_went_wrong") == 0) {
                    $("#resultDanger").html("Something went wrong");
                }
                else if (response.localeCompare("empty_time") == 0) {
                    $("#resultDanger").html("Empty time");
                }
                else if (response.localeCompare("number_format") == 0) {
                    $("#resultDanger").html("Not a number");
                }
                else if (response.localeCompare("negative_number") == 0) {
                    $("#resultDanger").html("Not positive number");
                }
                $('#resultDangerModal').modal('show');
            }
        }
    });
}

/**
 * Ajax pro obnovení dat v tabulce
 * @param raceId ID závodu
 */
function reloadTable(raceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    $.ajax({
        type: "POST",
        url: BASE_URL + "/race/" + raceId + "/results/refreshTable",
        dataType: "json",
        success: function (response) {
            var table = $('#myTable').DataTable().draw();
            if (response.length != 0) {
                //Kontrola řádků - případné přidání řádku nakonec
                //$.each(response, function (k, v) {
                //    if (!loopTableRows(v, k)) {
                //        addRow(v);
                //    }
                //});
                //var numberOfRows = table.data().length;
                ////Odebrání smazaných týmů
                //for (var i = 0; i < numberOfRows; i++) {
                //    var row = table.row(i).data();
                //    if (!isRowExists(row, response)) {
                //        table.row(i).remove().draw();
                //        numberOfRows--;
                //        i--;
                //    }
                //}
                var countOfItems = 0;
                var numberOfRows = table.data().length;
                $.each(response, function (k, team) {
                    countOfItems++;
                    if (k < numberOfRows) {
                        var notActual = false;
                        var row = table.row(k).data();
                        if (row[0].toString().localeCompare(team.id) == 0) {
                            if (row[1].localeCompare(team.name) != 0) {
                                row[1] = team.name;
                                notActual = true;
                            }
                            if (row[2].localeCompare(team.category.name) != 0) {
                                row[2] = team.category.name;
                                notActual = true;
                            }
                            if (team.points.toString().localeCompare(row[3]) != 0) {
                                row[3] = team.points;
                                notActual = true;
                            }
                            if (team.bonus.toString().localeCompare(row[4]) != 0) {
                                row[4] = team.bonus;
                                notActual = true;
                            }
                            if (team.startTime == null) {
                                if (row[5].localeCompare("") != 0) {
                                    row[5] = "";
                                    notActual = true;
                                }
                            }
                            else {
                                var startTime = formatDateTime(team.startTime.millis);
                                if (row[5].localeCompare(startTime) != 0) {
                                    row[5] = startTime;
                                    notActual = true;
                                }
                            }
                            if (team.finishTime == null) {
                                if (row[6].localeCompare("") != 0) {
                                    row[6] = "";
                                    notActual = true;
                                }
                            }
                            else {
                                var finishTime = formatDateTime(team.finishTime.millis);
                                if (row[6].localeCompare(finishTime) != 0) {
                                    row[6] = finishTime;
                                    notActual = true;
                                }
                            }
                            if (team.deadlineTime == null) {
                                if (row[7].localeCompare("") != 0) {
                                    row[7] = "";
                                    notActual = true;
                                }
                            }
                            else {
                                if (team.deadlineTime.toString().localeCompare(row[7]) != 0) {
                                    row[7] = team.deadlineTime;
                                    notActual = true;
                                }
                            }
                            if (notActual == true) {
                                table.row(k).data(row).draw();
                            }
                        }
                        else {
                            table.row(k).remove().draw();
                            numberOfRows--;
                        }
                    }
                    else {
                        addRow(team);
                    }
                });
                if(numberOfRows > countOfItems){
                    for (var i = countOfItems; i <= numberOfRows; i++){
                        table.row(i).remove().draw();
                        numberOfRows--;
                    }
                }
            }
            else {
                table.clear().draw();
            }
        }
    });
}

/**
 * Pro formátování data do potřebného formátu pro zobrazení v tabulce.
 * @param millis čas v milisekundách
 * @returns {string} naformátovaný čas v podobě stringu
 */
function formatDateTime(millis) {
    var starttime = new Date(new Date(millis));
    var isotime = new Date((new Date(starttime)).toISOString());
    var fixedtime = new Date(isotime.getTime() - (starttime.getTimezoneOffset() * 60000));
    return fixedtime.toISOString().slice(0, 19).replace('T', ' ');
}

/**
 * Pro přidání nového týmu do tabulky.
 * @param team tým
 */
function addRow(team) {
    var table = $('#myTable').DataTable();
    var row = table.row.add([
        team.id,
        team.name,
        team.category.name,
        team.points,
        team.bonus,
        "",
        "",
        "",
        '<input type="button" value="Finish" class="btn btn-primary btn-sm" onclick="teamFinished(' + team.race.id + ',' + team.id + ')">'
    ]).draw();
    var rowNode = row.node();

    //ROW
    $(rowNode).css("text-align", "center");
    $(rowNode).attr("id", "TEAM" + team.id);

    //CELL POINTS
    var cell = table.cell({row: row[0], column: 3}).node();
    $(cell).attr("id", "points" + team.id);
    $(cell).attr("onclick", "setPointsToModal(" + team.race.id + "," + team.id + ")");
    $(cell).attr("data-toggle", "modal");
    $(cell).attr("data-target", "#pointsModal");

    //CELL BONUS
    var cell = table.cell({row: row[0], column: 4}).node();
    $(cell).attr("id", "bonus" + team.id);
    $(cell).attr("onclick", "setPointsToModal(" + team.race.id + "," + team.id + ")");
    $(cell).attr("data-toggle", "modal");
    $(cell).attr("data-target", "#pointsModal");

    //CELL STARTTIME
    var cell = table.cell({row: row[0], column: 5}).node();
    $(cell).attr("id", "startTime" + team.id);
    $(cell).attr("onclick", "setStartTimeToModal(" + team.race.id + "," + team.id + ")");
    $(cell).attr("data-toggle", "modal");
    $(cell).attr("data-target", "#setStartTimeModal");

    //CELL FINISHTIME
    var cell = table.cell({row: row[0], column: 6}).node();
    $(cell).attr("id", "finishTime" + team.id);
    $(cell).attr("onclick", "setModalForFinishTime(" + team.race.id + "," + team.id + ")");
    $(cell).attr("data-toggle", "modal");
    $(cell).attr("data-target", "#finishTimeModal");

    //CELL DEADLINE
    var cell = table.cell({row: row[0], column: 7}).node();
    $(cell).attr("id", "deadline" + team.id);

}