/**
 * 
 */
package com.honlly;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.honlly.business.dao.mybatis.MessageWaitSendDao;
import com.honlly.business.domain.entity.mybatis.MessageWaitSend;
import com.honlly.business.domain.entity.mybatis.MessageWaitSend.MessageWaitSendMeta;
import com.honlly.common.BaseTest;
import com.honlly.commons.domain.Pagination;
import com.honlly.mybatis.utils.JsonUtil;

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
	public void save(){
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
	public void saveAsMap(){
		Map<String, Object> params = new HashMap<>();
		params.put(MessageWaitSendMeta.id, 123098);
		params.put(MessageWaitSendMeta.content, "哈哈,测试,傻白甜！！！");
		params.put(MessageWaitSendMeta.acceptTime, new Date());
		messageWaitSendDao.saveAsMap(params);
	}
	
	@Test
	public void saves(){
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
	
	@Test
	public void getById(){
		System.out.println(JsonUtil.toJson(messageWaitSendDao.getById(27L)));
	}
	
	@Test
	public void get(){
		MessageWaitSend entity = new MessageWaitSend();
		entity.setId(27L);
		System.out.println(JsonUtil.toJson(messageWaitSendDao.get(entity)));
	}
	
	@Test
	public void deleteById(){
		messageWaitSendDao.deleteById(2L);
	}
	
	@Test
	public void delete(){
		MessageWaitSend entity = new MessageWaitSend();
		entity.setId(284L);
		messageWaitSendDao.delete(entity);
	}
	
	@Test
	public void update(){
		MessageWaitSend entity = messageWaitSendDao.getById(27L);
		System.out.println("更新前:" + entity.getContent());
		entity.setContent("大家好，我是update方法的测试内容！！");
		messageWaitSendDao.update(entity);
		MessageWaitSend entity1 = messageWaitSendDao.getById(27L);
		System.out.println("更新后:" + entity1.getContent());
	}
	
	@Test
	public void updateAsMap(){
		Long id = 26L;
		MessageWaitSend entity = new MessageWaitSend();
		entity.setId(id);
		MessageWaitSend entity1 = messageWaitSendDao.getById(id);
		Map<String, Object> params = new HashMap<>();
		params.put(MessageWaitSendMeta.content, "updateAsMap - 哈哈,测试,傻白甜！！！");
		params.put(MessageWaitSendMeta.acceptTime, new Date());
		System.out.println("更新前:" + entity1.getContent());
		messageWaitSendDao.updateAsMap(entity,params);
		MessageWaitSend entity2 = messageWaitSendDao.getById(id);
		System.out.println("更新后:" + entity2.getContent());
	}
	
	@Test
	public void query(){
		//如果不需要分页，请将第二个参数置为null
		MessageWaitSend entity = new MessageWaitSend();
		entity.setContent("哈哈,测试,傻白甜！！！");
		Pagination pagination = new Pagination(1, 3);
		Pagination pagination1 = new Pagination(2, 2);
		List<MessageWaitSend> messageWaitSends = messageWaitSendDao.query(entity, pagination);
		List<MessageWaitSend> messageWaitSends1 = messageWaitSendDao.query(entity, pagination1);
		for (MessageWaitSend messageWaitSend : messageWaitSends) {
			System.out.println(JsonUtil.toJson(messageWaitSend));
		}
		for (MessageWaitSend messageWaitSend1 : messageWaitSends1) {
			System.out.println(JsonUtil.toJson(messageWaitSend1));
		}
	}
	
	@Test
	public void count(){
		MessageWaitSend entity = new MessageWaitSend();
		entity.setContent("哈哈,测试,傻白甜！！！");
		System.out.println(messageWaitSendDao.count(entity));
	}
	
	@Test
	public void deletes(){
		List<Long> ids = new ArrayList<>();
		ids.add(3L);
		ids.add(4L);
		messageWaitSendDao.deletes(ids);
	}
	
	@Test
	public void updates(){
		List<MessageWaitSend> list = new ArrayList<>();
		MessageWaitSend entity = messageWaitSendDao.getById(6L);
		entity.setContent("updates");
		MessageWaitSend entity1 = messageWaitSendDao.getById(7L);
		entity1.setContent("updates");
		list.add(entity);
		list.add(entity1);
		messageWaitSendDao.updates(list);
	}
	
}
