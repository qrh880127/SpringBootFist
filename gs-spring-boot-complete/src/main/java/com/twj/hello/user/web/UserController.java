package com.twj.hello.user.web;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twj.hello.dataAccess.web.MongoDbTest;
import com.twj.hello.user.domain.User;

@RestController
@RequestMapping(value="/users")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private MongoDbTest mongoDbTest;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    
    
    @Autowired
    private JavaMailSender	javaMailSender;
	
    /**
     * 获取用户信息
     * 
     * url:	http://localhost:8080/users/1
     * 
     * @PathVariable
     * 当使用@RequestMapping样式映射时， 即 someUrl/{paramId}, 这时的paramId可通过 @Pathvariable注解绑定它传过来的值到方法的参数上。
     * 
     * @param userId
     * @return
     */
	@RequestMapping(value="/{userId}")
	public User getUser(@PathVariable String userId) {
		List<User> users = (List<User>) redisTemplate.opsForValue().get("users");
		if(users == null) {
			return null;
		}
		for(User u : users) {
			if(userId.equals(u.getId())) {
				//找到
				return u;
			}
		}
		
		
		//Email test
		//http://docs.spring.io/spring/docs/4.1.4.RELEASE/spring-framework-reference/htmlsingle/#mail-usage-simple
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("qq8903239@163.com");
        msg.setTo("357722136@qq.com");
        msg.setText("qrh spring boot test");
		javaMailSender.send(msg);
		
		return users.get(0);
	}
	
	/**
	 * 新增用户
	 *	/users/add?username=qrh&password=qrh
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/add")
	public String addUser(User user) {
		user.setId(UUID.randomUUID().toString());
		List<User> users = (List<User>) redisTemplate.opsForValue().get("users");
		if(users == null) {
			users = new LinkedList<>();
		}
		
		for(User u : users) {
			if(user.getUsername().equals(u.getUsername())) {
				//有user了
				return "有user了";
			}
		}
		users.add(user);
		redisTemplate.opsForValue().set("users", users);
		
		return "ok";
	}
	
	/**
	 * 删除用户
	 * 
	 * url:	http://localhost:8080/users/del/1
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/del/{userId}")
	public User delUser(@PathVariable String userId) {
		
		User u = new User();
		u.setId(userId);
		return u;
	
		
	}
	
	@RequestMapping(value="/xxs/{u}")
	public String xxx(@PathVariable String u) {
		return u;
	}

	
	/**
	 * 设备探测
	 * 	pom.xml引入：spring-mobile-device
	 * @param device
	 * @return
	 */
	@RequestMapping(value="/deviceDetect")
	public String deviceDetect(Device device) {
		String deviceType = "unknown";
        if (device.isNormal()) {
            deviceType = "normal";
        } else if (device.isMobile()) {
            deviceType = "mobile";
        } else if (device.isTablet()) {
            deviceType = "tablet";
        }
		return deviceType;
	}
	

	@RequestMapping(value="/addX")
	public void addX() throws Exception {
		mongoDbTest.test1();
	}

	@RequestMapping(value="/go")
	public void ddd() {
		Object ls = redisTemplate.opsForValue().get("RATEDATA.C.SET");
		Object l = redisTemplate.opsForValue().get("RATEDATA.C.TXN.10012195267");
		System.out.println(l);
	}
	
	

	@RequestMapping(value="/sess")
	public void sessionTest(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String sId = session.getId();
		//session.setAttribute("msg", "msg");
		
		String sIdKey = "spring:session:sessions:" + sId;
		Boolean a = stringRedisTemplate.hasKey(sIdKey);
		
		Boolean hasKey = redisTemplate.hasKey(sIdKey);
		Object f = session.getAttribute("msg");

		//Object lsd = stringRedisTemplate.opsForValue().get(sIdKey);
		Object ls = redisTemplate.opsForValue().get(sIdKey);
		
		
		System.out.println(ls);
		
		System.out.println();
	}

	@RequestMapping(value="/logout")
	public void logout(HttpSession session) {
		session.invalidate();
		System.out.println();
	}


	@RequestMapping(value="/sendMsg")
	public void sendMsg(HttpSession session) {
		//stringRedisTemplate.convertAndSend("channel", new Random().nextInt(10000));
		//redisTemplate.convertAndSend("channel", new Random().nextInt(10000));
		redisTemplate.convertAndSend("channel", new User());
		System.out.println();
	}
}
