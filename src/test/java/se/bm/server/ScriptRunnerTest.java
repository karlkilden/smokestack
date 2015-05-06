package se.bm.server;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.bm.server.ftp.Script;
import se.bm.server.ftp.ScriptKey;

@RunWith(CdiTestRunner.class)
public class ScriptRunnerTest {

	@Inject
	ScriptRunner builder;
	private Script script;

	@Test
	public void cd_path_is_added_correctly() throws Exception {
		System.out.println(script.get());
		assertTrue(script.get().contains("test\\test\\test"));
	}
	
	@Test
	public void target_added_correctly() throws Exception {
		assertTrue(script.get().contains("target"));
	}

	@Before
	public void before() {
		script = builder.put(new ScriptKey(false, "name"), "test\\test\\test", "target");
	}

	@Test
		public void testFetch() throws Exception {
		}

}
