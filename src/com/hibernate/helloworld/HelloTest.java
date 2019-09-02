package com.hibernate.helloworld;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;

import com.hibernate.helloworld.entity.Person;

class HelloTest {

	@Test
	void test() {
		
		//[1]. 初始化配置文件
		Configuration cfg = new Configuration();
		
		//[2]. 加载配置文件
		cfg.configure();
		
		//[3]. 创建 SessionFactory 对象
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		
		//[4]. 创建 Session 对象
		Session session = sessionFactory.openSession();
		
		//[5]. 开启事务
		Transaction transaction = session.beginTransaction();
		
		//[6]. 执行保存操作
		Person person = new Person("Cbw", "Yzj", 25, new Date());
		session.save(person);
		
		//[7]. 提交事务
		transaction.commit();
		
		//[8]. 关闭 Session
		session.close();
		
		//[9]. 关闭 SessionFactory
		sessionFactory.close();
		
	}

}
