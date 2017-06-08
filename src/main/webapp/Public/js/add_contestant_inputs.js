var counter = 0;

/**
 * Přidání dalšíko závodníka do formuláte (max 2 navíc)
 * @param team_size definovaná velikost týmu
 */
function addContestant(team_size) {
    if (counter != team_size + 2) {
        if (counter == 0) {
            counter = team_size;
        }
        document.getElementById('T' + counter).style.display = "inline";
        document.getElementById(counter + 'firstname').setAttribute("name", "contestants[" + counter + "].firstname");
        document.getElementById(counter + 'lastname').setAttribute("name", "contestants[" + counter + "].lastname");
        document.getElementById(counter + 'phone').setAttribute("name", "contestants[" + counter + "].phone");
        document.getElementById(counter + 'email').setAttribute("name", "contestants[" + counter + "].email");
        var tmp = document.getElementById(counter + 'category');
        if (tmp != null) {
            tmp.setAttribute("name", "teammateCategory[" + counter + "]");
        }
        document.getElementById(counter + 'paid').setAttribute("name", "contestants[" + counter + "].paid");
        counter++;
    }
}

/**
 * Odebrání závodníka z formuláře.
 * @param team_size definovaná velikost týmu.
 */
function removeContestant(team_size) {
    if (counter != 0 && counter != team_size) {
        document.getElementById('T' + (counter - 1)).style.display = "none";
        document.getElementById((counter - 1) + 'firstname').setAttribute("name", "");
        document.getElementById((counter - 1) + 'lastname').setAttribute("name", "");
        document.getElementById((counter - 1) + 'phone').setAttribute("name", "");
        document.getElementById((counter - 1) + 'email').setAttribute("name", "");
        var tmp = document.getElementById((counter - 1) + 'category');
        if (tmp != null) {
            tmp.setAttribute("name", "");
        }
        document.getElementById((counter - 1) + 'paid').setAttribute("name", "");
        counter--;
    }
}