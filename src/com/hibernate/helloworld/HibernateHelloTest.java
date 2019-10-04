package com.hibernate.helloworld;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;

import com.hibernate.helloworld.entity.Person;

class HibernateHelloTest {

	@Test
	void test() {
		
		//[1]. 初始化并加载配置文件
		Configuration cfg = new Configuration().configure();
		
		//[2]. 创建 SessionFactory
		//Before Hibernate 4.2
//		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
//		SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		//Hibernate 4.3 
//		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
//		SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		//Hibernate 5.2
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();		
		SessionFactory sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
		
		//[3]. 创建 Session 对象
		Session session = sessionFactory.openSession();
		//[4]. 开启事务
		Transaction transaction = session.beginTransaction();
		//[5]. 执行保存操作
		Person person = new Person("Cbw", "Yzj", 25, new Date());
		session.save(person);
		//[6]. 提交事务
		transaction.commit();
		//[7]. 关闭 Session
		session.close();
		//[8]. 关闭 SessionFactory
		sessionFactory.close();
		
	}

}
