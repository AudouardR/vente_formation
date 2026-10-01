package fr.pbvf.flux;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.pbvf.jdbc.User;

public class UserDao implements Dao<User, String> {

	@Override
	public void create(User user, Connection connection) {
		String str = "INSERT INTO User_ (username, password) VALUES (?,?);";
    	try (PreparedStatement ps = connection.prepareStatement(str)){
    		ps.setString(1, user.getUsername());
    		ps.setString(2, user.getPassword());
    		
    		int rowCreated = ps.executeUpdate();
    		
    		if (rowCreated == 0) {
    			throw new IllegalArgumentException("Erreur lors de la création de l'utilisateur : " + user);
    		} 
    		else {
    			System.out.println("Utilisateur inséré");
    		}
    	} catch(SQLException e) {
    		throw new RuntimeException("Erreur de sauvegarde : " + e);
    	}
	}

	@Override
	public User findById(String username, Connection connection) {
		String sql = "SELECT * FROM User_ WHERE username = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                    	rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return null;
	}

	@Override
	public List<User> findAll(Connection connection) {
		String sql = "SELECT * FROM User_ ORDER BY username";
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            	users.add(new User(
                    rs.getString("username"),
                    rs.getString("password")
                ));
            }
        } catch (SQLException e) {
        	throw new RuntimeException("Erreur lors de la recherche des utilisateurs", e);
        }

        return users;
	}

	@Override
	public void update(String username, User user, Connection connection) {
		String sql = "UPDATE User_ SET username = ?, password = ? WHERE username = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, username);

            int rowUpdated = ps.executeUpdate();

            if (rowUpdated == 0) {
                throw new IllegalArgumentException("Utilisateur introuvable : " + username);
            }
            else {
            	System.out.println("Utilisateur mis à jour");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur : ", e);
        }
	}

	@Override
	public void delete(User user, Connection connection) {
		String str = "DELETE FROM User_ WHERE username = ?;";
    	
    	try (PreparedStatement ps = connection.prepareStatement(str)) {
    		ps.setString(1, user.getUsername());
    		
    		int rowDeleted = ps.executeUpdate();
    		
    		if (rowDeleted == 0) {
                throw new IllegalArgumentException("Utilisateur introuvable : " + user.getUsername());
            }
    		else {
    			System.out.println("Utilisateur supprimé");
    		}
    	}
    	catch (SQLException e) {
    		throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
        }
	}
	
}
