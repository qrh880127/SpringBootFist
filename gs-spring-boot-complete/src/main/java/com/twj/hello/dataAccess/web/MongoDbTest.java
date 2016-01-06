package com.twj.hello.dataAccess.web;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.twj.hello.dataAccess.domain.Car;
import com.twj.hello.dataAccess.domain.CarProduceInfo;
import com.twj.hello.dataAccess.mongodb.dao.CarRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MongoDbTest {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private CarRepository carRepository;
	
	public void testMongoTemplate() {
		mongoTemplate.dropCollection(Car.class);
		
		//create collection
		mongoTemplate.createCollection(Car.class);
		
		//save
		Car car = new Car("特斯拉", "马斯克");
		CarProduceInfo info = new CarProduceInfo(new Date(), "address1", "batchId1", "checker1");
		car.setCarProduceInfo(info);
		mongoTemplate.save(car);
		car = new Car("特斯拉", "马斯克2");
		info = new CarProduceInfo(new Date(), "address2", "batchId2", "checker2");
		car.setCarProduceInfo(info);
		mongoTemplate.save(car);
		car = new Car("特斯拉", "马斯克3");
		info = new CarProduceInfo(new Date(), "address3", "batchId3", "checker3");
		car.setCarProduceInfo(info);
		mongoTemplate.save(car);
		
		//query
		List<Car> qs = mongoTemplate.find(new Query(where("carProduceInfo.checker").is("checker1")), Car.class);
		qs.forEach(c -> {
			log.info(c.toString());
		});

		log.info("testMongoTemplate over");
	}
	
	public String test1() throws Exception {
		carRepository.deleteAll();
		Car car = new Car("特斯拉", "马斯克");
		CarProduceInfo info = new CarProduceInfo(new Date(), "address1", "batchId1", "checker1");
		car.setCarProduceInfo(info);
		Thread.sleep(50);
		Car car2 = new Car("宾利", "北京");
		CarProduceInfo info2 = new CarProduceInfo(new Date(), "address2", "batchId2", "checker2");
		car2.setCarProduceInfo(info2);
		carRepository.save(car);
		carRepository.save(car2);
		
		List<Car> cars = carRepository.findAll();
		log.info(cars.toString());
		
		List<Car> cs = carRepository.queryByName("特斯拉");
		log.info(cs.toString());

		
		cs = carRepository.queryByFactory("北京");
		log.info(cs.toString());
		
		//级联查询
		cs = carRepository.queryByCarProduceInfo_checker("checker1");
		log.info(cs.toString());
		
		cs = carRepository.queryByCarProduceInfo_checkerLike("checker");
		log.info(cs.toString());
		
		cs = carRepository.queryByCarProduceInfo_checkerLikeOrderByCarProduceInfo_TimeDesc("checker");
		log.info(cs.toString());
		log.info("over");

		car = new Car("特斯拉", "马斯克2");
		info = new CarProduceInfo(new Date(), "address1", "batchId1", "checker1");
		car.setCarProduceInfo(info);
		carRepository.save(car);
		Thread.sleep(50);
		car = new Car("特斯拉", "马斯克3");
		info = new CarProduceInfo(new Date(), "address1", "batchId1", "checker1");
		car.setCarProduceInfo(info);
		carRepository.save(car);
		Thread.sleep(50);
		car = new Car("特斯拉", "马斯克4");
		info = new CarProduceInfo(new Date(), "address1", "batchId1", "checker1");
		car.setCarProduceInfo(info);
		carRepository.save(car);
		Thread.sleep(50);
		
		log.info("\nSlice");
		Slice<Car> csps = carRepository.queryByNameLike("特斯拉", new PageRequest(0, 2, new Sort(Sort.Direction.DESC, "carProduceInfo_time")));
		csps.forEach(c -> {
			log.info(c.toString());
		});
		while(csps.hasNext()) {
			Pageable nextPage = csps.nextPageable();
			csps = carRepository.queryByNameLike("特斯拉", nextPage);
			csps.forEach(c -> {
				log.info(c.toString());
			});
		}

		log.info("\n 限制结果集，First/Top");
		Car c = carRepository.findFirstByNameLike("特斯拉");
		log.info("First1:" + c.toString());

		List<Car> cd = carRepository.queryTop3ByNameLike("特斯拉");
		log.info("Top3:" + cd.toString());
		

		log.info("\n 流化 Stream");
		Stream<Car> str = carRepository.readAllByName("特斯拉");
		str.forEach(ca -> {
			log.info(ca.toString());
		});
		
		//Stream过滤factory的长度大于3的item
		log.info("Stream过滤factory的长度大于3的item");
		str = carRepository.readAllByName("特斯拉");
		str.filter(ca -> ca.getFactory().length() > 3)
		.forEach(ca -> {
			log.info(ca.toString());
		});

		log.info("异步查询");
		Future<Stream<Car>> ret = carRepository.readByName("特斯拉");
		log.info("do others");
		Stream<Car> retStream = ret.get();
		retStream.forEach(t -> {
			log.info(c.toString());
		});

		testMongoTemplate();
		return "cc";
		//DBCollection carCollection = mongoTemplate.createCollection(Car.class);
		
		//Car car = new Car();
		//carCollection.save(car);
		
	}
}
