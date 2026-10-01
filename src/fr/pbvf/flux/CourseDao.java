package fr.pbvf.flux;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.pbvf.jdbc.Course;

public class CourseDao implements Dao<Course, Integer> {
	
	@Override
	public void create(Course course, Connection connection) {
		String str = "INSERT INTO Course (name, description, days, is_remote, price) VALUES (?,?,?,?,?);";
    	try (PreparedStatement ps = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS)){
    		ps.setString(1, course.getName());
    		ps.setString(2, course.getDescription());
    		ps.setInt(3, course.getDays());
    		ps.setBoolean(4, course.getIsRemote());
    		ps.setDouble(5, course.getPrice());
    		
    		int rowCreated = ps.executeUpdate();
    		
    		if (rowCreated == 0) {
    			throw new IllegalArgumentException("Erreur lors de la création de la formation : " + course);
    		} 
    		else {
    			System.out.println("Formation insérée");
    			
    			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
        			if (generatedKeys.next()) {
        				int lastInsertedId = generatedKeys.getInt(1);
        				course.setIdCourse(lastInsertedId);
        				System.out.println("ID : " + lastInsertedId);
        			}
        		}
    			catch(Exception e) {
    	    		throw new RuntimeException("Erreur lors de la sauvegarde de l'ID généré dans l'objet : " + e);
    	    	}
    		}
    	} catch(SQLException e) {
    		throw new RuntimeException("Erreur de sauvegarde : " + e);
    	}
	}

	@Override
	public Course findById(Integer idCourse, Connection connection) {
		String sql = "SELECT * FROM Course WHERE id_course = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCourse);

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

	@Override
	public List<Course> findAll(Connection connection) {
		String sql = "SELECT * FROM Course ORDER BY id_course";
        List<Course> courses = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        catch (SQLException e) {
        	throw new RuntimeException("Erreur lors de la recherche des formations", e);
        }

        return courses;
	}

	@Override
	public void update(Integer idCourse, Course course, Connection connection) {
		String sql = "UPDATE Course SET name = ?, description = ?, days = ?, is_remote = ?, price = ? WHERE id_course = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, course.getName());
            ps.setString(2, course.getDescription());
            ps.setInt(3, course.getDays());
            ps.setBoolean(4, course.getIsRemote());
            ps.setDouble(5, course.getPrice());
            ps.setInt(6, idCourse);

            int rowUpdated = ps.executeUpdate();

            if (rowUpdated == 0) {
                throw new IllegalArgumentException("Formation introuvable : " + idCourse);
            }
            else {
            	System.out.println("Formation mise à jour");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la formation", e);
        }
	}

	@Override
	public void delete(Course course, Connection connection) {
		String str = "DELETE FROM Course WHERE id_course = ?;";
    	
    	try (PreparedStatement ps = connection.prepareStatement(str)) {
    		ps.setInt(1, course.getIdCourse());
    		
    		int rowDeleted = ps.executeUpdate();
    		
    		if (rowDeleted == 0) {
                throw new IllegalArgumentException("Formation introuvable : " + course.getIdCourse());
            }
    		else {
    			System.out.println("Formation supprimée");
    		}
    	}
    	catch (SQLException e) {
    		throw new RuntimeException("Erreur lors de la suppression de la formation", e);
        }
	}
	
}
