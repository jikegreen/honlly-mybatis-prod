/**
 * 
 */
package com.honlly;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.honlly.business.dao.mybatis.MessageWaitSendDao;
import com.honlly.business.domain.entity.mybatis.MessageWaitSend;
import com.honlly.common.BaseTest;

/**
 * @date 2016年3月17日 下午4:47:01 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public class MessageWaitSendTest extends BaseTest {
	@Autowired
	private MessageWaitSendDao messageWaitSendDao;
	
	@Test
	public void select(){
		MessageWaitSend entity = new MessageWaitSend();
		entity.setContent("哈哈,测试,傻白甜！！！");
		entity.setAcceptTime(new Date());
		entity.setMobilePhones("13646874892");
		entity.setRemark("备注");
		entity.setSendNum(BigDecimal.ONE);
		entity.setSource("110");
		entity.setStatus(BigDecimal.ONE);
		entity.setTotalPhoneNumber(BigDecimal.ONE);
		entity.setChecked(BigDecimal.ONE);
		messageWaitSendDao.save(entity);
		System.out.println(entity.getId());
	}
	
	@Test
	public void batchSave(){
		List<MessageWaitSend> list = new ArrayList<MessageWaitSend>();
		MessageWaitSend entity = new MessageWaitSend();
		entity.setContent("哈哈,测试,傻白甜！！！");
		entity.setAcceptTime(new Date());
		entity.setMobilePhones("13646874892");
		entity.setRemark("备注");
		entity.setSendNum(BigDecimal.ONE);
		entity.setSource("1100");
		entity.setStatus(BigDecimal.ONE);
		entity.setTotalPhoneNumber(BigDecimal.ONE);
		entity.setChecked(BigDecimal.ONE);
		MessageWaitSend entity1 = entity.simpleClone();
		entity1.setSource("abc");
		list.add(entity);
		list.add(entity1);
		messageWaitSendDao.saves(list);
		System.out.println("entity id:" + entity.getId());
		System.out.println("entity1 id:" + entity1.getId());
	}
}
