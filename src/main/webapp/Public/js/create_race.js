var counter_con = 0;
var counter_team = 0;
var div_con = "con";
var div_team = "team";

/**
 * Pro přidávání další podkategorií do formuláře pro registraci závodu
 * @param counter_type typ podkategorie, con - podkategorie pro závodníky, team - podkategorie pro týmy
 */
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

/**
 * Pro odebrání podkategorií z formuláře pro registraci závodu
 * @param counter_type typ podkategorie, con - podkategorie pro závodníky, team - podkategorie pro týmy
 */
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

/**
 * Ajax pro vytvoření závodu
 */
function createRace() {

    var form = $("#createEventForm").serialize();

    $.ajax({
        type: "POST",
        url: BASE_URL+"/create_event",
        data: form,
        async: false,
        dataType: "html",
        success: function (data) {
            if (!isNaN(data)) {
                window.location.replace(BASE_URL+"/race/"+data);
            }
            else if (data.localeCompare("race_name_exists") == 0) {
                data = '<div class="alert alert-danger">The race with this name already exists.</div>'
                $('#create_race_result').html(data);
            }
            else if (data.localeCompare("values") == 0) {
                data = '<div class="alert alert-danger">Data are invalid:<br>Race name (3 - 32 length)<br>If you use custom categories (1 - 20 length)</div>'
                $('#create_race_result').html(data);
            }
        }
    });
}