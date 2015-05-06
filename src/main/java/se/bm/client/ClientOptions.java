package se.bm.client;

import com.beust.jcommander.Parameter;

import se.bm.core.OptionsBase;

public class ClientOptions extends OptionsBase {
	

	@Parameter(names = "-i", description = "Activate interactive client mode with a cmd promt")
	private boolean interactive;

	public boolean isInteractive() {
		return interactive;
	}

	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

}
