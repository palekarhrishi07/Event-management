package com.event_management.event_management_system_backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialsDto {

    private String username;
    private char[] password;
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	public char[] getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

}
