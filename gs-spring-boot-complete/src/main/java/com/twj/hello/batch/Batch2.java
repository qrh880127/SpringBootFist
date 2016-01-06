package com.twj.hello.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 导入用户到数据库
 * 	jobX
 * 		listener.beforeJob	:创建表Person
 * 
 * 		Step1:	导入user.csv的数据到数据库User表
 * 			1.reader	:读取item,并转为Person
 * 			2.processor	:姓名转为大写
 * 			3.writer	:写入数据库
 * 		Step2.	统计年龄>=20的用户人数
 * 			1.reader	:读Person表，为item，并转为Person
 * 			2.processor	:标记年龄>=20的用户为true
 * 			3.writer	:统计true的数量
 * 
 * 		listener.afterJob	:验证表Person的数据,删除表Person
 * 
 * 
 * @author ruihua.qin
 *
 */
@Component
@Slf4j
public class Batch2{

    // tag::Reader/Writer/Processor[]
    @Bean
    public ItemReader<Person> personItemReader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Person>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "firstName", "lastName", "age"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }

    /**
     * personItemProcessor
     */
    @Bean
    public ItemProcessor<Person, Person> personItemProcessor() {
        return new PersonItemProcessor();
    }

    @Bean
    public ItemWriter<Person> personItemWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO Person (first_name, last_name, age) VALUES (:firstName, :lastName, :age)");
        writer.setDataSource(dataSource);
        return writer;
    }
    // end::readerwriterprocessor[]
    
    
    /**
     * 计算年龄ItemReader
     * 
     * @param dataSource
     * @return
     */
    @Bean
    public ItemReader<Person> calAgeItemReader(DataSource dataSource) {
    	JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<Person>();
    	reader.setDataSource(dataSource);
    	reader.setSql("select first_name, last_name, age from Person");
    	reader.setRowMapper(new RowMapper<Person>() {
			@Override
			public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
				Person person = new Person();
				person.setFirstName(rs.getString(1));
				person.setLastName(rs.getString(2));
				person.setAge(rs.getInt(3));
				return person;
			}
		});
    	
    	return reader;
    }
    
    /**
     * calAgeItemProcessor
     * 		计算年龄Processor
     * 		如果年龄>=20返回true,否则返回false
     * @return
     */
    @Bean
    public ItemProcessor<Person, Boolean> calAgeItemProcessor() {
    	return new ItemProcessor<Person, Boolean>() {

			@Override
			public Boolean process(final Person item) throws Exception {
				return item.getAge() >= 20;
			}
		};
    }
    
    /**
     * 每处理完5条记录调用一次
     * @return
     */
    private int count;
    @Bean
    public ItemWriter<Boolean> calAgeItemWriter() {
    	return new ItemWriter<Boolean>() {
			
			@Override
			public void write(List<? extends Boolean> items) throws Exception {
				log.info("calAgeItemWriter：" + items);
				for(Boolean b : items) {
					count = b ? count++ : count;
				}
				log.info("age >=20 count:" + count);
			}
		};
    }

    // tag::jobstep[]
    @Bean
    public Job JobX(JobBuilderFactory jobs, Step step1_importPerson, Step step2_calCountOfAgeGe20, JobExecutionListener listener) {
        return jobs.get("JobX")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1_importPerson)
                .next(step2_calCountOfAgeGe20)
                .end()
                .build();
    }

    /**
     * Step1
     * 		导入用户
     * 
     * @param stepBuilderFactory
     * @param reader
     * @param writer
     * @param processor
     * @return
     */
    @Bean
    public Step step1_importPerson(StepBuilderFactory stepBuilderFactory, ItemReader<Person> personItemReader,
            ItemWriter<Person> personItemWriter, ItemProcessor<Person, Person> personItemProcessor) {
        return stepBuilderFactory.get("step1_importPerson")
                .<Person, Person> chunk(10)		//块，设置为10，每处理完10条记录，调用一次writer
                .reader(personItemReader)
                .processor(personItemProcessor)
                .writer(personItemWriter)
                .build();
    }

    /**
     * Step2
     * 		计算年龄大于等于20的用户数
     * 
     * @param stepBuilderFactory
     * @param reader
     * @param writer
     * @param processor
     * @return
     */
    @Bean
    public Step step2_calCountOfAgeGe20(StepBuilderFactory stepBuilderFactory, ItemReader<Person> calAgeItemReader,
            ItemWriter<Boolean> calAgeItemWriter, ItemProcessor<Person, Boolean> calAgeItemProcessor) {
        return stepBuilderFactory.get("step2_calCountOfAgeGe20")
                .<Person, Boolean> chunk(5)		//块，设置为5，每处理完5条记录，调用一次writer
                .reader(calAgeItemReader)
                .processor(calAgeItemProcessor)
                .writer(calAgeItemWriter)
                .build();
    }
    // end::jobstep[]
}