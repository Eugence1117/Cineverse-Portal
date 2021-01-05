package com.ms.chgpwd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ms.common.Constant;
import com.ms.login.Staff;


@Controller
public class ChangePasswordController {
	private static final Logger logger = LogManager.getLogger(ChangePasswordController.class);
	
	@Autowired
	private ChangePasswordService changePasswordService;
	
	@RequestMapping(value="/changepassword.htm", method=RequestMethod.GET)
	public String getChangePassword(Model model) {
		if (!model.containsAttribute("form"))
			model.addAttribute("form", new ChangePasswordForm());
		model.addAttribute("PASSWORD_MIN_LENGTH", Constant.PASSWORD_MIN_LENGTH);
		model.addAttribute("PASSWORD_MAX_LENGTH", Constant.PASSWORD_MAX_LENGTH);
		return "changepassword";
	}
	
	@RequestMapping(value="/changepassword.htm", method=RequestMethod.POST)
	public String postChangePassword(@ModelAttribute("form") ChangePasswordForm form, BindingResult binding, RedirectAttributes attr) {
		Staff user = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		changePasswordService.validateForm(form, binding, user.getUsername());
		if (binding.hasErrors()) {
			attr.addFlashAttribute("form", form);
			attr.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "form", binding);
			return "redirect:/changepassword.htm";
		}
		
		try {
			changePasswordService.updatePassword(user.getUsername(), form.getNewPassword());
		} catch (Exception e) {
			logger.error("Exception when changing password", e);
			binding.reject("chgpwd.error.exception");
		}
		attr.addFlashAttribute("success", true);
		return "redirect:/changepassword.htm";
	}
}
