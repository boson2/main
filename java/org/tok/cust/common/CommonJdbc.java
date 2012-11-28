package org.tok.cust.common;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * jdbcTemplate Wrapper 클래스.
 * 
 * @author snoopy
 * @version 1.0.0
 * @since 1.0-SNAPSHOT
 */
public class CommonJdbc {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	protected final JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}
}
