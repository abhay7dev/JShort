```java


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Temp {
	public static void main(String[] args) throws IOException {
		Path rootDir = Paths.get(args[0]);

        if (!Files.exists(rootDir) || !Files.isDirectory(rootDir)) {
            throw new IOException(rootDir + " does not exists or isn't a directory.");
        }
	}
}

```

```java

// javac -cp ./lib/express.jar Temp.java
// java -cp .:./lib/express.jar Temp

import express.Express;
import express.middleware.Middleware;

public class Temp {
	public static void main(String[] args) throws java.io.IOException {
		// Create instance
		Express app = new Express() {{
			// Define middleware-route for static site
			use("/", Middleware.statics("temp/"));
		}};
		app.listen(8080);
	}
}

```

```java

import express.Express;
import express.middleware.Middleware;

import java.io.IOException;
import java.nio.file.*;

class Main {

	public static void main(String[] args) throws Exception {
		Express app = setupApp();
		app.listen(8080);
	}

	private static Express setupApp() {

		Express app = new Express();

		try {

			app.use(
				Middleware.statics(
					"/home/runner/JShort/hi"
				)
			);

		} catch(IOException ioe) {
			ioe.printStackTrace();
			System.exit(2);
		}

		return app;
	}
}

```