package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.form.DeleteTeamForm;
import cz.zcu.fav.sportevents.model.Contestant;
import cz.zcu.fav.sportevents.model.Race;
import cz.zcu.fav.sportevents.model.Team;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    Integer createTeam(HttpServletRequest r, @ModelAttribute DeleteTeamForm deleteTeamForm,
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

    @RequestMapping(value = "/race/{id}/importTeams", method = RequestMethod.POST)
    public @ResponseBody String importTeams(HttpServletRequest request, @RequestParam("file") MultipartFile file, @PathVariable("id") int race_id) {
        XSSFWorkbook wb;
        try {
            wb = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return "something went wrong";
        }
        XSSFSheet sheet = wb.getSheet("Teams");
        if(sheet == null){
            return "sheet";
        }

        return "ulala";
    }

}
