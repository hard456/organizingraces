var counter = 0;

function addContestant(divName, team_size) {
    if (counter != team_size + 2) {
        if (counter == 0) {
            counter = team_size;
        }

        var newdiv = document.createElement('div');
        newdiv.setAttribute('id', counter);
        //newdiv.innerHTML = "Entry " + (counter + 1) + " <br><input type='text' class='form-control' name='contestants["+counter+"].firstname'>";

        newdiv.innerHTML = "<div class='row'><div class='col-sm-4'>" +
            "Teammate " + (counter + 1) + ":</div></div><br>" +
            "<div class='row'><div class='col-sm-4'>Firstname:<input class='form-control' name='contestants[" + counter + "].firstname'/>" +
            "</div><div class='col-sm-4'> Lastname:<input class='form-control' type='text' name='contestants[" + counter + "].lastname'>" +
            "</div><div class='col-sm-4'>Phone:<input class='form-control'type='text'path='contestants[" + counter + "].firstname'>" +
            "</div></div><div class='row'><div class='col-sm-4'>Email:<input class='form-control' type='text'name='contestants[" + counter + "].email'>" +
            "</div><div class='col-sm-4'>Category:<select class='form-control' name='contestants[" + counter + "].category'>" +
            "<option selected value='NONE'>NONE </option><option value='FAV'>FAV</option>" +
            "<option value='FEL'>FEL</option></select></div><br><div class='col-sm-4'><div class='checkbox'><label><input type='checkbox' name='contestants[" + counter + "].paid' value='true'>Paid</label></div></div></div><hr>";

        document.getElementById(divName).appendChild(newdiv);
        counter++;
    }
}

function removeContestant(team_size) {
    if (counter != 0 && counter != team_size) {
        var elem = document.getElementById(counter - 1);
        elem.parentNode.removeChild(elem);
        counter--;
    }
}