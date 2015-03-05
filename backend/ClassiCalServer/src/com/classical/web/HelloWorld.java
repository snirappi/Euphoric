package com.classical.web;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import com.classical.Generator;

@WebServlet("/hello")
public class HelloWorld extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String page = Generator.generatePage("Sample Servlet", new String[]{}, new String[]{}, new String[]{"default"}, new String[][]{{"Hello World"}});
		out.println(page);
		//out.println("Hello World");
	}
}
