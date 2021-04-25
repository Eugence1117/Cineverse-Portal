package com.ms.chgpwd;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChangePasswordDao {
	
	private JdbcTemplate jdbc;
	
	@Autowired
	public void setJdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
	    this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public String getCurrentPassword(String username) {
		String sql = "SELECT password FROM fl_users WHERE userid = ?";
		return jdbc.queryForObject(sql, String.class, username);
	}
	
	public void updatePassword(String username, String password) {
		String sql = "UPDATE fl_users SET password = ? WHERE userid = ?";
		jdbc.update(sql, password, username);
	}
}
