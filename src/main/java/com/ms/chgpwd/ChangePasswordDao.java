package com.ms.chgpwd;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChangePasswordDao {
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public String getCurrentPassword(String username) {
		String sql = "SELECT password FROM fl_users WHERE userid = ?";
		return jdbcTemplate.queryForObject(sql, String.class, username);
	}
	
	public void updatePassword(String username, String password) {
		String sql = "UPDATE fl_users SET password = ? WHERE userid = ?";
		jdbcTemplate.update(sql, password, username);
	}
}
