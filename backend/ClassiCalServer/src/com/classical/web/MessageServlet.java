package com.classical.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classical.Generator;

@WebServlet("/messages")
public class MessageServlet extends HttpServlet {
	
	private List<String> messages = new LinkedList<String>();
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String message = request.getParameter("message");
		if (message != null) {
			messages.add(message);
		}
		String[] top = {"<h1>Messages</h1>"};
		String[] contents = {"<div id='list'>"};
		for (int i = 0; i < messages.size(); i++) {
			contents[0] += "<br>" + messages.get(i);
		}
		contents[0] += "</div>";
		String[] bot = {"<br><br><input id='message' value='Enter message here!'><button onclick='send()'>Send</button>"};
		String page = Generator.generatePage("Sample Servlet", new String[]{}, new String[]{"/index.js", "http://code.jquery.com/jquery-1.11.1.js"}, new String[]{"top", "messages", "bot"}, new String[][]{top, contents, bot});
		out.println(page);
		out.close();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String message = request.getParameter("message");
		if (message != null) {
			messages.add(message);
		}
		String contents = "";
		for (int i = 0; i < messages.size(); i++) {
			contents += "<br>" + messages.get(i);
		}
		out.println(contents);
		out.close();
	}
}
