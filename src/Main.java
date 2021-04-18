import express.Express;
import express.middleware.Middleware;
import express.middleware.FileProviderOptions;

import java.io.IOException;

import java.nio.file.Paths;

class Main {

	public static void main(String[] args) throws Exception {
		Express app = setupApp();
		Configuration config = new Configuration();
		app.listen(config, config.getPort());
	}

	private static Express setupApp() {

		Express app = new Express();

		app.use((req, res) -> {
			res.setHeader(
				"Content-Security-Policy",
				"upgrade-insecure-requests;default-src 'self';style-src https://" +
				System.getenv("REPL_SLUG") +
				"." +
				System.getenv("REPL_OWNER") +
				".repl.co/styles/;style-src-elem https://" + 
				System.getenv("REPL_SLUG") +
				"." +
				System.getenv("REPL_OWNER") +
				".repl.co/styles/;style-src-attr 'none';font-src 'none';child-src 'none';connect-src 'self';frame-src https://replit.com;manifest-src 'none';frame-ancestors sameorgin;img-src 'self';media-src 'none';object-src 'none';prefetch-src 'none';worker-src 'none';script-src-attr 'none';script-src " +
				System.getenv("REPL_SLUG") +
				"." + 
				System.getenv("REPL_OWNER") +
				".repl.co/scripts/;script-src-elem " +
				System.getenv("REPL_SLUG") +
				"." + 
				System.getenv("REPL_OWNER") +
				".repl.co/scripts/;"
			);
			res.setHeader("X-Content-Type-Options", "nosniff");
		});

		String folderPath = Paths.get("../src/public").toString();

		try {

			app.use(
				Middleware.statics(
					folderPath
				)
			);

		} catch(IOException ioe) {
			ioe.printStackTrace();
			System.exit(2);
		}

		/* app.use((req, res) -> {
			System.out.println("Requested URL: " + req.getURI().getPath());
		}); */

		app.bind(new Router());

		return app;
	}
}