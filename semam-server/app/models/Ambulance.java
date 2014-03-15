package models;

class Displacement {

    private String path;
    private int distance;

    int getDistance() {
        return distance;
    }

    void setDistance(int distance) {
        this.distance = distance;
    }

    String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }
}

public class Ambulance extends MobileEntity {
	
	private static long id_counter = 0;
		
	private long id;
	private String code;
    private Displacement displacement;

	public Ambulance() {
		this.id = id_counter++;
        this.setType("ambulance");
        this.getLocation().setLatitude(-20.357111);
        this.getLocation().setLongitude(-40.320417);
	}

	public long getId() {
		return id;
	}
	
	public String getCode() {
		return code;
	}

    public Displacement getDisplacement() {
        return displacement;
    }

    public void setDisplacement(Displacement displacement) {
        this.displacement = displacement;
    }

    public void Move(String poly, int distance) {
        if (this.getDisplacement()==null) {
            this.setDisplacement(new Displacement());
        }
        this.getDisplacement().setPath(poly);
        this.getDisplacement().setDistance(distance);
    }

}
