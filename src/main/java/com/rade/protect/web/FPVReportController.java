package com.rade.protect.web;

import com.rade.protect.model.entity.FPVReport;
import com.rade.protect.model.request.FPVReportRequest;
import com.rade.protect.model.response.FPVReportResponse;
import com.rade.protect.service.impl.FPVReportServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@SessionAttributes("fpvReport")
public class FPVReportController {

    @Autowired
    private FPVReportServiceImpl fpvReportServiceImpl;

    @ModelAttribute(name = "fpvReport")
    public FPVReport fpvReport() {
        return new FPVReport();
    }

    //work
    @GetMapping("/fpvreports")
    public String createFPVReportForm(@ModelAttribute("fpvReport") FPVReport fpvReport, Model model) {
        model.addAttribute("fpvReport", fpvReport);
        return "form/createFPVReport";
    }

    //work
    @PostMapping("/submitToCreateFPVReportForm")
    public String submitCreateFPVReportForm(@Valid @ModelAttribute("fpvReport") FPVReportRequest fpvReportRequest, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "form/createFPVReport";
        }
        FPVReportResponse savedFpvReport = fpvReportServiceImpl.save(fpvReportRequest);
        log.info("FPV report submitted: {}", savedFpvReport);
        sessionStatus.setComplete();
        return "result/success/createFPVReport_success";
    }

    //work
    @GetMapping("/fpvReport/search")
    public String searchFPVReportForm() {
        return "form/searchFPVReport";
    }

    //work
    @PostMapping("/submitToSearchFPVReportForm")
    public String getFPVReportById(@RequestParam(name = "id") Long id, Model model) {
        log.info("Searching for report with id: {}", id);
        Optional<FPVReportResponse> fpvReport = fpvReportServiceImpl.findById(id);

        if (fpvReport.isPresent()) {
            model.addAttribute("fpvReport", fpvReport);
            model.addAttribute("message", "FPV Report with ID " + id + " is found!");
            return "result/success/viewFPVReport_success";
        } else {
            model.addAttribute("message", "FPV Report with ID " + id + " is not found!");
            return "result/failure/viewFPVReport_failure";
        }
    }

/*    @GetMapping("/fpvReports/sort")
    public String getAllFPVReports(@RequestParam(value = "sortCriteria", required = false) String sortCriteria,
                                   @RequestParam(value = "fpvModel", required = false) String fpvModel,
                                   @RequestParam(value = "isLostFPVDueToREB", required = false) Boolean isLostFPVDueToREB,
                                   @RequestParam(value = "isOnTargetFPV", required = false) Boolean isOnTargetFPV,
                                   Model model) {
        List<FPVReport> fpvReports;

        if ("fpvModel".equals(sortCriteria) && fpvModel != null) {
            log.info("FPV Model: {}", fpvModel);
            FPVDrone.FPVModel modelFilter = FPVDrone.FPVModel.valueOf(fpvModel.toUpperCase());
            fpvReports = fpvReportService.findAllByFPVModel(modelFilter);
        } else if (isLostFPVDueToREB != null) {
            fpvReports = fpvReportService.findAllByIsLostFPVDueToREB(isLostFPVDueToREB);
        } else if ("isOnTargetFPV".equals(sortCriteria) && isOnTargetFPV != null ) {
            fpvReports = fpvReportService.findAllByIsOnTarget(isOnTargetFPV);
        } else if ("dateTimeFlight".equals(sortCriteria)) {
            fpvReports = fpvReportService.findAllSortedByDate();
        } else if ("fpvPilotName".equals(sortCriteria)) {
            fpvReports = fpvReportService.findAllSortedByPilotName();
        } else if ("fpvPilotSurName".equals(sortCriteria)) {
            fpvReports = fpvReportService.findAllSortedByPilotSurName();
        } else {
            fpvReports = fpvReportService.findAll();
        }

        model.addAttribute("fpvReports", fpvReports);
        return "form/allFPVReports";
    }*/

    //work
    @GetMapping("/fpvReports")
    public String deleteFPVReportsForm(Model model) {
        model.addAttribute("fpvReports", fpvReportServiceImpl.findAll());
        return "form/deleteFPVReports";
    }

    //work
    @PostMapping("/submitToDeleteFPVReports")
    public String deleteFPVReportsByIds(@RequestParam(name = "ids", required = false) List<Long> ids, Model model) {
        if (ids != null && !ids.isEmpty()) {
            fpvReportServiceImpl.deleteAllByIds(ids);
            model.addAttribute("message", "Selected FPV Reports were deleted successfully!");
        } else {
            model.addAttribute("message", "No FPV Reports selected for deletion!");
        }
        return "result/deleteFPVReport";
    }

    //work
    @GetMapping("/fpvReport/delete")
    public String deleteFPVReportByIdForm() {
        return "form/deleteFPVReport";
    }

    //work
    @PostMapping("/submitToDeleteFPVReport")
    public String deleteFPVReportById(@RequestParam(name = "id") Long id, Model model) {
        if (fpvReportServiceImpl.existsById(id)) {
            fpvReportServiceImpl.deleteById(id);
            model.addAttribute("message", "FPV Report with ID " + id + " was deleted successfully!");
        } else {
            model.addAttribute("message", "FPV Report with ID " + id + " not found!");
        }
        return "result/deleteFPVReport";
    }

/*    @GetMapping("/sortedByDate")
    public String getReportsSortedByDate(Model model) {
        List<FPVReport> fpvReports = fpvReportRestService.findAllSortedByDate();
        model.addAttribute("fpvReports", fpvReports);
        return "form/allFPVReports";
    }*/

/*    @GetMapping("/sortedByPilotSurName")
    public String getReportsSortedByPilotSurName(Model model) {
        List<FPVReport> fpvReports = fpvReportRestService.findAllSortedByPilotSurName();
        model.addAttribute("fpvReports", fpvReports);
        return "form/allFPVReports";
    }*/

/*
    @GetMapping("/sortedByPilotName")
    public String getReportsSortedByPilotName(Model model) {
        List<FPVReport> fpvReports = fpvReportRestService.findAllSortedByPilotName();
        model.addAttribute("fpvReports", fpvReports);
        return "form/allFPVReports";
    }
*/

}
