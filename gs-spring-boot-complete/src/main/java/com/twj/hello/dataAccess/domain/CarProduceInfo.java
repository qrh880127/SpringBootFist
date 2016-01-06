package com.twj.hello.dataAccess.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarProduceInfo {
	private Date time;		//生产时间
	private String address;	//生产地址
	private String batchId;	//生产批号
	private String checker;	//质检人
}
