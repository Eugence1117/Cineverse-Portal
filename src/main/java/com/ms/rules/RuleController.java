package com.ms.rules;

import com.ms.common.Response;
import com.ms.login.Staff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RuleController {

    public static Logger log = LogManager.getLogger(RuleController.class);

    @Autowired
    RuleService service;

    @RequestMapping(value ="/api/manager/editOperatingHour.json")
    @ResponseBody
    public Response editOperatingHour(Model model, @RequestBody OperatingTimeRange range){
        log.info("Entered /manager/editOperatingHour.json");
        Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return service.editOperatingHours(user.getBranchid(),range);
    }
}
