package fr.pbvf.jdbc;

import java.util.ArrayList;

public class Customer {
	static int nextId = 1;
	
	int idCustomer;
	String firstName;
	String lastName;
	String email;
	String homeAddress;
	String phoneNumber;
	User user;
	ArrayList<Cart> carts = new ArrayList<Cart>();
	
	// Constructeur sans ID (objet à garder en mémoire)
	public Customer(String firstName, String lastName, String email, String homeAddress, String phoneNumber, User user) {
		setIdCustomer(nextId);
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setHomeAddress(homeAddress);
		setPhoneNumber(phoneNumber);
		setUser(user);
		// Crée un nouveau panier
		getCurrentCart();
		nextId++;
	}
	
	public Customer(String firstName, String lastName, User user) {
		setIdCustomer(nextId);
		setFirstName(firstName);
		setLastName(lastName);
		setUser(user);
		// Crée un nouveau panier
		getCurrentCart();
		nextId++;
	}
	
	// Constructeur avec ID (objet temporaire, pour le retourner dans une fonction par exemple)
	public Customer(int idCustomer, String firstName, String lastName, String email, String homeAddress, String phoneNumber, User user) {
		setIdCustomer(idCustomer);
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setHomeAddress(homeAddress);
		setPhoneNumber(phoneNumber);
		setUser(user);
	}
	
	public Customer(int idCustomer, String firstName, String lastName, User user) {
		setIdCustomer(idCustomer);
		setFirstName(firstName);
		setLastName(lastName);
		setUser(user);
	}
	
	public int getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public ArrayList<Cart> getCarts() {
		return carts;
	}
	
	public Cart getCurrentCart() {
		/*
		 * Retourne le panier actuel du client (Le dernier de la liste s'il n'est pas commandé), crée et retourne un nouveau panier si le client n'en a pas
		 */
		Cart currentCart;
		if (carts.isEmpty()) {
			currentCart = new Cart(this);
			carts.add(currentCart);
		}
		else {
			if (carts.get(carts.size() - 1).getIsOrdered()) {
				currentCart = new Cart(this);
				carts.add(currentCart);
			}
			else {
				currentCart = carts.get(carts.size() - 1);
			}
		}
		return currentCart;
	}
	
	public void addCourse(Course course) {
		getCurrentCart().addCourse(course);
	}
	public boolean removeCourse(Course course) {
		return getCurrentCart().removeCourse(course);
	}
	
	public void order() {
		// Commande les articles du panier actuel
		getCurrentCart().order();
		// Crée un nouveau panier
		getCurrentCart();
	}
	
	public String toString() {
		return getIdCustomer() + ") " + getFirstName() + " | " + getLastName() + " | " + getEmail() + " | " + getHomeAddress() + " | " + getPhoneNumber() + " | Utilisateur : " + getUser().getUsername();
	}
	
}
