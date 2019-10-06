package com.hibernate.many_to_many.both;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hibernate.many_to_many.both.entity.Categoty;
import com.hibernate.many_to_many.both.entity.Item;

//多对多
class ManyToManyTest {

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
	public void ManyToManySaveTest() {
		Categoty categoty1 = new Categoty();
		categoty1.setCategotyName("categotyName-AA");
		
		Categoty categoty2 = new Categoty();
		categoty2.setCategotyName("categotyName-BB");
		
		Item item1 = new Item();
		item1.setItemName("itemName-AA");
		
		Item item2 = new Item();
		item2.setItemName("itemName-BB");
		
		//设定关系
		categoty1.getItems().add(item1);
		categoty1.getItems().add(item2);
		categoty2.getItems().add(item1);
		categoty2.getItems().add(item2);
		
		//执行保存操作
		session.save(categoty1);
		session.save(categoty2);
		session.save(item1);
		session.save(item2);
	}

}
