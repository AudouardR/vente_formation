package fr.pbvf.flux;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.pbvf.jdbc.Cart;
import fr.pbvf.jdbc.Course;

public class CartDao implements Dao<Cart,Integer> {

	@Override
	public void create(Cart cart, Connection connection) {
		String str = "INSERT INTO Cart (id_customer, is_ordered) VALUES (?,?);";
    	try (PreparedStatement ps = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS)){
    		ps.setInt(1, cart.getCustomer().getIdCustomer());
    		ps.setBoolean(2, cart.getIsOrdered());
    		
    		int rowCreated = ps.executeUpdate();
    		
    		if (rowCreated == 0) {
    			throw new IllegalArgumentException("Erreur lors de la création du panier : " + cart);
    		} 
    		else {
    			System.out.println("Panier inséré");
    			
    			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
        			if (generatedKeys.next()) {
        				int lastInsertedId = generatedKeys.getInt(1);
        				cart.setIdCart(lastInsertedId);
        				System.out.println("ID : " + lastInsertedId);
        			}
        		}
    			catch(Exception e) {
    	    		throw new RuntimeException("Erreur lors de la sauvegarde de l'ID généré dans l'objet : " + e);
    	    	}
    			
    			for (Course c: cart.getCourses()) {
        			createOrder(c, cart, connection);
        		}
    		}
    	} catch(SQLException e) {
    		throw new RuntimeException("Erreur de sauvegarde : " + e);
    	}
	}

	@Override
	public Cart findById(Integer idCart, Connection connection) {
		CustomerDao customerDao = new CustomerDao();
		
		String sql = "SELECT * FROM Cart WHERE id_cart = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCart);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	Cart cart = new Cart(
                        rs.getInt("id_cart"),
                        customerDao.findById(rs.getInt("id_customer"), connection),
                        rs.getBoolean("is_ordered"),
                        findAllOrderedCourses(rs.getInt("id_cart"), connection)
                    );
                	
                    return cart;
                }
            }
        } 
		catch (SQLException e) {
            e.printStackTrace();
        }
		
		return null;
	}

	@Override
	public List<Cart> findAll(Connection connection) {
		CustomerDao customerDao = new CustomerDao();
		
		String sql = "SELECT * FROM Cart ORDER BY id_cart";
        List<Cart> carts = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            	carts.add(new Cart(
                    rs.getInt("id_cart"),
                    customerDao.findById(rs.getInt("id_customer"), connection),
                    rs.getBoolean("is_ordered"),
                    findAllOrderedCourses(rs.getInt("id_cart"), connection)
                ));
            }
        } 
        catch (SQLException e) {
        	throw new RuntimeException("Erreur lors de la recherche des paniers : ", e);
        }

        return carts;
	}

	@Override
	public void update(Integer idCart, Cart cart, Connection connection) {
		String sql = "UPDATE Cart SET id_customer = ?, is_ordered = ? WHERE id_cart = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, cart.getCustomer().getIdCustomer());
    		ps.setBoolean(2, cart.getIsOrdered());
    		ps.setInt(3, cart.getIdCart());

            int rowUpdated = ps.executeUpdate();

            if (rowUpdated == 0) {
                throw new IllegalArgumentException("Panier introuvable : " + idCart);
            }
            else {
            	System.out.println("Panier mis à jour");
            	
            	List<Course> currentCourses = findAllOrderedCourses(cart.getIdCart(), connection);
            	
            	// Listes d'ID des formations pour les comparer et vérifier si elles figurent dans l'objet Cart ou dans la base de données
            	List<Integer> newCoursesId = new ArrayList<>();
            	for (Course c: cart.getCourses()) {
            		newCoursesId.add(c.getIdCourse());
            	}
            	List<Integer> currentCoursesId = new ArrayList<>();
            	for (Course c: currentCourses) {
    				currentCoursesId.add(c.getIdCourse());
    			}
    			
            	// Mettre à jour le contenu du panier dans la table Order_
    			for (Course c: cart.getCourses()) {
    				if (!currentCoursesId.contains(c.getIdCourse())) {
        				createOrder(c, cart, connection);
    				}
        		}
    			for (Course c: currentCourses) {
    				if (!newCoursesId.contains(c.getIdCourse())) {
        				deleteOrder(c, cart, connection);
    				}
    			}
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du panier : ", e);
        }
	}

	@Override
	public void delete(Cart cart, Connection connection) {
		String str = "DELETE FROM Cart WHERE id_cart = ?;";
    	
    	try (PreparedStatement ps = connection.prepareStatement(str)) {
    		ps.setInt(1, cart.getIdCart());
    		
    		int rowDeleted = ps.executeUpdate();
    		
    		if (rowDeleted == 0) {
                throw new IllegalArgumentException("Panier introuvable : " + cart.getIdCart());
            }
    		else {
    			System.out.println("Panier supprimé");
    		}
    	}
    	catch (SQLException e) {
    		throw new RuntimeException("Erreur lors de la suppression du panier : ", e);
        }
	}
	
	public void createOrder(Course course, Cart cart, Connection connection) {
		String str = "INSERT INTO Order_ (id_course, id_cart) VALUES (?, ?)";
		
		try (PreparedStatement ps = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, course.getIdCourse());
			ps.setInt(2, cart.getIdCart());
			
			int rowCreated = ps.executeUpdate();
    		
    		if (rowCreated == 0) {
    			throw new IllegalArgumentException("Erreur lors de l'ajout de la formation " + course.getName() + " au panier : " + cart);
    		} 
    		else {
    			System.out.println("Formation ajoutée au panier");
    		}
			
		} catch(SQLException e) {
    		throw new RuntimeException("Erreur de sauvegarde : " + e);
    	}
	}
	
	public Course findOrderedCourseById(int idCourse, int idCart, Connection connection) {
		String str = "SELECT c.* FROM Order_ o JOIN Course c ON o.id_course = c.id_course WHERE id_course = ? AND id_cart = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(str)) {
            ps.setInt(1, idCourse);
            ps.setInt(2, idCart);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	return new Course(
                        rs.getInt("id_course"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("days"),
                        rs.getBoolean("is_remote"),
                        rs.getDouble("price")
                    );
                }
            }
        } 
		catch (SQLException e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	public List<Course> findAllOrderedCourses(int idCart, Connection connection) {
		String str = "SELECT c.* FROM Order_ o JOIN Course c ON o.id_course = c.id_course WHERE id_cart = ? ORDER BY id_course";
		List<Course> courses = new ArrayList<>();
		
		try (PreparedStatement ps = connection.prepareStatement(str)) {
			ps.setInt(1, idCart);
			
			try (ResultSet rs = ps.executeQuery()) {
		        while (rs.next()) {
		            courses.add(new Course(
		            	rs.getInt("id_course"),
	                    rs.getString("name"),
	                    rs.getString("description"),
	                    rs.getInt("days"),
	                    rs.getBoolean("is_remote"),
	                    rs.getDouble("price")
		            ));
		        }
			}
	    } 
	    catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la recherche des formations dans le panier : ", e);
	    }

	    return courses;
	}
	
	public void deleteOrder(Course course, Cart cart, Connection connection) {
		String str = "DELETE FROM Order_ WHERE id_course = ? AND id_cart = ?;";
    	
    	try (PreparedStatement ps = connection.prepareStatement(str)) {
    		ps.setInt(1, course.getIdCourse());
    		ps.setInt(2, cart.getIdCart());
    		
    		int rowDeleted = ps.executeUpdate();
    		
    		if (rowDeleted == 0) {
                throw new IllegalArgumentException("Formation " + course.getIdCourse() + " introuvable dans le panier " + cart.getIdCart());
            }
    		else {
    			System.out.println("Formation retirée du panier");
    		}
    	}
    	catch (SQLException e) {
    		throw new RuntimeException("Erreur lors de la suppression du panier : ", e);
        }
	}

}
