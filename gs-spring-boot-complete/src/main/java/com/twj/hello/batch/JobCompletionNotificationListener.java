package com.twj.hello.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 创建数据库表
	 */
	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Job batchA start!");
		
		log.info("create tables by sql");
		jdbcTemplate.execute("create table Person (id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255), age integer)");
	};

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Job batchA end!");
			log.info("validate data start~");

			List<Person> results = jdbcTemplate.query("SELECT first_name, last_name, age FROM Person", new RowMapper<Person>() {
				@Override
				public Person mapRow(ResultSet rs, int row) throws SQLException {
					return new Person(rs.getString(1), rs.getString(2), rs.getInt(3));
				}
			});

			for (Person person : results) {
				log.info("Found <" + person + "> in the database.");
			}

			log.info("validate data end~");
		}
		
		log.info("drop table Person");
		jdbcTemplate.execute("drop table Person");
	}
}
