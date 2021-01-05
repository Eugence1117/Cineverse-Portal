package com.ms.login;

import java.util.Scanner;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class BCryptPasswordGenerator {
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
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
		
		System.out.println("Enter encrpted password: ");
		try(Scanner scanner = new Scanner(System.in)){
			String encrptyPas = scanner.nextLine();
		}
	}
}
