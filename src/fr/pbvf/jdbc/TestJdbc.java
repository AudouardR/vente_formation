package fr.pbvf.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fr.pbvf.flux.CartDao;
import fr.pbvf.flux.CourseDao;
import fr.pbvf.flux.CustomerDao;
import fr.pbvf.flux.UserDao;

public class TestJdbc {
	
	public static void main(String[] args) throws Exception {
		ArrayList<Course> courses = new ArrayList<Course>();
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String url = "jdbc:mariadb://localhost:3306/vente_formation";
		String login = "root";
		String password = "";
		
		try(Connection connection = DriverManager.getConnection(url,login,password)){
			
			UserDao userDao = new UserDao();
			CustomerDao customerDao = new CustomerDao();
			CourseDao courseDao = new CourseDao();
			CartDao cartDao = new CartDao();
			
			// Créer les utilisateurs
			
			User raphael = new User("AudouardR", "root");
			users.add(raphael);
			
			User martial = new User("BretM", "bret01");
			users.add(martial);
			
			for(User u: users) {
				try {
					userDao.create(u, connection);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
			
			// Créer les clients
			
			Customer patrick = new Customer("Patrick", "Dumont", "patrick.dupont@gmail.com", "23 Rue des Alaoudes, 40230 Tosse", "0672564322", raphael);
			raphael.addCustomer(patrick);
			customers.add(patrick);
			
			Customer kylian = new Customer("Kylian", "Perrin", "kylian.perrin@gmail.com", "231 Av. des Lièvres, 40150 Soorts-Hossegor", "0742786331", raphael);
			raphael.addCustomer(kylian);
			customers.add(kylian);
			
			Customer fabrice = new Customer("Fabrice", "Lafargue", "fabrice.lafargue@msn.com", "3 Chem. de la Croix de Jubilé, 40140 Soustons", "0752010867", martial);
			martial.addCustomer(fabrice);
			customers.add(fabrice);
			
			for(Customer cs: customers) {
				try {
					customerDao.create(cs, connection);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
			
			// Créer les formations
			
			Course java = new Course(1, "Java", "Java SE 8 : Syntaxe & Poo", 20, false, 5.99);
			courses.add(java);
			
			Course javaAvance = new Course(2, "Java avancé", "Spring Core/Mvc/Security", 20, false, 8.53);
			courses.add(javaAvance);
			
			Course spring = new Course(3, "Spring", "Java SE 8 : Syntaxe & Poo", 20, true, 6.99);
			courses.add(spring);
			
			Course phpFrameworks = new Course(4, "Php frameworks", "Symphony", 15, true, 11.23);
			courses.add(phpFrameworks);
			
			Course cSharp = new Course(5, "C#", "DotNet Core", 20, false, 7.50);
			courses.add(cSharp);
			
			for(Course c: courses) {
				try {
					courseDao.create(c, connection);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
			
			// Afficher les utilisateurs stockés en base de données
			try {
				for(User u: userDao.findAll(connection)) {
					System.out.println(u);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher les clients stockés en base de données
			try {
				for(Customer cu: customerDao.findAll(connection)) {
					System.out.println(cu);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher les formations stockées en base de données sous forme de tableau
			
			System.out.printf("%2s | %-20s | %-8s | %-30s | %-12s | %8s%n",
			        "ID", "Formation", "Jours", "Programme", "Modalité", "Prix");

			System.out.println("-----------------------------------------------------------------------------------------------");
			try {
				for(Course c: courseDao.findAll(connection)) {
					System.out.printf("%2s | %-20s | %-8d | %-30s | %-12s | %7.2f€%n",
							c.getIdCourse(),
					        c.getName(), 
					        c.getDays(), 
					        c.getDescription(), 
					        c.getIsRemote() ? "Distanciel" : "Présentiel", 
					        c.getPrice());
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher les formations contenant le mot clé "Java"
			
			System.out.printf("%2s | %-20s | %-8s | %-30s | %-12s | %8s%n",
						      "ID", "Formation", "Jours", "Programme", "Modalité", "Prix");

			System.out.println("-----------------------------------------------------------------------------------------------");
			try {
				for(Course c: courseDao.findByKeyword("Java", connection)) {
					System.out.printf("%2s | %-20s | %-8d | %-30s | %-12s | %7.2f€%n",
							c.getIdCourse(),
			                c.getName(), 
							c.getDays(), 
							c.getDescription(), 
							c.getIsRemote() ? "Distanciel" : "Présentiel", 
							c.getPrice());
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher les formations en distanciel
			
			System.out.printf("%2s | %-20s | %-8s | %-30s | %-12s | %8s%n",
							  "ID", "Formation", "Jours", "Programme", "Modalité", "Prix");

			System.out.println("-----------------------------------------------------------------------------------------------");
			try {
				for(Course c: courseDao.findByModality(true, connection)) {
					System.out.printf("%2s | %-20s | %-8d | %-30s | %-12s | %7.2f€%n",
							c.getIdCourse(),
						    c.getName(), 
							c.getDays(), 
							c.getDescription(), 
							c.getIsRemote() ? "Distanciel" : "Présentiel", 
							c.getPrice());
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Modifier l'utilisateur BretM
			String bretMOldUsername = martial.getUsername();
			martial.setUsername("MartialBret");
			martial.setPassword("bret02");
			try {
				userDao.update(bretMOldUsername, martial, connection);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			// Modifier le client Patrick
			patrick.setHomeAddress("30 Rue de la Forêt, 40230 Saint-Geours-de-Maremne");
			patrick.setEmail("patrick.dupont@icloud.com");
			try {
				customerDao.update(patrick.getIdCustomer(), patrick, connection);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			// Modifier la formation Spring
			spring.setDescription("Java SE 8 : Syntaxe & POO");
			spring.setDays(25);
			spring.setIsRemote(false);
			spring.setPrice(7.99);
			try {
				courseDao.update(spring.getIdCourse(), spring, connection);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher l'utilisateur MartialBret depuis la BDD
			try {
				System.out.println(userDao.findById(martial.getUsername(), connection));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher le client Patrick depuis la BDD
			try {
				System.out.println(customerDao.findById(patrick.getIdCustomer(), connection));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher la formation Spring depuis la BDD
			try {
				System.out.println(courseDao.findById(spring.getIdCourse(), connection));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Supprimer le client Kylian
			raphael.removeCustomer(kylian);
			try {
				customerDao.delete(kylian, connection);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Raphaël ajoute des formations au panier du client Patrick
			try {
				raphael.addCourse(patrick, java);
				raphael.addCourse(patrick, javaAvance);
			} 
			catch(Exception e) {
				e.printStackTrace();
			}
			try {
				customerDao.update(patrick.getIdCustomer(), patrick, connection);
			} 
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Raphaël ajoute la formation c# et retire la formation "Java Avancé"
			try {
				raphael.removeCourse(patrick, javaAvance);
				raphael.addCourse(patrick, cSharp);
			} 
			catch(Exception e) {
				e.printStackTrace();
			}
			try {
				customerDao.update(patrick.getIdCustomer(), patrick, connection);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher le panier actuel de Patrick
			Cart cart = patrick.getCurrentCart();
			try {
				System.out.println(cartDao.findById(cart.getIdCart(), connection));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Raphaël confirme l'achat des articles
			try {
				raphael.order(patrick);
			} 
			catch(Exception e) {
				e.printStackTrace();
			}
			try {
				customerDao.update(patrick.getIdCustomer(), patrick, connection);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Raphaël ajoute un article dans un nouveau panier pour Patrick
			try {
				raphael.addCourse(patrick, spring);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			try {
				customerDao.update(patrick.getIdCustomer(), patrick, connection);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher le nouveau panier de Patrick
			cart = patrick.getCurrentCart();
			try {
				System.out.println(cartDao.findById(cart.getIdCart(), connection));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			// Afficher tous les paniers de Patrick
			for (Cart ca: patrick.getCarts()) {
				try {
					System.out.println(cartDao.findById(ca.getIdCart(), connection));
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
			
			// Afficher tous les paniers
			try {
				for(Cart ca: cartDao.findAll(connection)) {
					System.out.println(ca);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			
			// Supprimer tous les utilisateurs (et les clients associés en cascade)
			for(User u: users) {
				try {
					userDao.delete(u, connection);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
			
			// Supprimer toutes les formations
			for(Course c: courses) {
				try {
					courseDao.delete(c, connection);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
			
			// Réinitialiser l'auto-incrémentation de toutes les tables qui l'ont
			resetAutoIncrement("Course", connection);
			resetAutoIncrement("Customer", connection);
			resetAutoIncrement("Cart", connection);
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void resetAutoIncrement(String tableName, Connection connection) throws SQLException {
		/*
		 * Réinitialise l'auto-incrémentation de la table dont le nom est donné en paramètre
		 */
		String sql = "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1";
		try (Statement statement = connection.createStatement()) {
	        statement.executeUpdate(sql);
	        System.out.println("Auto-incrémentation de " + tableName + " réinitialisée");
	    }
	    catch (SQLException e) {
        	throw new RuntimeException("Erreur lors de la réinitialisation de l'auto-incrémentation : ", e);
        }
	}
	
}
