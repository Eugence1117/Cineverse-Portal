package com.ms.login;

import javax.persistence.AttributeConverter;

public enum UserStatus {
	INACTIVE(0), ACTIVE(1), SUSPENDED(2);
	
	private final int value;
	
	private UserStatus(int value) {
		this.value = value;
	}
	
	public static UserStatus valueOf(int value) {
		switch (value) {
		case 0: return INACTIVE;
		case 1: return ACTIVE;
		case 2: return SUSPENDED;
		default: throw new IllegalArgumentException();
		}
	}
	
	public int getValue() {
		return value;
	}
	
	@javax.persistence.Converter
	public static class Converter implements AttributeConverter<UserStatus, Integer> {

		@Override
		public Integer convertToDatabaseColumn(UserStatus attribute) {
			return attribute.getValue();
		}

		@Override
		public UserStatus convertToEntityAttribute(Integer dbData) {
			return UserStatus.valueOf(dbData);
		}
		
	}
	
}