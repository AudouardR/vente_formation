package fr.pbvf.jdbc;

import java.util.ArrayList;
import java.util.List;

public class User {
	String username;
	String password;
	List<Customer> customers = new ArrayList<Customer>();
	
	public User(String username, String password) {
		setUsername(username);
		setPassword(password);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Customer> getCustomers() {
		return customers;
	}
	
	public void addCustomer(Customer customer) {
		getCustomers().add(customer);
	}
	
	public boolean removeCustomer(Customer customer) {
		return getCustomers().remove(customer);
	}
	
	public void addCourse(Customer customer, Course course) {
		if (getCustomers().contains(customer)) {
			customer.addCourse(course);
		}
		else {
			System.out.println("L'utilisateur " + getUsername() + " n'a pas accès au client " + customer.getFirstName() + " " + customer.getLastName());
		}
	}
	public boolean removeCourse(Customer customer, Course course) {
		if (getCustomers().contains(customer)) {
			return customer.removeCourse(course);
		}
		else {
			System.out.println("L'utilisateur " + getUsername() + " n'a pas accès au client " + customer.getFirstName() + " " + customer.getLastName());
		}
		return false;
	}
	
	public void order(Customer customer) {
		if (getCustomers().contains(customer)) {
			customer.order();
		}
		else {
			System.out.println("L'utilisateur " + getUsername() + " n'a pas accès au client " + customer.getFirstName() + " " + customer.getLastName());
		}
	}
	
	public String toString() {
		return getUsername() + " | MDP : " + getPassword();
	}
}
