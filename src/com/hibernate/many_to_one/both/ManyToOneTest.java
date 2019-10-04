package com.hibernate.many_to_one.both;

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

import com.hibernate.many_to_one.both.entity.AccInfo;
import com.hibernate.many_to_one.both.entity.SealCard;

//双向一对多
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
		AccInfo accInfo = session.get(AccInfo.class, 47);
//		accInfo.getSealCards().clear();
	}
	
	@Test
	public void manyToOneUpdateTest() {
		AccInfo accInfo = session.get(AccInfo.class, 25);
		accInfo.getSealCards().iterator().next().setCardNo("cardNo-p");
	}
	
	@Test
	public void manyToOneGetTest() {
		//1. 对 many 一端的集合使用延迟加载
		AccInfo accInfo = session.get(AccInfo.class, 40);
		System.out.println(accInfo.getAccName());
		//2. 返回 many 一端的集合是 hibernate 内置的集合类型. 
		//	  该类型具有延迟加载和存放代理对象的功能.
		System.out.println(accInfo.getSealCards().getClass());
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
		
		// 设定关系
		accInfo.getSealCards().add(sealCard1);
		accInfo.getSealCards().add(sealCard2);
		
		//先插入 AccInfo, 再插入 SealCard
		//3 条 INSERT语句, 两条 UPDATE语句
		//因为 one 一端和 many 一端都需要维护关系, 所以多出两条 UPDATE
		//
		//可以在 one 一端的 set 节点指定 inverse="true", 来使 one 的一端放弃维护关联关系
		//建议设定 set 的 inverse="true", 先插入 one 一端, 再插入 many 一端.
		//好处是不会多出 UPDATE 语句
		session.save(accInfo);
//		session.save(sealCard1);
//		session.save(sealCard2);
		
		//先插入 SealCard, 再插入 AccInfo
		//3 条 INSERT语句 + 4 条 UPDATE 语句
//		session.save(sealCard1);
//		session.save(sealCard2);
//		session.save(accInfo);
	}

}
