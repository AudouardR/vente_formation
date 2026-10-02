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
	
	public Cart(Customer customer) {
		/**
		 * Constructeur sans ID (objet à garder en mémoire)
		 */
		setIdCart(nextId);
		setCustomer(customer);
		setIsOrdered(false);
		nextId++;
	}
	
	public Cart(int idCart, Customer customer, boolean isOrdered, List<Course> courses) {
		/**
		 * Constructeur avec ID (objet temporaire, pour le retourner dans une fonction par exemple)
		 */
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
	
	public List<Course> getCourses() {
		/**
		 * Retourne les formations du panier
		 */
		return courses;
	}
	public void setCourses(List<Course> courses) {
		/**
		 * Ajoute une liste pré-remplie de formations au panier
		 */
		this.courses = courses;
	}
	
	public void addCourse(Course course) {
		/**
		 * Ajoute une formation dans le panier
		 */
		getCourses().add(course);
	}
	public boolean removeCourse(Course course) {
		/**
		 * Retire une formation du panier si elle existe (retourne vrai si c'est le cas, faux sinon)
		 */
		return getCourses().remove(course);
	}
	
	public void order() {
		/**
		 * Définit le panier comme commandé lorsque l'utilisateur le commande
		 */
		setIsOrdered(true);
	}
	public double getTotalPrice() {
		/**
		 * Retourne le prix total des articles du panier
		 */
		double totalPrice = 0;
		for (Course course: getCourses()) {
			totalPrice += course.getPrice();
		}
		return totalPrice;
	}
	
	public String toString() {
		/**
		 * Représentation en String du panier
		 */
		String str = getIdCart() + ") Panier de " + getCustomer().getFirstName() + " " + getCustomer().getLastName() + ", " + (getIsOrdered() ? "Commandé" : "En attente") + " :";
		for (Course c: getCourses()) {
			str += "\n- " + c;
		}
		str += "\nPrix total : " + getTotalPrice() + "€";
		return str;
	}

}
