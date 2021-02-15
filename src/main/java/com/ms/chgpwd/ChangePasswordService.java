package com.ms.chgpwd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Service
public class ChangePasswordService {
	private static final Logger logger = LogManager.getLogger();
	
	@Autowired
	private ChangePasswordDao changePasswordDao;
	
	public void validateForm(ChangePasswordForm form, Errors errors, String username) {
		try {
			if (form.getCurrentPassword() == null || form.getCurrentPassword().isEmpty()) {
				errors.rejectValue("currentPassword", "chgpwd.error.common.required");
			} else {
				if (!validateCurrentPassword(username, form.getCurrentPassword()))
					errors.rejectValue("currentPassword", "chgpwd.error.currentpassword.incorrect");
			}
			if (form.getNewPassword() == null || form.getNewPassword().isEmpty()) {
				errors.rejectValue("newPassword", "chgpwd.error.common.required");
			}
			if (form.getConfirmPassword() == null || form.getConfirmPassword().isEmpty()) {
				errors.rejectValue("confirmPassword", "chgpwd.error.common.required");
			}
			if (form.getNewPassword() != null && !form.getNewPassword().isEmpty() && form.getConfirmPassword() != null 
					&& !form.getConfirmPassword().isEmpty()) {
				if (!form.getNewPassword().equals(form.getConfirmPassword())) {
					errors.rejectValue("confirmPassword", "chgpwd.error.pwd.notmatched");
				}
			}
		} catch (Exception e) {
			logger.error("Exception when validating form", e);
			errors.reject("chgpwd.error.exception");
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	private boolean validateCurrentPassword(String username, String currentPassword) {
		String hashedPassword = changePasswordDao.getCurrentPassword(username);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(currentPassword, hashedPassword);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void updatePassword(String username, String newRawPassword) {
		changePasswordDao.updatePassword(username, bcryptHash(newRawPassword));
	}
	
	private static String bcryptHash(String rawPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(rawPassword);
	}
}
