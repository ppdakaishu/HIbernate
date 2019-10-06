package com.hibernate.one_to_one.primary;

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

import com.hibernate.one_to_one.primary.entity.Citizen;
import com.hibernate.one_to_one.primary.entity.IdentityCard;

//一对一
class OneToOneTest {

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
	public void oneToOneGet2Test() {

	}
	
	@Test
	public void oneToOneGetTest() {

	}
	
	@Test
	public void oneToOneSaveTest() {
		Citizen citizen = new Citizen();
		citizen.setCitizenName("citizenName-AA");
		
		IdentityCard identityCard = new IdentityCard();
		identityCard.setCardNo("cardNo-AA");
		
		//设定关系
		citizen.setIdentityCard(identityCard);
		identityCard.setCitizen(citizen);
		
		//保存操作
		//无论先插入哪一个都不会有多余的更新语句
		session.save(identityCard);
		session.save(citizen);
	}

}
