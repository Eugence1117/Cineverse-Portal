package com.ms.MasterPisPortal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class SqlServerDemo{
	
	public static Logger log = LogManager.getLogger(SqlServerDemo.class);
	
	private final JdbcTemplate temp;
	
	@EventListener(ApplicationReadyEvent.class)
	public void ready() {
		List<String> idList = this.temp.query("SELECT seqid FROM staff",(resultSet,i) -> resultSet.getString("seqid"));
		idList.forEach(log::info);
	}
	
}