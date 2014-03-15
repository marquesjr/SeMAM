package models;

public class Patient extends Person {

	private Heartbeat heartbeat;
	
	public Patient(String name) {
		super(name);
		this.heartbeat = new Heartbeat();
        this.setType("patient");
	}
	
	public Heartbeat getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(Heartbeat heartbeat) {
		this.heartbeat = heartbeat;
	}

}
