package me.tony.school.products;

public class Product {
	
	private String name;
	private int id = 0;
	private double value;
	
	public Product(String name, double value) {
		generateId();
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public double getValue() {
		return value;
	}
	
	public int getId() {
		return id;
	}
	
	private void generateId() {
		this.id += id + 1;
	}
}
