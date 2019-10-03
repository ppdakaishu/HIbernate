package com.hibernate.session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hibernate.helloworld.entity.A;
import com.hibernate.helloworld.entity.B;
import com.hibernate.helloworld.entity.LargeText;
import com.hibernate.helloworld.entity.Person;
import com.hibernate.helloworld.entity.Time;

//对象状态转换测试
class StateTransitionTest {

	private SessionFactory sessionFactory;
	//注 : 实际应用中, 下列属性不能作为成员变量使用. 可能会导致并发问题
	private Session session;
	private Transaction transaction;
	
	@BeforeEach
	public void Init() {
		//加载配置文件
		Configuration cfg = new Configuration().configure();
		//声明 ServiceRegistry
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
		//创建 SessionFactory
		sessionFactory = cfg.buildSessionFactory(serviceRegistry);
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
	
	//测试组成关系映射
	@Test
	public void testComponent() {
		B b = new B();
		b.setbOneField("bOneField");
		b.setbTwoField("bTwoField");
		
		A a = new A();
		a.setaOneField("aOneField");
		a.setB(b);
		
		session.save(a);
	}
	
	//测试大文本类型的映射
	@Test
	public void testBlob() throws IOException, SQLException {
		//写入
		LargeText lart = new LargeText();
		
		Clob clob = Hibernate.getLobCreator(session).createClob("content.......");
		lart.setContent(clob);
		
		InputStream stream = new FileInputStream("Blank.jpg");
		Blob image = Hibernate.getLobCreator(session).createBlob(stream, stream.available());
		lart.setImage(image);
		
		session.save(lart);
		
		//读取
		LargeText lart1 = (LargeText) session.get(LargeText.class, 66);
		Blob image1 = lart1.getImage();
		InputStream inputStream = image1.getBinaryStream(); 
		System.out.println(inputStream.available());
	}
	
	//测试时间和日期类型的映射
	@Test
	public void testTime() {
		Date date = new Date();
//		Time time = new Time(date, date, date);
//		session.save(time);
		
		Time time = (Time) session.get(Time.class, 52);
		//Time [id=52, date=2019-09-26, time=23:29:57, timestamp=2019-09-26 23:29:57.0]
		System.out.println(time);
		
	}
	
	//测试派生属性
	@Test
	public void testFormula() {
		Person person = (Person) session.get(Person.class, 41);
		System.out.println(person.getDesc());
	}
	
	//调用存储过程
	@Test
	public void testDoWork() {
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				//oracle.jdbc.driver.T4CConnection@550ee7e5, [ 未使用C3P0 ]
				//com.mchange.v2.c3p0.impl.NewProxyConnection@53812a9b, [ 使用C3P0 ]
				System.out.println(connection);
				//调用存储过程...
			}
		});
	}
	
	//evict : 
	//从 session 中把指定的持久化对象移除
	@Test
	public void testEvict() {
		Person person = (Person) session.get(Person.class, 4);
		person.setCompany("Yzj");
		//若不执行evict() : 
		//则第一句会发送查询语句执行查询操作, 在提交事务时会发送更新语句执行更新操作
		//若执行evict() : 
		//则提交事务时, 也不会发送更新语句执行更新操纵. 因为执行后, 持久化对象已被移除 session
		session.evict(person);
	}
	
	//delete() : 
	//执行删除操作.  只要 OID 和数据表中的一条记录对应, 就会执行 delete 操作
	//若 OID 在数据表中没有对应记录, 则会抛出异常.  但是若映射文件中为 <id> 设置了 unsaved-value 属性, 
	//且对象的 OID 与 unsaved-value 属性值匹配. 则不会抛出异常, 也不会发出 delete 语句
	//
	//可以通过设置 hibernate 配置文件 hibernate.use_identifier_rollback 属性为 ture
	//指删除对象后, 把其 OID 置为 null
	@Test
	public void testDelete() {
		//持久化对象
		Person person = (Person) session.get(Person.class, 24);
		//游离对象
//		Person person = new Person();
//		person.setId(8);
		
		//在 flush 缓存时发出 delete 语句执行操作
		session.delete(person);
		
		System.out.println(person);
	}
	
	//saveOrUpdate() : 
	//	1. 若 OID 不为 null, 但数据表中没有与之对应的记录.  则会抛出 StaleObjectStateException 异常
	//	2. 了解 ：映射文件中为 <id> 设置了 unsaved-value 属性, 并且 Java 对象的 OID 取值与这个 unsaved-value 属性值匹配,
	//			也被认为是一个游离对象,  不会抛出 StaleObjectStateException 异常. 而是会执行 insert 操作
	@Test
	public void testSaveOrUpdate() {
		Person person = new Person("Wxx", "home", 48, new Date());
		person.setId(99);
		session.saveOrUpdate(person);
	}
	
	//update() : 
	//	1. 若更新一个持久化对象, 不需要显示的( 即写session.update(..) )调用 update() 方法. 
	//		因为在调用 Transaction 的 commit() 方法时, 会先执行 session 的 flush 方法为了让缓存中与数据库记录保持一致发送 update() 语句. 
	//	2. 更新一个游离对象, 需要显示的调用 session 的 update 方法, 可以把一个游离对象变为持久化对象. 
	//
	//	注意 : 
	//		1. 无论要更新的游离对象和数据表的记录是否一致, 都会发送更新语句. 
	//		2. 若数据表中无对应记录, 但还调用了 update 方法, 会抛出异常. 
	//		3. 当 update() 一个游离对象, 并且当 Session 缓存中已经存在 OID 相同的持久化对象时, 会抛出异常
	//			因为在一个 Session 缓存中, 不能有两个 OID 相同的对象.  a
	//	
	//	问题 : 
	//		Q : 如何能让 update() 方法不在盲目的发出update语句?
	//		A : 在 .hbm.xml 文件的class节点设置 select-before-update = true, 默认为 false.
	//			通常不需要设置该属性, 除非与触发器共同工作. 因为更新时要多发送一条查询语句, 导致效率变低
	@Test
	public void testUpdate() {
		Person person = (Person) session.get(Person.class, 4);
		
		//关闭 Session
		transaction.commit();
		session.close();
		
		//打开一个新 Session
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		
		//person.setId(100);
		
		//在新 Session 中, person 为游离对象. 
		//调用 update() 方法时不管对象是否与数据库记录是否一致都会发送 update 语句
		//person.setCompany("YzjL");
		//若加上此句, 会抛出 StaleObjectStateException, 注意中第三点. 
		Person person2 = (Person) session.get(Person.class, 4);
		session.update(person);
	}
	
	//get() VS load() :
	//	1. get() -> 立即检索, load() -> 延迟检索
	//		执行 get() 方法 : 会立即加载对象. 
	//		执行 load() 方法 : 延迟加载. 若不使用该对象, 则不会立即执行查询操作, 而返回一个代理对象.
	//	2. load() 可能会抛出 LazyInitializationException 异常
	//		在需要初始化代理对象之前关闭 Session
	//	3. 若数据表中无对应记录, 且 Session 也没有关闭
	//		执行 get() 方法 : 返回null.
	//		执行 load() 方法 : 若不使用该对象的任何属性, 无异常. 若需要初始化, 则会抛出异常. 
	@Test
	public void testGetAndLoad() {
		Person person = (Person) session.get(Person.class, 41);
		System.out.println(person);
		
		Person person2 = (Person) session.load(Person.class, 4);
		session.close();
		System.out.println(person2);
	}
	
	//save() : 
	//	1. 使一个临时对象变为持久化对象. 
	//	2. 为对象分配 ID
	//	3. 在 flush 缓存时会发送一条 INSERT 语句
	//	4. 在 save 方法前的 ID 是无效的
	//	5. 持久化对象的 ID 是不能被修改的 ( Hibernate 使用 ID 与数据库记录对应 )
	//Persist() :
	//	1. 也会执行 INSERT 语句.
	//区别 : 
	//	1. 在调用 save() 方法前, 若对象已有 ID, 则依然会执行 INSERT. 
	//	2. 在调用 persist() 方法前, 若对象已有 ID, 则不会执行 INSERT, 而是会抛出异常. 
	@Test
	public void testSaveAndPersist() {
		Person person = new Person("Cd", "xxc", 48, new Date());
		//save() : 此处 ID 并不会存入数据库, 而是由主键生成策略决定取值
		//Persist() : 在执行 session.persist(person) 时抛出异常
		person.setId(100); 
		
		System.out.println(person);
		
		session.save(person);
//		session.persist(person);
		
		System.out.println(person);
	}

}
