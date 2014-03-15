package controllers.contexts;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.drools.runtime.rule.QueryResultsRow;

import com.google.gson.Gson;

import play.libs.Json;
import play.mvc.*;
import engine.SceneEngine;

import models.Patient;
import models.Location;
import models.Heartbeat;

import query.QueryHelper;

public class PatientCtrl extends Controller {
	
    public static Result listPatients() {
    	Gson gson = new Gson();
    	List<Patient> patients = QueryHelper.GetPatients(SceneEngine.getInstance().getSession());
    	String s = gson.toJson(patients);
    	JsonNode result = Json.parse(s);
        return ok(result).as("application/json");
    }	
	
	public static Result newPatient() {
		JsonNode json = request().body().asJson();
		if(json != null) {
			String name = json.findPath("patient").findValue("name").asText();
			Patient p = new Patient(name);
			SceneEngine.getInstance().getSession().insert(p);
			SceneEngine.getInstance().getSession().insert(p.getHeartbeat());
			SceneEngine.getInstance().getSession().insert(p.getLocation());
			ObjectNode result = Json.newObject();
			result.put("id", p.getId());
			return ok(result).as("application/json");
		} else return badRequest("Expecting JSON data");
	}
	
    public static Result updateHeartbeat(String id) {
    	JsonNode json = request().body().asJson();
    	if(json != null) {
        	QueryResultsRow row = QueryHelper.GetPatient(SceneEngine.getInstance().getSession(), id);
        	if (row!=null) {
        		Heartbeat h = (Heartbeat) row.get("heartbeat");
        		h.setRate( json.findPath("patient").findValue("heartbeat").asInt());        		
        		SceneEngine.getInstance().getSession().update(row.getFactHandle("heartbeat"), h);
        		SceneEngine.getInstance().getSession().update(row.getFactHandle("patient"), row.get("patient"));        		
            	return ok().as("application/json");
        	}
        	else return badRequest("No patient with id=" + id + "found");
    	} else return badRequest("Expecting JSON data");
    }
    
    public static Result updateLocation(String id) {
    	JsonNode json = request().body().asJson();
    	if(json != null) {
        	QueryResultsRow row = QueryHelper.GetPatient(SceneEngine.getInstance().getSession(), id);
        	if (row!=null) {
        		Location l = (Location) row.get("loc");	
        		l.setLatitude( new Double(json.findPath("patient").findPath("location").findValue("latitude").asDouble()) );
        		l.setLongitude( new Double(json.findPath("patient").findPath("location").findValue("longitude").asDouble()) );       		
        		SceneEngine.getInstance().getSession().update(row.getFactHandle("location"), l);
        		//SceneEngine.getInstance().getSession().update(row.getFactHandle("patient"), row.get("patient"));     		
            	return ok().as("application/json");
        	}
        	else return badRequest("No patient with id=" + id + " found");
    	} else return badRequest("Expecting JSON data");
    }    

}
