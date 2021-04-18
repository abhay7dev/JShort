import express.ExpressListener;

public class Configuration implements ExpressListener {

	private int port;
	private String host;

	public Configuration() {
		this(
			System.getenv("REPL_SLUG") +
			"." +
			System.getenv("REPL_OWNER") +
			".repl.co", 
			5050
		);
	}

	public Configuration(int port) {
		this(
			System.getenv("REPL_SLUG") +
			"." +
			System.getenv("REPL_OWNER") +
			".repl.co", 
			port
		);
	}

	public Configuration(String host) {
		this(host, 5050);
	}

	public Configuration(String host, int port) {
		this.port = port;
		this.host = host;
	}

	@Override
	public void action() {
		System.out.println(
			"\nListening on " + 
			this.host + 
			":" + 
			this.port + 
			" at " + 
			new java.util.Date() + 
			".\n"
		);

	}

	public int getPort() {
		return this.port;
	}

	public String getHost() {
		return this.host;
	}

}