package se.smokestack;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@ExceptionHandler
public class SmokestackExceptionHandler {
	private static final Logger log = LogManager.getLogger();

	public void handleException(@Handles ExceptionEvent<Exception> event) {
		log.error("Script failure", event.getException());
	}
}
