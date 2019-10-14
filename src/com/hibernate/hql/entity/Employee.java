package com.hibernate.hql.entity;

//员工
public class Employee {

	private Integer id;
	private String employeeName;
	private Integer age;
	private String email;
	private float salary;
	private Department department;
	
	public Employee() {

	}

	public Employee(String employeeName, float salary, Department department) {
		super();
		this.employeeName = employeeName;
		this.salary = salary;
		this.department = department;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", employeeName=" + employeeName + ", age=" + age + ", email=" + email
				+ ", salary=" + salary + ", department=" + department + "]";
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}

}
