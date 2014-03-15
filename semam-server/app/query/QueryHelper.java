package query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;

import models.*;

public class QueryHelper {

    public static List<MobileEntity> GetEntities(StatefulKnowledgeSession ksession) {
        QueryResults results = ksession.getQueryResults("MobileEntities");
        List<MobileEntity> list = new ArrayList<MobileEntity>();
        for ( QueryResultsRow row : results ) {
            MobileEntity p = (MobileEntity) row.get( "entity");
            list.add(p);
        }
        return list;
    }


	public static List<Patient> GetPatients(StatefulKnowledgeSession ksession) {
		QueryResults results = ksession.getQueryResults("Patients");
		List<Patient> list = new ArrayList<Patient>();
		for ( QueryResultsRow row : results ) {
			Patient p = (Patient) row.get( "patient");
		    list.add(p);
		}
		return list;
	}	
	
	public static List<EmergencyCall> GetEmergencyCallsByStatus(StatefulKnowledgeSession ksession, EmergencyStatus status) {
		QueryResults results = ksession.getQueryResults("EmergenciesByStatus", new Object[] { status });
		List<EmergencyCall> list = new ArrayList<EmergencyCall>();
		for ( QueryResultsRow row : results ) {
			EmergencyCall p = (EmergencyCall) row.get( "call");
		    list.add(p);
		}
		return list;
	}
	
	public static List<User> GetUsers(StatefulKnowledgeSession ksession) {
		QueryResults results = ksession.getQueryResults("Users");
		List<User> list = new ArrayList<User>();
		for ( QueryResultsRow row : results ) {
			User p = (User) row.get( "user");
		    list.add(p);
		}
		return list;
	}	
	
	public static FactContainer GetUser(StatefulKnowledgeSession ksession, String id) {
		QueryResults results = ksession.getQueryResults("User", new Object[] { id });
		for ( QueryResultsRow row : results ) {
			return new FactContainer(row.getFactHandle("user"), row.get("user"));
		}
		return null;
	}
	
	public static QueryResultsRow GetPatient(StatefulKnowledgeSession ksession, String id) {
		QueryResults results = ksession.getQueryResults("Patient", new Object[] { id });
		
		Iterator<QueryResultsRow> i = results.iterator();

		if (i.hasNext()) { 
			return i.next();
		}
		else return null;
	}

    public static QueryResultsRow GetEmergencyCall(StatefulKnowledgeSession ksession, String id, EmergencyStatus status) {
        QueryResults results = ksession.getQueryResults("EmergencyCall", new Object[] { id, status });
        Iterator<QueryResultsRow> i = results.iterator();
        if (i.hasNext()) {
            return i.next();
        }
        else return null;
    }

    public static QueryResultsRow GetAmbulanceAssignment(StatefulKnowledgeSession ksession, String id) {
        QueryResults results = ksession.getQueryResults("Assignment", new Object[] { id });

        Iterator<QueryResultsRow> i = results.iterator();

        if (i.hasNext()) {
            return i.next();
        }
        else return null;
    }

	
	public static QueryResultsRow GetAmbulance(StatefulKnowledgeSession ksession, String id) {
		QueryResults results = ksession.getQueryResults("Ambulance", new Object[] { id });
		Iterator<QueryResultsRow> i = results.iterator();
		if (i.hasNext()) { 
			return i.next();
		}
		else return null;
	}	
	
	
}
