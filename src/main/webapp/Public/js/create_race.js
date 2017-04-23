var counter_con = 0;
var counter_team = 0;
var div_con = "con";
var div_team = "team";

function addCategory(counter_type) {
    if (counter_type.toString() == "con") {
        if (counter_con < 18) {
            var newdiv = document.createElement('div');
            newdiv.setAttribute('id', "C" + counter_con);
            newdiv.innerHTML = "Category " + (counter_con + 3) + " <span style='color: darkgrey'>(1 - 20 length)</span>:" + "<input type='text' maxlength='20' class='form-control' style='margin-bottom: 7px' name='contestantSubCategories["+(counter_con+2)+"].name'>";
            document.getElementById(div_con).appendChild(newdiv);
            counter_con++;
        }
    }
    else if (counter_type.toString() == "team") {
        if (counter_team < 18) {
            var newdiv = document.createElement('div');
            newdiv.setAttribute('id', "T" + counter_team);
            newdiv.innerHTML = "Category " + (counter_team + 3) + " <span style='color: darkgrey'>(1 - 20 length)</span>:" + "<input type='text' maxlength='20' class='form-control' style='margin-bottom: 7px' name='teamSubCategories["+(counter_team+2)+"].name'>";
            document.getElementById(div_team).appendChild(newdiv);
            counter_team++;
        }
    }

}

function removeCategory(counter_type) {
    if (counter_type.toString() == "con") {
        if (counter_con > 0) {
            var elem = document.getElementById("C" + (counter_con - 1));
            elem.parentNode.removeChild(elem);
            counter_con--;
        }
    }
    else if (counter_type.toString() == "team") {
        if (counter_team > 0) {
            var elem = document.getElementById("T" + (counter_team - 1));
            elem.parentNode.removeChild(elem);
            counter_team--;
        }
    }

}

function createRace() {

    var form = $("#createEventForm").serialize();

    $.ajax({
        type: "POST",
        url: BASE_URL+"/create_event",
        data: form,
        async: false,
        dataType: "html",
        success: function (data) {
            if (data.localeCompare("ok") == 0) {
                $('#createEventForm').get(0).reset();
                data = '<div class="alert alert-success">The race was created.</div>'
            }
            else if (data.localeCompare("race_name_exists") == 0) {
                data = '<div class="alert alert-danger">The race with this name already exists.</div>'
            }
            else if (data.localeCompare("values") == 0) {
                data = '<div class="alert alert-danger">Data are invalid:<br>Team name (3 - 32 length)<br>If you use custom categories (1 - 20 length)</div>'
            }

            $('#create_race_result').html(data);
        }
    });
}