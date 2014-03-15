package models;

public class Person extends MobileEntity {

	private static long id_counter = 0;
	
	private long id;
	private String name;


	public Person(String name) {
		this.id = id_counter++;
		this.setName(name);
	}
	
	public long getId() {
		return id;
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
