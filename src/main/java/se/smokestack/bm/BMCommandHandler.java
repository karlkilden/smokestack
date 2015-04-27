package se.smokestack.bm;

import org.apache.deltaspike.core.util.ExceptionUtils;

public abstract class BMCommandHandler {


	abstract Command command();

	abstract void handle(BMCommand data);
	


	

}
