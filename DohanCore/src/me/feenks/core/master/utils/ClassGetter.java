package me.feenks.core.master.utils;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassGetter {

	public static ArrayList<Class<?>> getClassesForPackage(Class<?> clas, String pkgname) {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		// String relPath = pkgname.replace('.', '/');

		// Get a File object for the package
		CodeSource src = clas.getProtectionDomain().getCodeSource();
		if (src != null) {
			URL resource = src.getLocation();
			resource.getPath();
			processJarfile(resource, pkgname, classes);
		}
		return classes;
	}

	private static Class<?> loadClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
		} catch (NoClassDefFoundError e) {
			return null;
		}
	}

	private static void processJarfile(URL resource, String pkgname, ArrayList<Class<?>> classes) {
		String relPath = pkgname.replace('.', '/');
		String resPath = resource.getPath().replace("%20", " ");
		String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
		JarFile jarFile;
		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
		}
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			String className = null;
			if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
				className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
			}
			if (className != null) {
				Class<?> c = loadClass(className);
				if (c != null)
					classes.add(c);
			}
		}
		try {
			jarFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
