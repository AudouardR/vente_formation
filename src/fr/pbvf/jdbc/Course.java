package fr.pbvf.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Course {
	
	int idCourse;
	String name;
	String description;
	int days;
	boolean isRemote;
	double price;
	
	// Constructeur 
	public Course(int idCourse, String name, String description, int days, boolean isRemote, double price) {
		setIdCourse(idCourse);
		setName(name);
		setDescription(description);
		setDays(days);
		setIsRemote(isRemote);
		setPrice(price);
	}
	
	public int getIdCourse() {
		return idCourse;
	}
	public void setIdCourse(int idCourse) {
		this.idCourse = idCourse;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	
	public boolean getIsRemote() {
		return isRemote;
	}
	public void setIsRemote(boolean isRemote) {
		this.isRemote = isRemote;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String toString() {
		return getIdCourse() + ") " + 
				getName() + " | " + 
				getDays() + " | " + 
				getDescription() + " | " + 
				(getIsRemote() ? "Distanciel" : "Présentiel") + " | " + 
				getPrice() + "€";
	}
}
