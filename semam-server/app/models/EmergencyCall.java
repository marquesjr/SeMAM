package models;

public class EmergencyCall {

	private static long id_counter = 0;
	
	public long id;
	private long timestamp;
	private EmergencyStatus status;
	private Ambulance ambulance;
    private Patient patient;

    public EmergencyCall(Patient p) {
        this.status = EmergencyStatus.IDLE;
        this.patient = p;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }



	public EmergencyCall() {
		this.id = id_counter++;
	}
	
	public long getId() {
		return id;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Ambulance getAmbulance() {
		return ambulance;
	}

	public void setAmbulance(Ambulance ambulance) {
		this.ambulance = ambulance;
	}

	public EmergencyStatus getStatus() {
		return status;
	}

	public void setStatus(EmergencyStatus status) {
		this.status = status;
	}
	
	
	
}
