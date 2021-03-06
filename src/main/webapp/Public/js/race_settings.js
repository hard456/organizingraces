/**
 * Ajax pro zakázání a povolení registrace do závodu.
 * @param raceId ID závodu
 */
function changeRegistration(raceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });

        $.ajax({
            type: "POST",
            url: BASE_URL + "/race/" + raceId + "/changeRegistration",
            dataType: "html",
            success: function (response) {
                if (response == 1) {
                    $("#registration_b").css("background-color", "#5cb85c");
                    $("#registration_b").attr("value", "Enabled");
                }
                if (response == 0) {
                    $("#registration_b").css("background-color", "#d9534f");
                    $("#registration_b").attr("value", "Disabled");
                }
            }
        });
}

/**
 * Ajax pro označení závodu jako vyhodnocený nebo naopak.
 * @param raceId ID závodu
 */
function changeEvaluation(raceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });

        $.ajax({
            type: "POST",
            url: BASE_URL + "/race/" + raceId + "/changeEvaluation",
            dataType: "html",
            success: function (response) {
                if (response == 1) {
                    $("#evaluation_b").css("background-color", "#5cb85c");
                    $("#evaluation_b").attr("value", "Evaluated");
                }
                if (response == 0) {
                    $("#evaluation_b").css("background-color", "#d9534f");
                    $("#evaluation_b").attr("value", "Not evaluated");
                }
            }
        });
}
