package models;

public class User {
	
	private static int counter = 0;
	
	private String id;
	private String firstName;
	private String lastName;
	
	public User() {
		User.counter++;
		this.id = String.valueOf(counter);
	}

	public String getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstname) {
		this.firstName = firstname;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastname) {
		this.lastName = lastname;
	}

}
