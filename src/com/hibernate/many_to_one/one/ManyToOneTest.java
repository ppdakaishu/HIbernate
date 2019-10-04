package com.hibernate.many_to_one.one;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hibernate.many_to_one.one.entity.AccInfo;
import com.hibernate.many_to_one.one.entity.SealCard;

//单向一对多
class ManyToOneTest {

	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	
	@BeforeEach
	public void Init() {
		//声明 ServiceRegistry
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();		
		//创建 SessionFactory
		sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
		//创建 Session
		session = sessionFactory.openSession();
		//创建 Transaction
		transaction = session.beginTransaction();
		
		System.out.println("init complete..");
	}
	
	@AfterEach
	public void destroy() {
		//提交事务
		transaction.commit();
		//关闭 Session
		session.close();
		//关闭 SessionFactory
		sessionFactory.close();
		
		System.out.println("destroy complete..");
	}
	
	@Test
	public void manyToOneDeleteTest() {
		//在不设定级联属性情况下, 同时 one 一端的对象有 many 一端的对象在引用, 
		//不能直接删除 one 一端的对象
		AccInfo accInfo = session.get(AccInfo.class, 7);
		session.delete(accInfo);
	}
	
	@Test
	public void manyToOneUpdateTest() {
		SealCard sealCard = session.get(SealCard.class, 8);
		sealCard.getAccInfo().setAccName("accInfo-p");
	}
	
	@Test
	public void manyToOneGetTest() {
		//1. 若查询 many 一端的对象, 默认情况下只查询 many 的一端. 而不查询关联的 one 那一端的对象
		SealCard sealCard = session.get(SealCard.class, 8);
		System.out.println(sealCard.getCardNo());
		
		//获取 many 一端对象时, 默认情况下, 其关联的 one 一端的对象是个代理对象
		System.out.println(sealCard.getAccInfo().getClass().getName());
		
		//2. 若需要使用到关联对象时, 才发送对应的 SQL 语句查询. [ 懒加载 ]
		// 	  若在此之前 session 已被关闭, 则默认情况下, 会发生 lazyInitializationException 异常
		AccInfo accInfo = sealCard.getAccInfo();
		System.out.println(accInfo.getAccName());
	}
	
	@Test
	public void manyToOneSaveTest() {
		//one
		AccInfo accInfo = new AccInfo();
		accInfo.setAccName("accInfo-2");
		
		//many
		SealCard sealCard1 = new SealCard();
		sealCard1.setCardNo("cardNo-3");
		sealCard1.setAccInfo(accInfo);
		
		SealCard sealCard2 = new SealCard();
		sealCard2.setCardNo("cardNo-4");
		sealCard2.setAccInfo(accInfo);
		
		//先保存 one 的一端, 再保存 many 的一端 [ 推荐 : 效率较高 ]
		//只有 3 条 INSERT语句
		session.save(accInfo);
		session.save(sealCard1);
		session.save(sealCard2);
		
		//先保存 many 的一端, 再保存 one 的一端
		//3 条 INSERT语句 + 2 条 UPDATE 语句
		//因为在插入 many 的一端时, 无法确认 one 的一端的外建值. 
		//所以只能等 one 的一端插入后, 再额外发送 UPDATE 语句
//		session.save(sealCard1);
//		session.save(sealCard2);
//		session.save(accInfo);
	}

}
