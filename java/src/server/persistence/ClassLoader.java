package server.persistence;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoader {
	
	public static Class<?> loadClass(String filePath, String packagePath) {
		try {
			File file = new File(filePath);
			URL[] jarUrl = new URL[]{new URL("file:"+file.getAbsolutePath())};
			URLClassLoader urlClassLoader = new URLClassLoader(jarUrl);
			
			return Class.forName(packagePath, true, urlClassLoader);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
