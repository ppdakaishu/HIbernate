package com.hibernate.hql;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.query.QueryProducer;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hibernate.hql.entity.Department;
import com.hibernate.hql.entity.Employee;

//HQL
class HqlTest {

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
	public void testSQL() {
		String sql = "SELECT * FROM HIBERNATE_HQL_DEPARTMENT";
		//4.x
//		Query query = session.createSQLQuery(sql);
		//5.x
		Query query = session.createNativeQuery(sql);
		List<Department> list = query.list();
		System.out.println(list.size());
		//只需执行, 如新增或更新的SQL :
//		query.executeUpdate();
	}
	
	//QBC
	@Test
	public void testQBC4() {
		Criteria criteria = session.createCriteria(Employee.class);
		//排序
		criteria.addOrder(Order.asc("age"));
		criteria.addOrder(Order.desc("salary"));
		//分页
		int pageNumber = 3;
		int pageSize = 5;
		criteria.setFirstResult((pageNumber - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		
		criteria.list();
	}
	
	@Test
	public void testQBC3() {
		Criteria criteria = session.createCriteria(Employee.class);
		// 统计查询
		criteria.setProjection(Projections.max("age"));
		System.out.println(criteria.uniqueResult());
	}
	
	@Test
	public void testQBC2() {
		//创建对象 [ Criteria ]
		Criteria criteria = session.createCriteria(Employee.class);
		
		//AND
		Conjunction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.like("employeeName", "emp", MatchMode.ANYWHERE));
		conjunction.add(Restrictions.eq("age", 10));
		System.out.println(conjunction);
		
		//OR
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.isNotNull("email"));
		disjunction.add(Restrictions.ge("salary", 100F));
		System.out.println(disjunction);
		
		criteria.add(conjunction);
		criteria.add(disjunction);
		
		List<Employee> employees = criteria.list();
		System.out.println(employees.size());
	}
	
	@Test
	public void testQBC1() {
		//创建对象 [ Criteria ]
		Criteria criteria = session.createCriteria(Employee.class);
		//添加条件
		criteria.add(Restrictions.between("age", 0, 10));
		//执行查询
		List<Employee> employees = criteria.list();
		System.out.println(employees.size());
	}
	
	//报表查询
	@Test
	public void testHql() {
		String hql = "SELECT MIN(e.age), MAX(e.age) FROM Employee e GROUP BY e.department HAVING MIN(e.age) > :minAge";
		Query query = session.createQuery(hql);
		List<Object[]> result = query.setParameter("minAge", 90).list();
		for(Object [] objs : result) {
			System.out.println(Arrays.asList(objs));
		}
	}
	
	//投影查询
	@Test
	public void testHqlFieldQuery2() {
		//new Employee(e.employeeName, e.salary, e.department) 
		//需要实体类中存在有参构造器
		String hql = "SELECT new Employee(e.employeeName, e.salary, e.department) FROM Employee e WHERE e.department = :department";
		Query query = session.createQuery(hql);
		
		Department dept = new Department();
		dept.setId(621);
		List<Employee> result = query.setParameter("department", dept).list();
		for(Employee emp : result) {
			System.out.println(emp);
		}
	}
	
	//投影查询
	@Test
	public void testHqlFieldQuery1() {
		String hql = "SELECT e.employeeName, e.salary, e.department FROM Employee e WHERE e.department = :department";
		Query query = session.createQuery(hql);
		
		Department dept = new Department();
		dept.setId(621);
		List<Object[]> result = query.setParameter("department", dept).list();
		for(Object [] objs : result) {
			System.out.println(Arrays.asList(objs));
		}
	}
	
	//命名查询
	@Test
	public void testHqlNamedQuery() {
		Query query = session.getNamedQuery("namedQueryTest");
		List<Employee> employees = query.setParameter("MINAGE", 0).setParameter("MAXAGE", 10).list();
		System.out.println(employees.size());
	}
	
	//分页查询
	@Test
	public void testHqlPageQuery() {
		String hql = "FROM Employee";
		Query query = session.createQuery(hql);
		int pageNumber = 3;
		int pageSize = 5;
		
		List<Employee> employees = query.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(pageSize).list();
		System.out.println(employees);
	}
	
	@Test
	public void testHqlNamedParameter() {
		String hql = "FROM Employee WHERE employeeName LIKE :name AND AGE > :age";
		
		//创建对象 Query
		Query query = session.createQuery(hql);
		//绑定参数, 按参数名字绑定, 支持方法链的编程风格
		query.setParameter("name", "%%").setParameter("age", 12);
		//执行查询
		List<Employee> employees = query.list();
		System.out.println(employees.size());
	}
	
	@Test
	public void testHqlPositionParameter() {
		String hql = "FROM Employee WHERE employeeName LIKE ? AND AGE >= ? AND department = ?";
		
		//创建对象 Query
		Query query = session.createQuery(hql);
		//绑定参数, 按参数位置绑定, 支持方法链的编程风格
		Department dept = new Department();
		dept.setId(621);
		//4.x
//		query.setString(0, "%employeeName%").setInteger(1, 0).setEntity(2, dept);
		//5.x 
		query.setParameter(0, "%employeeName%").setParameter(1, 0).setParameter(2, dept);
		//执行查询
		List<Employee> employees = query.list();
		System.out.println(employees.size());
	}
	
	@Test
	public void save() {
		for(int i = 0; i < 100; i++) {
			Department department = new Department();
			department.setDepartmentName("departmentName-" + i);
			
			Employee employee1 = new Employee();
			employee1.setEmployeeName("employeeName-" + i);
			employee1.setAge(i);
			employee1.setEmail("70129526@qq.com");
			employee1.setSalary(11);
			employee1.setDepartment(department);
			
			Employee employee2 = new Employee();
			employee2.setEmployeeName("employeeName-" + i);
			employee2.setAge(i);
			employee2.setEmail("1027616230@qq.com");
			employee2.setSalary(22);
			employee2.setDepartment(department);
			
			session.save(department);
			session.save(employee1);
			session.save(employee2);
		}
		
	}
	
}
