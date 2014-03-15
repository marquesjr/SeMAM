package controllers;

import java.util.List;

import models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.gson.Gson;

import org.drools.runtime.rule.QueryResultsRow;
import play.libs.Json;
import play.mvc.*;
import engine.SceneEngine;
import query.FactContainer;
import query.QueryHelper;

public class Application extends Controller {

    public static Result index() {
        Patient p = new Patient("José");
        p.getLocation().setLatitude(-20.268566);
        p.getLocation().setLongitude(-40.300019);
        SceneEngine.getInstance().getSession().insert(p);
        EmergencyCall call = new EmergencyCall(p);
        SceneEngine.getInstance().getSession().insert(call);
    	return redirect("assets/index.html");
    }

    public static Result getEntities() {
        Gson gson = new Gson();
        List<MobileEntity> entities = QueryHelper.GetEntities(SceneEngine.getInstance().getSession());
        String s = gson.toJson(entities);
        JsonNode result = Json.parse(s);
        return ok(result).as("application/json");
    }

    public static Result listCalls() {
        Gson gson = new Gson();
        List<EmergencyCall> calls = QueryHelper.GetEmergencyCallsByStatus(SceneEngine.getInstance().getSession(), EmergencyStatus.IDLE);
        String s = gson.toJson(calls);
        JsonNode result = Json.parse(s);
        return ok(result).as("application/json");
    }

    public static Result assignCall() {
        JsonNode json = request().body().asJson();
        if(json != null) {
            QueryResultsRow row = QueryHelper.GetEmergencyCall(SceneEngine.getInstance().getSession(), json.findPath("id").textValue(), EmergencyStatus.IDLE);
            EmergencyCall e  = (EmergencyCall) row.get("call");
            e.setStatus(EmergencyStatus.UNDERWAY);
            SceneEngine.getInstance().getSession().update(row.getFactHandle("call"), e);
            return ok().as("application/json");
        } else return badRequest("Expecting Json data");
    }

    public static Result listUsers() {
    	Gson gson = new Gson();
    	List<User> users = QueryHelper.GetUsers(SceneEngine.getInstance().getSession());
    	String s = gson.toJson(users);
    	JsonNode result = Json.parse(s);
        return ok(result).as("application/json");
    }
    
    public static Result newUser()  {
    	
    	JsonNode json = request().body().asJson();
    	
    	if(json != null) {
        	User user = new User();
        	user.setFirstName(json.findPath("firstName").textValue());
        	user.setLastName(json.findPath("lastName").textValue());
        	SceneEngine.getInstance().getSession().insert(user);
    	    return ok().as("application/json");
    	} else {
    		return badRequest("Expecting Json data");
    	}    	
    }    
    
    public static Result editUser(String id) {
    	FactContainer fact = QueryHelper.GetUser(SceneEngine.getInstance().getSession(), id);
    	User user = (User) fact.getObj();
    	if (user!=null) {
    		ObjectNode result = Json.newObject();
    		result.put("id", user.getId());
    		result.put("firstName", user.getFirstName());
    		result.put("lastName", user.getLastName());
    		return ok(result).as("application/json");
    	}
    	else {
    		return ok().as("application/json");  
    	}
    }
    
    public static Result updateUser(String id) {
    	JsonNode json = request().body().asJson();
    	if(json != null) {
    		
        	FactContainer fact = QueryHelper.GetUser(SceneEngine.getInstance().getSession(), id);
        	User user = (User) fact.getObj();
        	if (user!=null) {
        		user.setFirstName(json.findPath("firstName").textValue());
            	user.setLastName(json.findPath("lastName").textValue());
            	SceneEngine.getInstance().getSession().update(fact.getHandle(), user);
            	return ok().as("application/json");
        	}
        	return ok().as("application/json");

    	} else {
    		return badRequest("Expecting Json data");
    	}
    }
    
    public static Result deleteUser(String id) {
    	FactContainer fact = QueryHelper.GetUser(SceneEngine.getInstance().getSession(), id);
    	User user = (User) fact.getObj();
    	if (user!=null) {
    		SceneEngine.getInstance().getSession().retract(fact.getHandle());
    		return ok().as("application/json");
    	}
    	else {
    		return ok().as("application/json");  
    	}
    }        

}
