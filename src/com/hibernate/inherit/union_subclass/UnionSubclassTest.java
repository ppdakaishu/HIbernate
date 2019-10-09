package com.hibernate.inherit.union_subclass;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hibernate.inherit.union_subclass.entity.Car;
import com.hibernate.inherit.union_subclass.entity.Person;

//继承映射
class UnionSubclassTest {

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
	
	//缺点 : 
	// 使用了辨别者列
	// 子类独有字段不能添加非空约束
	// 继承层次较深, 则数据表的字段会较多
	
	//对于父类记录, 只需要查一张数据表
	//对于子类记录, 也只需要查一张数据表
	@Test
	public void GetTest() {
		List<Person> list = session.createQuery("from Person").list();
		System.out.println(list.size());
	}
	
	//对于子类对象只需要把记录查到一张数据表中
	//辨别者列由 hibernate 自动维护
	@Test
	public void SaveTest() {
		Person person = new Person();
		person.setName("Cbw");
		person.setAge("11");
		
		Car car = new Car();
		car.setCarNumber(1);
		
		session.save(person);
		session.save(car);
	}

}
