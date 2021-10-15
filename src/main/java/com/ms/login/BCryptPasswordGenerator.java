package com.ms.login;

import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ms.common.Constant;


public class BCryptPasswordGenerator {
	
	public static void main(String[] args) {
		
		//System.out.println("Format :" + getFileFormat("https://cineversefiles.blob.core.windows.net/profilepic/3822675f-17e1-4ea4-bca1-72a371664946.gif"));
		
		//System.out.println(UUID.fromString().toString());
		String rawPassword;
		if (args.length >= 1) {
			rawPassword = args[0];
		} else {
			//System.out.println("UUID : " + Utils.getUUID());
			System.out.println("Enter raw password: ");
			try (Scanner scanner = new Scanner(System.in)) {
				rawPassword = scanner.nextLine();
			}
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode(rawPassword));
	}
	
	public static String getFileFormat(String filename) {
		String format = "";
		Pattern ptn = Pattern.compile(Constant.FILE_PATTERN);
		Matcher matcher = ptn.matcher(filename);
		
		while(matcher.find()) {
			format = matcher.group();
		}
		
		return format;
	}
}
