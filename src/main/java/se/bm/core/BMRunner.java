package se.bm.core;

public interface BMRunner {

	void start(String[] args);

	String configPath();

	void runValidation();
	
	RunModeType runModeType();


}
