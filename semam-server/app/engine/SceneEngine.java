package engine;

import java.io.File;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import play.Logger;
import play.Play;

import scala.reflect.io.VirtualFile;

import br.ufes.inf.lprm.scene.SituationKnowledgeBaseFactory;
import br.ufes.inf.lprm.scene.SituationKnowledgeBuilderFactory;

public class SceneEngine extends Thread {

	private StatefulKnowledgeSession ksession;	
	private static SceneEngine instance = new SceneEngine();

	public SceneEngine() {
        // load up the knowledge base
        KnowledgeBase kbase;
		try {
			kbase = readKnowledgeBase();
			this.ksession = kbase.newStatefulKnowledgeSession();
			this.start();
		} catch (Exception e) {
            Logger.error("Exception: %s", e);
		}
		
	}

	public void run() {
		this.ksession.fireUntilHalt();
	}
   
	public static SceneEngine getInstance() {
		return instance;
	}
	
	public StatefulKnowledgeSession getSession() {

        if (ksession ==null) {
            KnowledgeBase kbase;
            try {
                kbase = readKnowledgeBase();
                this.ksession = kbase.newStatefulKnowledgeSession();
                this.ksession.fireUntilHalt();
            } catch (Exception e) {
                Logger.error("Exception: %s", e);
            }
        }
		return this.ksession;
	}

    private static KnowledgeBase readKnowledgeBase() throws Exception {
    	
    	KnowledgeBuilderConfiguration kBuilderConfiguration = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(null, play.Play.application().classloader());
    	KnowledgeBuilder kbuilder = SituationKnowledgeBuilderFactory.newKnowledgeBuilder(kBuilderConfiguration);
    
    	File q = Play.application().getFile("resources/query/QueryHelper.drl");
    	
        kbuilder.add(ResourceFactory.newFileResource(q), ResourceType.DRL);
        
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

    	KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(null, play.Play.application().classloader()); 
   	
        KnowledgeBase kbase = SituationKnowledgeBaseFactory.newKnowledgeBase(kbuilder, kbaseConfig);
        return kbase;
    }

}

