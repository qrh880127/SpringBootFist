package com.twj.hello.dataAccess.mongodb.dao;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;

import com.twj.hello.dataAccess.domain.Car;

/**
 * 参考：http://docs.spring.io/spring-data/data-mongo/docs/1.8.2.RELEASE/reference/html/
 * 
 * 例子：
 * emailAddress and lastname
 * List<Person> findByEmailAddressAndLastname(EmailAddress emailAddress, String lastname);

  // Enables the distinct flag for the query
  List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
  List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);

  //忽略大小写
  List<Person> findByLastnameIgnoreCase(String lastname);
  //忽略大小写，多个字段
  List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);

  //Order by 排序
  List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
  List<Person> findByLastnameOrderByFirstnameDesc(String lastname);
 * @author ruihua.qin
 *
 */
public interface CarRepository extends MongoRepository<Car, Integer>{
	public List<Car> queryByName(String name);
	public List<Car> queryByFactory(String factory);
	
	//car.carProduceInfo.checker
	public List<Car> queryByCarProduceInfo_checker(String checker);
	//car.carProduceInfo.checker like %xxxx%
	public List<Car> queryByCarProduceInfo_checkerLike(String checker);
	//car.carProduceInfo.checker like %xxxx% order by car.carProduceInfo.time desc
	public List<Car> queryByCarProduceInfo_checkerLikeOrderByCarProduceInfo_TimeDesc(String checker);
	
	
	
	/*
	 * 分页/切片/排序	Pageable, Slice and Sort
	 * 
	 * 分页查询最好用Slice，通过hasNext(),nextPageable()进行分页遍历
	 * 排序Sort在Pageable中设置
	 */
	public Page<Car> queryByName(String name, Pageable pageable);
	public Slice<Car> queryByNameLike(String name, Pageable pageable);
	
	/*
	 * 限制查询结果集数量First/Top
	 * 
	 */
	public Car findFirstByNameLike(String name);
	public List<Car> queryTop3ByNameLike(String name);	//前3个
	
	
	//流化 Stream
	public Stream<Car> readAllByName(String name);
	
	//异步查询
	@Async
	public Future<Stream<Car>> readByName(String name);
}
