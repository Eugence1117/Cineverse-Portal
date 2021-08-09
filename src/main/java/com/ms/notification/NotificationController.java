package com.ms.notification;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NotificationController {
	
	@RequestMapping(value = {"/viewNotification.htm"})
	public String getNotificationPage(Model model) {
		return "viewNotification";
	}
}
