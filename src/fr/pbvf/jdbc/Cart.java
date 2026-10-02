package fr.pbvf.jdbc;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	/**
	 * Panier constitué par un utilisateur pour un client, contenant les formations à commander
	 * 
	 * idCart: Identifiant unique du panie
	 * customer: Client auquel le panier appartient
	 * isOrdered: Vrai si le panier a été commandé, faux sinon
	 * courses: Formations ajoutées au panier
	 * 
	 * nextId: ID du prochain panier à ajouter en mémoire, augmente de 1 à chaque ajout de panier (auto-increment)
	 */
	static int nextId = 1;
	
	int idCart;
	Customer customer;
	boolean isOrdered;
	List<Course> courses = new ArrayList<Course>();
	
	// Constructeurs
	
	/**
	 * Constructeur sans ID (objet à garder en mémoire)
	 * 
	 * @param customer
	 */
	public Cart(Customer customer) {
		setIdCart(nextId);
		setCustomer(customer);
		setIsOrdered(false);
		nextId++;
	}
	
	/**
	 * Constructeur avec ID (objet temporaire, pour le retourner dans une fonction par exemple)
	 * 
	 * @param idCart
	 * @param customer
	 * @param isOrdered
	 * @param courses
	 */
	public Cart(int idCart, Customer customer, boolean isOrdered, List<Course> courses) {
		setIdCart(idCart);
		setCustomer(customer);
		setIsOrdered(isOrdered);
		setCourses(courses);
	}
	
	// Accesseurs
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
	
	/**
	 * Retourne les formations du panier
	 * 
	 * @return
	 */
	public List<Course> getCourses() {
		return courses;
	}
	/**
	 * Ajoute une liste pré-remplie de formations au panier
	 * 
	 * @param courses
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	/**
	 * Ajoute une formation dans le panier
	 * 
	 * @param course
	 */
	public void addCourse(Course course) {
		getCourses().add(course);
	}
	/**
	 * Retire une formation du panier si elle existe (retourne vrai si c'est le cas, faux sinon)
	 * 
	 * @param course
	 * @return
	 */
	public boolean removeCourse(Course course) {
		return getCourses().remove(course);
	}
	
	/**
	 * Définit le panier comme commandé lorsque l'utilisateur le commande
	 */
	public void order() {
		setIsOrdered(true);
	}
	/**
	 * Retourne le prix total des articles du panier
	 * 
	 * @return
	 */
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
		str += "\nPrix total : " + getTotalPrice() + "€";
		return str;
	}

}
