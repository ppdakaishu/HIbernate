package com.hibernate.one_to_one.foreign;

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
import com.hibernate.one_to_one.foreign.entity.Citizen;
import com.hibernate.one_to_one.foreign.entity.IdentityCard;

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
	public void oneToOneDeleteTest() {
	}
	
	@Test
	public void oneToOneUpdateTest() {
		
	}
	
	@Test
	public void oneToOneGet2Test() {
		//在查询没有外键的实体对象时, 使用左外连接查询, 一并查出其关联对象并初始化
		IdentityCard identityCard = session.get(IdentityCard.class, 60);
		System.out.println(identityCard.getCardNo());
		System.out.println(identityCard.getCitizen().getCitizenName());
	}
	
	@Test
	public void oneToOneGetTest() {
		//默认情况下会对关联属性使用延迟加载, 所以可能会出现延迟加载异常
		Citizen citizen = session.get(Citizen.class, 61);
		System.out.println(citizen.getCitizenName());
		
		IdentityCard identityCard = citizen.getIdentityCard();
		System.out.println(identityCard.getCardNo());
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
		//建议先保存没有外键列的对象, 这样会减少更新语句
		session.save(identityCard);
		session.save(citizen);
	}

}
