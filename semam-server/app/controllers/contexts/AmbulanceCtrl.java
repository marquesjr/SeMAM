package controllers.contexts;

import com.google.gson.Gson;
import models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.drools.runtime.rule.QueryResultsRow;

import play.libs.Json;
import play.mvc.*;
import engine.SceneEngine;

import models.Ambulance;
import models.Location;

import query.QueryHelper;

import java.util.Iterator;
import java.util.List;

public class AmbulanceCtrl extends Controller {

    public static Result Move(String id) {
        JsonNode json = request().body().asJson();
        if(json != null) {
            QueryResultsRow row = QueryHelper.GetAmbulance(SceneEngine.getInstance().getSession(), id);
            if (row!=null) {
                Ambulance a = (Ambulance) row.get("ambulance");
                a.Move(json.findValue("polyline").asText(), json.findValue("distance").asInt());
                return ok().as("application/json");
            }
            else return badRequest("No ambulance found!");
        } else {
            return badRequest("Expecting Json data");
        }
    }

    public static Result atPatientLocation(String id) {
        QueryResultsRow row = QueryHelper.GetEmergencyCall(SceneEngine.getInstance().getSession(), id, EmergencyStatus.UNDERWAY);
        if (row != null) {
            Ambulance ambulance = (Ambulance) row.get("ambulance");

            EmergencyCall call  = (EmergencyCall) row.get("call");
            call.setStatus(EmergencyStatus.ONGOING);
            SceneEngine.getInstance().getSession().update(row.getFactHandle("call"), call);
            return ok().as("application/json");
        } else return badRequest("no results");
    }

    public static Result solved(String id) {
        QueryResultsRow row = QueryHelper.GetEmergencyCall(SceneEngine.getInstance().getSession(), id, EmergencyStatus.ONGOING);
        if (row != null) {
            EmergencyCall call  = (EmergencyCall) row.get("call");
            call.setStatus(EmergencyStatus.SOLVED);
            SceneEngine.getInstance().getSession().update(row.getFactHandle("call"), call);
            return ok().as("application/json");
        } else return badRequest("no results");
    }

	public static Result getAssignment(String id) {
        Gson gson = new Gson();
        QueryResultsRow row = QueryHelper.GetEmergencyCall(SceneEngine.getInstance().getSession(), id, EmergencyStatus.ASSIGNED);
        JsonNode result = null;

        //FOR TESTS ONLY BEGIN

        /*JsonNodeFactory jf = JsonNodeFactory.instance;

        ObjectNode result = new ObjectNode(jf);
        ObjectNode location = new ObjectNode(jf);
        //UFES
        location.put("latitude", -20.268566);
        location.put("longitude", -40.300019);
        result.put("location", location);

        return ok(result).as("application/json");    */

        //FOR TESTS ONLY END

        if (row != null) {
            Patient p = (Patient) row.get("patient");
            String s = gson.toJson(p);
            result = Json.parse(s);
            EmergencyCall call = (EmergencyCall) row.get("call");
            call.setStatus(EmergencyStatus.UNDERWAY);
            SceneEngine.getInstance().getSession().update(row.getFactHandle("call"), call);
            return ok(result).as("application/json");
        } else return ok().as("application/json");
    }

    public static Result newAmbulance() {
        Ambulance a = new Ambulance();

        SceneEngine.getInstance().getSession().insert(a);
        SceneEngine.getInstance().getSession().insert(a.getLocation());

        List<EmergencyCall> calls = QueryHelper.GetEmergencyCallsByStatus(SceneEngine.getInstance().getSession(), EmergencyStatus.IDLE);

        Iterator i = calls.iterator();

        if (i.hasNext()) {
            EmergencyCall call = (EmergencyCall) i.next();
            call.setAmbulance(a);
            call.setStatus(EmergencyStatus.ASSIGNED);
            SceneEngine.getInstance().getSession().update(SceneEngine.getInstance().getSession().getFactHandle(call), call);
        }

        Gson gson = new Gson();
        String s = gson.toJson(a);
        JsonNode result = Json.parse(s);
        return ok(result).as("application/json");
	}
    
    public static Result updateLocation(String id) {
    	JsonNode json = request().body().asJson();
    	if(json != null) {
        	QueryResultsRow row = QueryHelper.GetAmbulance(SceneEngine.getInstance().getSession(), id);
        	if (row!=null) {
        		Location l = (Location) row.get("location");       		
        		l.setLatitude( new Double(json.findPath("location").findValue("latitude").asDouble()) );
        		l.setLongitude( new Double(json.findPath("location").findValue("longitude").asDouble()) );
        		SceneEngine.getInstance().getSession().update(row.getFactHandle("ambulance"), row.get("ambulance"));
        		SceneEngine.getInstance().getSession().update(row.getFactHandle("location"), l);     		
            	return ok().as("application/json");
        	}
        	else return badRequest("No ambulance found!");
    	} else {
    		return badRequest("Expecting Json data");
    	}
    }    

}
