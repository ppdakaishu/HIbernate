package com.hibernate.session;

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

import com.hibernate.helloworld.entity.Person;

//操作缓存测试
class OperationCacheTest {

	private SessionFactory sessionFactory;
	//注 : 实际应用中, 下列属性不能作为成员变量使用. 可能会导致并发问题
	private Session session;
	private Transaction transaction;
	
	@BeforeEach
	public void Init() {
		//加载配置文件
		Configuration cfg = new Configuration().configure();
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
	
	//clear() : 清理缓存
	@Test
	public void testSessionClear() {
		Person person = session.get(Person.class, 4);
		System.out.println(person);
		
		session.clear();
		//清理缓存后, 会再发送一次查询语句
		Person person2 = session.get(Person.class, 4);
		System.out.println(person2);
	}
	
	//refresh() : 会强制发送 SELEST 语句
	//			    以使 Session 缓存中对象的状态和数据表中对应的记录保持一致
	@Test
	public void testSessionRefresh() {
		Person person = session.get(Person.class, 4);
		System.out.println(person);
		
		//为了测试, 在此处打断点. 手动修改数据库记录
		session.refresh(person);
		System.out.println(person);
	}
	
	//flush() : 使数据表中的记录和 Session 缓存中的对象状态保持一致. 
	//		    为了保持一致, 则可能会发送对应的 SQL 语句. 
	//1. 在 Transaction 的 commit() 方法中 : 
	//		1). 先调用 Session 的 flush 方法
	//		2). 再提交事务
	//2. flush() 方法可能会发送 SQL 语句, 但不会提交事务. 
	
	//注意 : 
	//	1. 执行 HQL 或 QBC 方法查询, 会先进行 flush() 操作, 以得到数据表的最新的记录. 
	//	2. 若记录的主键是由底层数据库使用自增方式生成的, 则在调用 save() 方法时, 就会立即发送 INSERT 语句. 
	//		因为 save() 方法后, 必须保证对象的主键是存在的. 
	@Test
	public void testSessionFlush() {
		Person person = session.get(Person.class, 4);
		person.setCompany("Yzj");
		
		//使用 QBC 方式进行查询, 会在事务 commit 之前执行 flush() 以确保得到的是最新的记录. 
		Person person2 = (Person) session.createCriteria(Person.class).uniqueResult();
		//此时, 打印的对象中. Company字段已更新为 Yzj. 但数据库还未进行更新. 
		System.out.println(person2);
	}
	
	@Test
	public void test() {
		
		Person person = session.get(Person.class, 4);
		System.out.println(person);
		
		Person person2 = session.get(Person.class, 4);
		System.out.println(person2);
		
	}

}
