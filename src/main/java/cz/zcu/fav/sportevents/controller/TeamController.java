package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.container.ContestantImportExcel;
import cz.zcu.fav.sportevents.container.TeamImportExcel;
import cz.zcu.fav.sportevents.form.DeleteTeamForm;
import cz.zcu.fav.sportevents.form.UpdateTeamForm;
import cz.zcu.fav.sportevents.model.*;
import cz.zcu.fav.sportevents.service.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
public class TeamController {

    @Autowired
    RaceService raceService;

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;

    @Autowired
    ContestantService contestantService;

    @Autowired
    RaceCooperationService raceCooperationService;

    @Autowired
    TeamSubcategoryService teamSubcategoryService;

    @Autowired
    ContestantSubcategoryService contestantSubcategoryService;

    @RequestMapping(value = "/race/{id}/contestants/teams", method = RequestMethod.GET)
    public ModelAndView teams(@PathVariable("id") int race_id) {
        ModelAndView model = new ModelAndView();
        Race race = raceService.getRaceById(race_id);
        if (race == null) {
            model.addObject("error", "404");
            model.setViewName("error/error_page");
            return model;
        } else {
            User user = userService.getLoginUser();
            model.addObject("race", race);
            model.addObject("user", user);
            if(race.getTeamCategory() != null){
                model.addObject("team_categories", teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId()));
            }
            model.setViewName("race/teams");
            model.addObject("teams", teamService.getTeamsByRaceId(race_id));
            model.addObject("contestants", contestantService.getContestantsByRaceId(race_id));
            if (user != null) {
                if (raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
                    model.addObject("race_cooperator", true);
                } else {
                    model.addObject("race_cooperator", false);
                }
            }
            return model;
        }
    }

    @RequestMapping(value = "/race/{id}/teams/deleteTeam", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer deleteTeam(HttpServletRequest r, @ModelAttribute DeleteTeamForm deleteTeamForm,
                       BindingResult bindingResult, @PathVariable("id") int race_id) {

        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);

        if (bindingResult.hasErrors()) {
            return -1;
        }

        Team team = teamService.getTeamById(deleteTeamForm.getTeamId());

        if (user == null || race == null) {
            return -1;
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return -1;
        }

        if (team.getRace().getId() != race.getId()) {
            return -1;
        }

        if (deleteTeamForm.getDeleteContestants()) {
            contestantService.deleteContestantsByTeamId(deleteTeamForm.getTeamId());
        } else {
            contestantService.removeTeamByTeamId(deleteTeamForm.getTeamId());
        }

        teamService.delete(team);

        return deleteTeamForm.getTeamId();

    }

    @RequestMapping(value = "/race/{id}/exportTeams", method = RequestMethod.POST)
    public String exportTeams(@PathVariable("id") int race_id, HttpServletResponse response) {

        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);

        if (user == null || race == null) {
            return "error/wrong";
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "error/wrong";
        }

        int rowIndex = 0;
        int cellIndex = 0;
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet teamSheet = wb.createSheet("Teams");

        List<Team> teams = teamService.getTeamsByRaceId(race_id);
        List<Contestant> contestants = contestantService.getContestantsByRaceId(race_id);

        XSSFRow row = teamSheet.createRow(rowIndex);
        XSSFCell cell;

        if (race.getTeamSize() > 1) {
            //SOLO
            cell = row.createCell(cellIndex);
            cell.setCellValue("SOLO");
            cellIndex++;

            //TEAM NAME
            cell = row.createCell(cellIndex);
            cell.setCellValue("TEAM");
            cellIndex++;

            //TEAM CATEGORY
            cell = row.createCell(cellIndex);
            cell.setCellValue("TEAM_CATEGORY");
            cellIndex++;

            //CAPTIONS
            for (int i = 0; i < race.getTeamSize(); i++) {
                cell = row.createCell(cellIndex);
                cell.setCellValue(i + "FIRSTNAME");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue(i + "LASTNAME");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue(i + "EMAIL");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue(i + "PHONE");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue(i + "PAID");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue(i + "CATEGORY");
                cellIndex++;
            }
            rowIndex++;
            row = teamSheet.createRow(rowIndex++);
            cellIndex = 0;

            //TEAM RECORDS
            for (Team team : teams) {
                cell = row.createCell(cellIndex);
                cell.setCellValue("NO");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue(team.getName());
                cellIndex++;

                cell = row.createCell(cellIndex);
                if (team.getCategory() != null) {
                    cell.setCellValue(team.getCategory().getName());
                }
                cellIndex++;

                for (Contestant c : contestants) {
                    if (c.getTeam() != null && c.getTeam().getId() == team.getId()) {
                        cell = row.createCell(cellIndex);
                        cell.setCellValue(c.getFirstname());
                        cellIndex++;

                        cell = row.createCell(cellIndex);
                        cell.setCellValue(c.getLastname());
                        cellIndex++;

                        cell = row.createCell(cellIndex);
                        cell.setCellValue(c.getEmail());
                        cellIndex++;

                        cell = row.createCell(cellIndex);
                        cell.setCellValue(c.getPhone());
                        cellIndex++;

                        cell = row.createCell(cellIndex);
                        if (c.isPaid()) {
                            cell.setCellValue("YES");
                        } else {
                            cell.setCellValue("NO");
                        }
                        cellIndex++;

                        cell = row.createCell(cellIndex);
                        if (race.getContestantCategory() != null) {
                            cell.setCellValue(c.getCategory().getName());
                        }
                        cellIndex++;
                    }
                }

                row = teamSheet.createRow(rowIndex++);
                cellIndex = 0;
            }
            for (Contestant c : contestants) {
                if (c.getTeam() == null) {
                    cell = row.createCell(cellIndex);
                    cell.setCellValue("YES");
                    cellIndex += 3;

                    cell = row.createCell(cellIndex);
                    cell.setCellValue(c.getFirstname());
                    cellIndex++;

                    cell = row.createCell(cellIndex);
                    cell.setCellValue(c.getLastname());
                    cellIndex++;

                    cell = row.createCell(cellIndex);
                    cell.setCellValue(c.getEmail());
                    cellIndex++;

                    cell = row.createCell(cellIndex);
                    cell.setCellValue(c.getPhone());
                    cellIndex++;

                    cell = row.createCell(cellIndex);
                    if (c.isPaid()) {
                        cell.setCellValue("YES");
                    } else {
                        cell.setCellValue("NO");
                    }
                    cellIndex++;

                    cell = row.createCell(cellIndex);
                    if (race.getContestantCategory() != null) {
                        cell.setCellValue(c.getCategory().getName());
                    }

                    cellIndex = 0;
                    row = teamSheet.createRow(rowIndex++);
                }
            }
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Teams-" + dateFormat.format(new Date()) + ".xlsx");
            wb.write(response.getOutputStream());
        } catch (FileNotFoundException e) {
            return "error/wrong";
        } catch (IOException e) {
            return "error/wrong";
        }
        return "";
    }

    @RequestMapping(value = "/race/{id}/exportContestants", method = RequestMethod.POST)
    public String exportContestants(@PathVariable("id") int race_id, HttpServletResponse response) {

        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);

        int rowIndex = 0;
        int cellIndex = 0;
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet teamSheet = wb.createSheet("Teams");

        List<Team> teams = teamService.getTeamsByRaceId(race_id);
        List<Contestant> contestants = contestantService.getContestantsByRaceId(race_id);

        XSSFRow row = teamSheet.createRow(rowIndex);
        XSSFCell cell;
        if (user == null || race == null) {
            return "error/wrong";
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "error/wrong";
        }
        if (race.getTeamSize() == 1) {
            for (int i = 0; i < race.getTeamSize(); i++) {
                cell = row.createCell(cellIndex);
                cell.setCellValue("FIRSTNAME");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue("LASTNAME");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue("EMAIL");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue("PHONE");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue("PAID");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue("CON_CATEGORY");
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue("TEAM_CATEGORY");
                cellIndex++;
            }
            rowIndex++;
            row = teamSheet.createRow(rowIndex++);
            cellIndex = 0;

            for (Contestant c : contestants) {
                cell = row.createCell(cellIndex);
                cell.setCellValue(c.getFirstname());
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue(c.getLastname());
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue(c.getEmail());
                cellIndex++;

                cell = row.createCell(cellIndex);
                cell.setCellValue(c.getPhone());
                cellIndex++;

                cell = row.createCell(cellIndex);
                if (c.isPaid()) {
                    cell.setCellValue("YES");
                } else {
                    cell.setCellValue("NO");
                }
                cellIndex++;

                cell = row.createCell(cellIndex);
                if (race.getContestantCategory() != null) {
                    cell.setCellValue(c.getCategory().getName());
                }
                cellIndex++;

                cell = row.createCell(cellIndex);
                if (race.getTeamCategory() != null) {
                    cell.setCellValue(c.getTeam().getCategory().getName());
                }

                cellIndex = 0;
                row = teamSheet.createRow(rowIndex++);
            }
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            response.setHeader("Expires", "0");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Teams-" + dateFormat.format(new Date()) + ".xlsx");
            wb.write(response.getOutputStream());
        } catch (FileNotFoundException e) {
            return "error/wrong";
        } catch (IOException e) {
            return "error/wrong";
        }
        return "";
    }

    @RequestMapping(value = "/race/{id}/importOnePersonTeam", method = RequestMethod.POST)
    public
    @ResponseBody
    String importOnePersonTeam(HttpServletRequest request, @RequestParam("file") MultipartFile file, @PathVariable("id") int race_id) {
        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        XSSFWorkbook wb;
        XSSFRow row;
        XSSFCell cell;
        if(race.getTeamSize() > 1){
            return "something went wrong";
        }
        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something went wrong";
        }
        if (file.isEmpty()) {
            return "empty";
        }
        if (file.getName().endsWith(".xlsx")) {
            return "not xlsx format";
        }
        try {
            wb = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return "something went wrong";
        }
        XSSFSheet sheet = wb.getSheet("Teams");
        if (sheet == null) {
            return "Teams sheet is not exists";
        }

        Iterator rows = sheet.rowIterator();

        if (!rows.hasNext()) {
            return "captions";
        }

        row = (XSSFRow) rows.next();

        List<String> captions = generateContestantCaptionList();
        String captionsResult = validCaptions(captions, row);
        if (!captionsResult.equals("")) {
            return captionsResult;
        }
        String tmp;
        List<ContestantImportExcel> contestants = new ArrayList<>();
        boolean rowEnd = false;
        List<TeamSubcategory> teamCategories = null;
        List<ContestantSubcategory> conCategories = null;
        if (race.getTeamCategory() != null) {
            teamCategories = teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId());
        }
        if (race.getContestantCategory() != null) {
            conCategories = contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId());
        }

        while (rows.hasNext() && !rowEnd) {
            row = (XSSFRow) rows.next();
            ContestantImportExcel contestant = new ContestantImportExcel();
            for (int i = 0; i < captions.size(); i++) {
                cell = row.getCell(i);
                if (cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                }
                if(isRowEmpty(row,captions.size())){
                    rowEnd = true;
                    break;
                }
                if (i == 0) {
                    if (cell != null) {
                        tmp = HtmlUtils.htmlEscape(cell.getStringCellValue(), "UTF-8");
                        if (tmp.length() > 32 || tmp.length() < 3) {
                            return "wrong firstname (3 - 32 length) [ROW:" + (row.getRowNum()+1) + "]";
                        } else {
                            contestant.setFirstname(tmp);
                        }
                    }
                    else{
                        return "empty firstname [ROW:" + (row.getRowNum()+1) + "]";
                    }
                } else if (i == 1) {
                    if (cell != null) {
                        tmp = HtmlUtils.htmlEscape(cell.getStringCellValue(), "UTF-8");
                        if (tmp.length() > 32 || tmp.length() < 3) {
                            return "wrong lastname (3 - 32 length) [ROW:" + (row.getRowNum()+1) + "]";
                        } else {
                            contestant.setLastname(tmp);
                        }
                    }
                    else{
                        return "empty lastname [ROW:" + (row.getRowNum()+1) + "]";
                    }
                } else if (i == 2) {
                    if (cell != null) {
                        tmp = HtmlUtils.htmlEscape(cell.getStringCellValue(), "UTF-8");
                        if (tmp.length() > 32 || tmp.length() < 6) {
                            return "wrong email (6 - 32 length) [ROW:" + (row.getRowNum()+1) + "]";
                        } else {
                            contestant.setEmail(tmp);
                        }
                    }
                    else{
                        contestant.setEmail("");
                    }
                } else if (i == 3) {
                    if (cell != null) {
                        String phoneRegEx = "^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$";
                        tmp = cell.getStringCellValue().replaceAll("\\s+","");
                        if (!tmp.matches(phoneRegEx)) {
                            return "wrong phone format [ROW:" + (row.getRowNum()+1) + "]";
                        } else {
                            contestant.setPhone(tmp);
                        }
                    }
                    else{
                        contestant.setPhone("");
                    }
                } else if (i == 4) {
                    if (cell != null) {
                        tmp = cell.getStringCellValue();
                        if (!tmp.equals("YES") && !tmp.equals("NO")) {
                            return "wrong paid format [ROW:" + (row.getRowNum()+1) + "]";
                        } else {
                            contestant.setPaid(tmp);
                        }
                    }
                    else{
                        return "empty paid [ROW:" + (row.getRowNum()+1) + "]";
                    }
                } else if (i == 5) {
                    if (race.getContestantCategory() != null) {

                        if(cell != null && cell.getStringCellValue().length() > 0){
                            contestant.setContestantCategory(cell.getStringCellValue());
                        }
                        else{
                            return "empty contestant category [ROW:" + (row.getRowNum()+1) + "]";
                        }

                        if(!validConCategory(conCategories,cell.getStringCellValue())){
                            return "wrong contestant category [ROW:" + (row.getRowNum()+1) + "]";
                        }

                    } else {
                        if(cell != null && cell.getStringCellValue().length() > 0){
                            return "contestant category should be empty [ROW:" + (row.getRowNum()+1) + "]";
                        }
                        contestant.setContestantCategory("");
                    }
                } else if (i == 6) {
                    if (race.getTeamCategory() != null) {
                        if (cell != null && cell.getStringCellValue().length() > 0) {
                            contestant.setTeamCategory(cell.getStringCellValue());
                        }
                        else{
                            return "empty team category [ROW:" + (row.getRowNum()+1) + "]";
                        }
                        if(!validTeamCategory(teamCategories,cell.getStringCellValue())){
                            return "wrong team category [ROW:" + (row.getRowNum()+1) + "]";
                        }
                    } else {
                        if(cell != null && cell.getStringCellValue().length() > 0){
                            return "team category should be empty [ROW:" + (row.getRowNum()+1) + "]";
                        }
                        contestant.setTeamCategory("");
                    }
                }
            }
            if(!rowEnd){
                contestants.add(contestant);
            }
        }

        try {
            file.getInputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ContestantImportExcel c : contestants) {
            Team team = new Team();
            Contestant contestant = new Contestant();
            team.setRace(race);
            if(race.getTeamCategory() != null){
                team.setCategory(teamSubcategoryService.getSubcategoryByNameByCategoryId(c.getTeamCategory(),race.getTeamCategory().getId()));
            }
            teamService.save(team);
            contestant.setFirstname(c.getFirstname());
            contestant.setLastname(c.getLastname());


            contestant.setRace(race);
            contestant.setUser(user);
            if(race.getContestantCategory() != null){
                contestant.setCategory(contestantSubcategoryService.getCategoryByNameByCategoryId(c.getContestantCategory(),race.getContestantCategory().getId()));
            }
            if(c.getPaid().equals("YES")){
                contestant.setPaid(true);
            }
            else{
                contestant.setPaid(false);
            }
            if(c.getPhone().length() > 0){
                contestant.setPhone(c.getPhone());
            }
            if(c.getEmail().length() > 0){
                contestant.setEmail(c.getEmail());
            }
            contestant.setTeam(team);
            contestantService.saveContestant(contestant);
        }

        return "ok";
    }

    @RequestMapping(value = "/race/{id}/importTeams", method = RequestMethod.POST)
    public
    @ResponseBody
    String importTeams(HttpServletRequest request, @RequestParam("file") MultipartFile file, @PathVariable("id") int race_id) {

        Race race = raceService.getRaceById(race_id);
        User user = userService.getLoginUser();

        XSSFWorkbook wb;
        XSSFRow row;
        XSSFCell cell;
        boolean rowEnd = false;
        if(race.getTeamSize() == 1){
            return "something went wrong";
        }
        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something went wrong";
        }
        if (file.isEmpty()) {
            return "empty";
        }
        if (file.getName().endsWith(".xlsx")) {
            return "not xlsx format";
        }
        try {
            wb = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return "something went wrong";
        }
        XSSFSheet sheet = wb.getSheet("Teams");
        if (sheet == null) {
            return "Teams sheet is not exists";
        }

        Iterator rows = sheet.rowIterator();

        if (!rows.hasNext()) {
            return "captions";
        }

        row = (XSSFRow) rows.next();

        List<String> captions = generateTeamCaptionList(race.getTeamSize());
        String captionsResult = validCaptions(captions, row);
        if (!captionsResult.equals("")) {
            return captionsResult;
        }

        List<TeamImportExcel> teamImport = new ArrayList<>();

        while (rows.hasNext() && !rowEnd) {
            row = (XSSFRow) rows.next();
            TeamImportExcel team = new TeamImportExcel();
            ContestantImportExcel contestant = new ContestantImportExcel();
            for (int i = 0; i < captions.size(); i++) {
                if(isRowEmpty(row,captions.size())){
                    rowEnd = true;
                    break;
                }
                if (team.getSolo() != null && team.getSolo().equals("YES") && i == 9) {
                    break;
                }
                cell = row.getCell(i);
                if (cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                }
                if (i == 0) {
                    if (cell != null) {
                        if (cell.getStringCellValue().equals("YES") || cell.getStringCellValue().equals("NO")) {
                            team.setSolo(cell.getStringCellValue());
                        } else {
                            return "solo wrong value [ROW:" + (row.getRowNum()+1) + "]";
                        }
                    } else {
                        //BREAK FILE READING
                        break;
                    }
                } else if (i == 1) {
                    if (cell != null) {
                        team.setTeamName(cell.getStringCellValue());
                    } else {
                        team.setTeamName("");
                    }
                } else if (i == 2) {
                    if (race.getTeamCategory() == null) {
                        if (cell != null && cell.getStringCellValue().length() > 0) {
                            return "team category should be empty [ROW:" + (row.getRowNum()+1) + "]";
                        }
                        team.setCategory("");
                    } else {
                        if (team.getSolo().equals("YES")) {
                            team.setCategory("");
                        } else if (cell != null && cell.getStringCellValue().length() > 0) {
                            team.setCategory(HtmlUtils.htmlEscape(cell.getStringCellValue(), "UTF-8"));
                        } else {
                            return "empty team category [ROW:" + (row.getRowNum()+1) + "]";
                        }
                    }
                } else if (i > 2) {
                    if ((i - 2) % 6 == 0) {
                        if (race.getContestantCategory() == null) {
                            if (cell != null && cell.getStringCellValue().length() > 0) {
                                return "contestant category should be empty [ROW:" + (row.getRowNum()+1) + "]";
                            }
                            contestant.setContestantCategory("");
                        } else {
                            if (cell != null && cell.getStringCellValue().length() > 0) {
                                contestant.setContestantCategory(HtmlUtils.htmlEscape(cell.getStringCellValue(), "UTF-8"));
                            } else {
                                return "empty contestant category [ROW:" + (row.getRowNum()+1) + "]";
                            }
                        }
                        team.addContestant(contestant);
                    } else if ((i - 2) % 6 == 1) {
                        contestant = new ContestantImportExcel();
                        if (cell != null && cell.getStringCellValue().length() > 0) {
                            contestant.setFirstname(HtmlUtils.htmlEscape(cell.getStringCellValue(), "UTF-8"));
                        } else {
                            return "empty firstname [ROW:" + (row.getRowNum()+1) + "]";
                        }

                    } else if ((i - 2) % 6 == 2) {
                        if (cell != null && cell.getStringCellValue().length() > 0) {
                            contestant.setLastname(HtmlUtils.htmlEscape(cell.getStringCellValue(), "UTF-8"));
                        } else {
                            return "empty lastname [ROW:" + (row.getRowNum()+1) + "]";
                        }
                    } else if ((i - 2) % 6 == 3) {
                        if (cell != null) {
                            contestant.setEmail(HtmlUtils.htmlEscape(cell.getStringCellValue(), "UTF-8"));
                        } else {
                            contestant.setEmail("");
                        }
                    } else if ((i - 2) % 6 == 4) {
                        if (cell != null && cell.getStringCellValue().length() != 0) {
                            contestant.setPhone(cell.getStringCellValue().replaceAll("\\s+",""));
                        } else {
                            contestant.setPhone("");
                        }
                    } else if ((i - 2) % 6 == 5) {
                        if (cell != null) {
                            contestant.setPaid(HtmlUtils.htmlEscape(cell.getStringCellValue(), "UTF-8"));
                        } else {
                            return "empty paid [ROW:" + (row.getRowNum()+1) + "]";
                        }
                    }
                }
            }
            if (!rowEnd) {
                teamImport.add(team);
            }
        }
        String validation = validationTeamImportData(teamImport, race);
        if (!validation.equals("")) {
            return validation;
        }

        try {
            file.getInputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (TeamImportExcel team : teamImport) {
            if(team.getSolo().equals("NO")){
                Team newTeam = new Team();
                if(team.getTeamName().length() > 0){
                    newTeam.setName(team.getTeamName());
                }
                if(race.getTeamCategory() != null){
                    String teamCategory = team.getCategory();
                    newTeam.setCategory(teamSubcategoryService.getSubcategoryByNameByCategoryId(teamCategory,race.getTeamCategory().getId()));
                }
                newTeam.setRace(race);
                teamService.save(newTeam);

                for (ContestantImportExcel contestant : team.getContestants()) {
                    Contestant newContestant = new Contestant();
                    newContestant.setFirstname(contestant.getFirstname());
                    newContestant.setLastname(contestant.getLastname());
                    newContestant.setFirstname(contestant.getFirstname());
                    if(contestant.getEmail().length() > 0){
                        newContestant.setEmail(contestant.getEmail());
                    }
                    if(contestant.getPhone().length() > 0){
                        newContestant.setPhone(contestant.getPhone());
                    }
                    if(race.getContestantCategory() != null){
                        String category = contestant.getContestantCategory();
                        newContestant.setCategory(contestantSubcategoryService.getCategoryByNameByCategoryId(category,race.getContestantCategory().getId()));
                    }
                    if(contestant.getPaid().equals("YES")){
                        newContestant.setPaid(true);
                    }
                    else{
                        newContestant.setPaid(false);
                    }
                    newContestant.setRace(race);
                    newContestant.setUser(user);
                    newContestant.setTeam(newTeam);
                    contestantService.saveContestant(newContestant);
                }

            }
            else{
                Contestant newContestant = new Contestant();
                newContestant.setFirstname(team.getContestants().get(0).getFirstname());
                newContestant.setLastname(team.getContestants().get(0).getLastname());

                if(race.getContestantCategory() != null){
                    String category = team.getContestants().get(0).getContestantCategory();
                    newContestant.setCategory(contestantSubcategoryService.getCategoryByNameByCategoryId(category,race.getContestantCategory().getId()));
                }
                if(team.getContestants().get(0).getPaid().equals("YES")){
                    newContestant.setPaid(true);
                }
                else{
                    newContestant.setPaid(false);
                }
                if(team.getContestants().get(0).getEmail().length() > 0){
                    newContestant.setEmail(team.getContestants().get(0).getEmail());
                }
                if(team.getContestants().get(0).getPhone().length() > 0){
                    newContestant.setPhone(team.getContestants().get(0).getPhone());
                }
                newContestant.setUser(user);
                newContestant.setRace(race);
                contestantService.saveContestant(newContestant);
            }
        }

        return "ok";
    }

    private String validationTeamImportData(List<TeamImportExcel> teamsImport, Race race) {
        String phoneRegEx = "^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$";
        List<TeamSubcategory> teamCategories = null;
        List<ContestantSubcategory> conCategories = null;
        if (race.getTeamCategory() != null) {
            teamCategories = teamSubcategoryService.getListByCategoryId(race.getTeamCategory().getId());
        }
        if (race.getContestantCategory() != null) {
            conCategories = contestantSubcategoryService.getListByCategoryId(race.getContestantCategory().getId());
        }
        for (int i = 0; i < teamsImport.size(); i++) {
            TeamImportExcel team = teamsImport.get(i);
            if (team.getSolo().equals("NO")) {
                if (team.getTeamName().length() != 0 && (team.getTeamName().length() > 32 || team.getTeamName().length() < 3)) {
                    return "wrong team name size (3 - 32 length) [ROW:" + (i + 2) + "]";
                }
                if (race.getTeamCategory() != null && !validTeamCategory(teamCategories, team.getCategory())) {
                    return "wrong team category [ROW:" + (i + 2) + "]";
                }
            }
            for (ContestantImportExcel contestant : team.getContestants()) {
                if (contestant.getFirstname().length() > 32 || contestant.getFirstname().length() < 3) {
                    return "wrong fristname (3 - 32 length) [ROW:" + (i + 2) + "]";
                }
                if (contestant.getLastname().length() > 32 || contestant.getLastname().length() < 3) {
                    return "wrong lastname (3 - 32 length) [ROW:" + (i + 2) + "]";
                }
                if (!contestant.getPaid().equals("YES") && !contestant.getPaid().equals("NO")) {
                    return "wrong paid value [ROW:" + (i + 2) + "]";
                }
                if (contestant.getEmail().length() != 0) {
                    if (!EmailValidator.getInstance().isValid(contestant.getEmail())) {
                        return "wrong email format [ROW:" + (i + 2) + "]";
                    }
                    if (contestant.getEmail().length() < 6 || contestant.getEmail().length() > 32) {
                        return "wrong email (3 - 32 length) [ROW:" + (i + 2) + "]";
                    }
                }
                if (contestant.getPhone().length() != 0 && !contestant.getPhone().matches(phoneRegEx)) {
                    return "wrong phone format [ROW:" + (i + 2) + "]";
                }
                if (race.getContestantCategory() != null && !validConCategory(conCategories, contestant.getContestantCategory())) {
                    return "wrong contestant category [ROW:" + (i + 2) + "]";
                }
            }
        }
        return "";
    }

    private boolean validTeamCategory(List<TeamSubcategory> categories, String teamCategory) {
        for (TeamSubcategory category : categories) {
            if (category.getName().equals(teamCategory)) {
                return true;
            }
        }
        return false;
    }

    private boolean validConCategory(List<ContestantSubcategory> categories, String conCategory) {
        for (ContestantSubcategory category : categories) {
            if (category.getName().equals(conCategory)) {
                return true;
            }
        }
        return false;
    }

    private List<String> generateTeamCaptionList(int teamSize) {
        List<String> captions = new ArrayList<>();
        captions.add("SOLO");
        captions.add("TEAM");
        captions.add("TEAM_CATEGORY");
        for (int i = 0; i < teamSize; i++) {
            captions.add(i + "FIRSTNAME");
            captions.add(i + "LASTNAME");
            captions.add(i + "EMAIL");
            captions.add(i + "PHONE");
            captions.add(i + "PAID");
            captions.add(i + "CATEGORY");
        }
        return captions;
    }

    private List<String> generateContestantCaptionList() {
        List<String> captions = new ArrayList<>();
        captions.add("FIRSTNAME");
        captions.add("LASTNAME");
        captions.add("EMAIL");
        captions.add("PHONE");
        captions.add("PAID");
        captions.add("CON_CATEGORY");
        captions.add("TEAM_CATEGORY");
        return captions;
    }

    private String validCaptions(List<String> captions, XSSFRow row) {
        Iterator<Cell> cells = row.cellIterator();
        XSSFCell cell;
        for (String caption : captions) {
            if (cells.hasNext()) {
                cell = (XSSFCell) cells.next();
                if (!caption.equals(cell.getStringCellValue())) {
                    return "captions [cell:" + (cell.getColumnIndex()+1) + "]";
                }
            } else {
                return "captions";
            }
        }
        return "";
    }

    private boolean isRowEmpty(XSSFRow row, int rowSize){
        Iterator<Cell> cells = row.cellIterator();
        XSSFCell cell;
        for (int i = 0; i<rowSize; i++){
            if (cells.hasNext()) {
                cell = (XSSFCell) cells.next();
                if(cell != null && cell.getStringCellValue().length() > 0){
                    return false;
                }
            }
        }
        return true;
    }

    @RequestMapping(value = "/race/{id}/teams/updateTeam", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateTeam(HttpServletRequest r, @ModelAttribute UpdateTeamForm updateTeamForm,
                       BindingResult bindingResult, @PathVariable("id") int race_id) {

        User user = userService.getLoginUser();
        Race race = raceService.getRaceById(race_id);
        TeamSubcategory category = null;
        if (bindingResult.hasErrors()) {
            return "something_went_wrong";
        }

        Team team = teamService.getTeamById(updateTeamForm.getTeamId());

        if (user == null || race == null) {
            return "something_went_wrong";
        }

        if (race.getUser().getId() != user.getId() && !raceCooperationService.isUserRaceCooperator(race_id, user.getId())) {
            return "something_went_wrong";
        }

        if (team.getRace().getId() != race.getId()) {
            return "something_went_wrong";
        }

        if(updateTeamForm.getTeamName() != null && updateTeamForm.getTeamName().length() != 0){
            updateTeamForm.setTeamName(HtmlUtils.htmlEscapeHex(updateTeamForm.getTeamName(),"UTF-8"));
            if(!team.getName().equals(updateTeamForm.getTeamName())){
                if(teamService.getByRaceIdTeamName(race_id,updateTeamForm.getTeamName()) != null){
                    return "team_exists";
                }
            }
            if(updateTeamForm.getTeamName().length() > 32 || updateTeamForm.getTeamName().length() < 3){
                return "team_name";
            }
        }

        if(race.getTeamCategory() != null){
            if(updateTeamForm.getTeamCategory() != null){
                category = teamSubcategoryService.getSubcategoryById(updateTeamForm.getTeamCategory());
                if(category.getTeamCategory().getId() != race.getTeamCategory().getId()){
                    return "something_went_wrong";
                }
            }
            else{
                return "something_went_wrong";
            }
        }

        team.setName(updateTeamForm.getTeamName());
        team.setCategory(category);
        teamService.update(team);

        return "ok";
    }

}
