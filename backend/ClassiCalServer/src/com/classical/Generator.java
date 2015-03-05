package com.classical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Generator {

	private static final char[] REPLACE_CHARS = {'<', '>', '\'', '\"', '&'};
	public static final String NOT_FOUND = generatePage("404", new String[]{}, new String[]{}, new String[]{"default"}, new String[][]{{"Page not found"}});
	
	/**
	 * Cleans input/output to prevent malicious script injection
	 * @param s String to clean
	 * @return Cleaned string
	 */
	public static String clean(String s) {
		StringBuilder builder = new StringBuilder();
		int length = s.length();
		char c;
		boolean flag = false;
		for (int i = 0; i < length; i++) {
			c = s.charAt(i);
			flag = false;
			for (int j = 0; j < REPLACE_CHARS.length; j++) {
				if (c == REPLACE_CHARS[j]) {
					builder.append("&#" + (int)c + ";");
					flag = true;
					break;
				}
			}
			if (!flag) {
				builder.append(c);
			}
		}
		return builder.toString();
//		return s.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&#34").replaceAll("'", "&#39").replaceAll("&", "&amp");
	}

	/**
	 * Generates the string of a whole html page 
	 * @param title Title of the page
	 * @param css CSS files to reference
	 * @param js JS files to reference
	 * @param classes List of HTML classes to label divs
	 * @param contents Contents of each class
	 * @return The HTML page as a string
	 */
	public static String generatePage(String title, String[] css, String[] js, String[] classes, String[][] contents) {
		List<List<String>> newContents = new LinkedList<List<String>>();
		for (String[] s : contents) {
			newContents.add(Arrays.asList(s));
		}
		return generatePage(title, Arrays.asList(css), Arrays.asList(js), Arrays.asList(classes), newContents);
	}
	
	/**
	 * Generates the string of a whole html page 
	 * @param title Title of the page
	 * @param css CSS files to reference
	 * @param js JS files to reference
	 * @param classes List of HTML classes to label divs
	 * @param contents Contents of each class
	 * @return The HTML page as a string
	 */
	public static String generatePage(String title, List<String> css, List<String> js, List<String> classes, List<List<String>> contents) {
		if (classes.size() != contents.size()) {
			System.out.println("classes.size() [" + classes.size() + "] != contents.size() [" + contents.size() + "]");
			return "Page was not generated properly";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("<html>\n\t<head>\n\t\t<title>" + title + "</title>\n");
		for (String s : css) {
			builder.append("\t\t<link rel='stylesheet' type='text/css' href='" + s + "'>\n");
		}
		for (String s : js) {
			builder.append("\t\t<script src='" + s + "'></script>\n");
		}
		builder.append("\t</head>\n\t<body>\n\t\t<div class='container'>\n");
		int max = -1;
		for (List<?> l : contents) {
			int size = l.size();
			max = (size > max) ? size : max;
		}
		for (int i = 0; i < max; i++) {
			for (int j = 0; j < classes.size(); j++) {
				if (i < contents.get(j).size()) {
					builder.append("\t\t\t<div class='" + classes.get(j) + "'>" + contents.get(j).get(i) + "</div>\n");
				}
			}
		}
		builder.append("\t\t</div>\n\t</body>\n</html>");
		return builder.toString();
	}
	
	/**
	 * Parses files and puts it into a string
	 * @param path File path of file we want
	 * @return Content of the file
	 */
	public static String getFileContent(String path) {
		File file = new File(path);
		StringBuilder builder = new StringBuilder();
		Scanner reader = null;
		try {
			reader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
		while (reader.hasNextLine()) {
			builder.append(reader.nextLine() + "\n");
		}
		reader.close();
		return builder.toString();
	}
	
}