package fr.pbvf.flux;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.pbvf.jdbc.Cart;
import fr.pbvf.jdbc.Customer;

public class CustomerDao implements Dao<Customer, Integer> {

	@Override
	public void create(Customer customer, Connection connection) {
		String str = "INSERT INTO Customer (first_name, last_name, email, home_address, phone_number, username) VALUES (?,?,?,?,?,?);";
    	try (PreparedStatement ps = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS)){
    		ps.setString(1, customer.getFirstName());
    		ps.setString(2, customer.getLastName());
    		ps.setString(3, customer.getEmail());
    		ps.setString(4, customer.getHomeAddress());
    		ps.setString(5, customer.getPhoneNumber());
    		ps.setString(6, customer.getUser().getUsername());
    		
    		int rowCreated = ps.executeUpdate();
    		
    		if (rowCreated == 0) {
    			throw new IllegalArgumentException("Erreur lors de la création du client : " + customer);
    		} 
    		else {
    			System.out.println("Client insérée");
    			
    			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
        			if (generatedKeys.next()) {
        				int lastInsertedId = generatedKeys.getInt(1);
        				customer.setIdCustomer(lastInsertedId);
        				System.out.println("ID : " + lastInsertedId);
        			}
        		}
    			catch(Exception e) {
    	    		throw new RuntimeException("Erreur lors de la sauvegarde de l'ID généré dans l'objet : " + e);
    	    	}
    			
    			CartDao cartDao = new CartDao();
    	    	try {
    	    		Cart currentCart = customer.getCurrentCart();
    	    		cartDao.create(currentCart, connection);
    	    	}
    	    	catch(Exception e) {
    	    		throw new RuntimeException("Echec de la création du panier : " + e);
    	    	}
    		}
    	} catch(SQLException e) {
    		throw new RuntimeException("Erreur de sauvegarde : " + e);
    	}
    	
	}

	@Override
	public Customer findById(Integer idCustomer, Connection connection) {
		UserDao userDao = new UserDao();
		
		String sql = "SELECT * FROM Customer WHERE id_customer = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCustomer);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                    	rs.getInt("id_customer"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("home_address"),
                        rs.getString("phone_number"),
                        userDao.findById(rs.getString("username"), connection)
                    );
                }
            }
        } 
		catch (SQLException e) {
            e.printStackTrace();
        }
		
		return null;
	}

	@Override
	public List<Customer> findAll(Connection connection) {
		UserDao userDao = new UserDao();
		
		String sql = "SELECT * FROM Customer ORDER BY id_customer";
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            	customers.add(new Customer(
            		rs.getInt("id_customer"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("home_address"),
                    rs.getString("phone_number"),
                    userDao.findById(rs.getString("username"), connection)
                ));
            }
        } 
        catch (SQLException e) {
        	throw new RuntimeException("Erreur lors de la recherche des clients", e);
        }

        return customers;
	}

	@Override
	public void update(Integer idCustomer, Customer customer, Connection connection) {
		String sql = "UPDATE Customer SET first_name = ?, last_name = ?, email = ?, home_address = ?, phone_number = ?, username = ? WHERE id_customer = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, customer.getFirstName());
    		ps.setString(2, customer.getLastName());
    		ps.setString(3, customer.getEmail());
    		ps.setString(4, customer.getHomeAddress());
    		ps.setString(5, customer.getPhoneNumber());
    		ps.setString(6, customer.getUser().getUsername());
    		ps.setInt(7, idCustomer);

            int rowUpdated = ps.executeUpdate();

            if (rowUpdated == 0) {
                throw new IllegalArgumentException("Client introuvable : " + idCustomer);
            }
            else {
            	System.out.println("Client mis à jour");
            	
            	CartDao cartDao = new CartDao();
            	try {
            		for (Cart ca: customer.getCarts()) {
            			if (cartDao.findById(ca.getIdCart(), connection) != null) {
            				cartDao.update(ca.getIdCart(), ca, connection);
            			}
            		}
            	}
            	catch(Exception e) {
    	    		throw new RuntimeException("Echec de mise à jour des paniers : " + e);
    	    	}
    	    	try {
    	    		Cart currentCart = customer.getCurrentCart();
    	    		if (cartDao.findById(currentCart.getIdCart(), connection) == null) {
    	    			cartDao.create(currentCart, connection);
    	    		}
    	    	}
    	    	catch(Exception e) {
    	    		throw new RuntimeException("Echec de la modification du panier : " + e);
    	    	}
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du client", e);
        }
	}

	@Override
	public void delete(Customer customer, Connection connection) {
		String str = "DELETE FROM Customer WHERE id_customer = ?;";
    	
    	try (PreparedStatement ps = connection.prepareStatement(str)) {
    		ps.setInt(1, customer.getIdCustomer());
    		
    		int rowDeleted = ps.executeUpdate();
    		
    		if (rowDeleted == 0) {
                throw new IllegalArgumentException("Client introuvable : " + customer.getIdCustomer());
            }
    		else {
    			System.out.println("Client supprimé");
    		}
    	}
    	catch (SQLException e) {
    		throw new RuntimeException("Erreur lors de la suppression du client", e);
        }
	}

}
