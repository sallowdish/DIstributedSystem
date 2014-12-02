import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
public class CLI {
	private static final Logger log = Logger.getLogger(CLI.class.getName());
	private String[] args = null;
	private Options options = new Options();
	public CommandLine cmd;

	
	public CLI(String[] args) {
		this.args = args;
		options.addOption("h", "help", false, "show help.");
		options.addOption("ip","ip",false,"ip address to listen");
		options.addOption("port","port-num",false,"port number to listen");
		options.addOption("dir", "directory", true, "local directory to write to");
		options.addOption("p", "primary", false, "start as a primary server");
	}
	public boolean parse() {
		CommandLineParser parser = new BasicParser();

		cmd = null;
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("h"))
				help();
			if (cmd.hasOption("dir")) {
//				log.log(Level.INFO, "Using cli argument -dir=" + cmd.getOptionValue("dir"));
				// Whatever you want to do with the setting goes here
				return true;
			} else {
				log.log(Level.SEVERE, "MIssing -dir option");
				help();
				return false;
			}

		} catch (ParseException e) {
			log.log(Level.SEVERE, "Failed to parse comand line properties", e);
			help();
			return false;
		}
	}

	private void help() {
		// This prints out some help
		HelpFormatter formater = new HelpFormatter();

		formater.printHelp("RSFSTServer [-ip client ip address] [-port client port number] -dir Root directory", options);
		System.exit(0);
	}
}

