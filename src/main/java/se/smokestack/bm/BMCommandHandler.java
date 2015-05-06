package se.smokestack.bm;

import se.bm.server.CommandWrapper;

public interface BMCommandHandler {


	public  Command command();

	public  void handle(CommandWrapper commandWrapper);
	


	

}
