package se.smokestack.bm;

import org.junit.Test;

public class ProcessRunnerTest {

	@Test
	public void testRunProcess() throws Exception {
		
		ProcessRunner runner = new ProcessRunner();
		runner.runProcess("cmd /c C:/projects/WinSCP/WinSCP.exe /script=C:/projects/WinSCP/test.script");
	
	}

}
