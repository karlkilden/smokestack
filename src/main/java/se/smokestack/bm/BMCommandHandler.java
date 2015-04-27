package se.smokestack.bm;

public abstract class BMCommandHandler {


	abstract Command command();

	abstract void handle(BMCommand data);
	


	

}
