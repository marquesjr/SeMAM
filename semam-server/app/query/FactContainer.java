package query;

import org.drools.runtime.rule.FactHandle;

public class FactContainer {

	private FactHandle handle;
	private Object obj;
	
	public FactContainer(FactHandle handle, Object obj) {
		
		this.handle = handle;
		this.obj = obj;
		
	}

	public FactHandle getHandle() {
		return handle;
	}

	public Object getObj() {
		return obj;
	}
}
