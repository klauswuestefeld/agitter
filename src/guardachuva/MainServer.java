package guardachuva;

import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.server.AgitosServer;

import java.io.File;

import sneer.bricks.hardware.clock.ticker.ClockTicker;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.exceptions.robust.RobustExceptionLogging;
import sneer.bricks.hardware.io.log.filter.LogFilter;
import sneer.bricks.snapps.system.log.file.LogToFile;
import sneer.bricks.snapps.system.log.sysout.LogToSysout;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.ClosureX;

public class MainServer {
	
	public static void main(String[] args) throws Exception {
		Environments.runWith(Brickness.newBrickContainer(), new ClosureX<Exception>() { @Override public void run() throws Exception {
			start();
		}});
	}

	
	private static void start() throws Exception {
		startLogging();
		
		my(ClockTicker.class);
		
		AgitosServer.startRunning();
// FIXME
//		MailerServer.startRunning();
		
		my(Threads.class).waitUntilCrash();
	}
	
	
	private static void startLogging() {
		my(LogFilter.class).whiteListEntries().add(""); //Mostra todos as mensagens no log.

		my(RobustExceptionLogging.class).turnOn();
		my(LogToSysout.class);
		my(LogToFile.class).startWritingLogTo(newLogFile());
	}

	
	private static File newLogFile() {
		return new File("logs/log_" + System.currentTimeMillis() + ".log");
	}
}
