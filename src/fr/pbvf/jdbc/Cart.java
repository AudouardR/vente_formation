package fr.pbvf.jdbc;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	static int nextId = 1;
	
	int idCart;
	Customer customer;
	boolean isOrdered;
	List<Course> courses = new ArrayList<Course>();
	
	// Constructeur sans ID (objet à garder en mémoire)
	public Cart(Customer customer) {
		setIdCart(nextId);
		setCustomer(customer);
		setIsOrdered(false);
		nextId++;
	}
	
	// Constructeur avec ID (objet temporaire, pour le retourner dans une fonction par exemple)
	public Cart(int idCart, Customer customer, boolean isOrdered, List<Course> courses) {
		setIdCart(idCart);
		setCustomer(customer);
		setIsOrdered(isOrdered);
		setCourses(courses);
	}
	
	public int getIdCart() {
		return idCart;
	}
	public void setIdCart(int idCart) {
		this.idCart = idCart;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public boolean getIsOrdered() {
		return isOrdered;
	}
	public void setIsOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}
	
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public void addCourse(Course course) {
		getCourses().add(course);
	}
	public boolean removeCourse(Course course) {
		return getCourses().remove(course);
	}
	
	public void order() {
		setIsOrdered(true);
	}
	public double getTotalPrice() {
		double totalPrice = 0;
		for (Course course: getCourses()) {
			totalPrice += course.getPrice();
		}
		return totalPrice;
	}
	
	public String toString() {
		String str = getIdCart() + ") Panier de " + getCustomer().getFirstName() + " " + getCustomer().getLastName() + ", " + (getIsOrdered() ? "Commandé" : "En attente") + " :";
		for (Course c: getCourses()) {
			str += "\n- " + c;
		}
		return str;
	}

}
