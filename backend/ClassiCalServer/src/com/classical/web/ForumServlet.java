package com.classical.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classical.Forum;
import com.classical.Message;

@WebServlet("/forums")
public class ForumServlet extends HttpServlet {
	
	//TODO: BUILD FORUM OBJECT
	//TODO: LOGINS (???)
	//TODO: INTERACT WITH FORUM OBJECT WITH GET/POST REQUESTS
	//TODO: ONE FORUM OBJECT PER CLASS
	//TODO: DUMMY FORUM POPULATING
	
	private static final String[] COMMANDS = {"getParents", "getChildren", "post", "reply"};
	
//	private List<Forum> forums;	//forums for each course
	private Forum forum = new Forum("43855");
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		String command = request.getParameter("command");
		if (command == null) {
			error(out, "COMMAND_NOT_FOUND");
			return;
		}
		/*
		//TODO: POST TO COURSE BY CRN
		String crn = request.getParameter("crn");
		if (crn == null) {
			error(out, "COURSE_NOT_FOUND");
			return;
		}
		*/
		String contents = "COMMAND_NOT_FOUND";
		if (command.equals(COMMANDS[0])) {
			StringBuilder list = new StringBuilder();
			List<Message> parents = forum.getParents();
			contents = Message.toJson(parents);
		} else if (command.equals(COMMANDS[1])) {
			String parentId = request.getParameter("parentId");
			if (parentId == null) {
				error(out, "PARENT_ID_NOT_FOUND");
				return;
			}
			List<Message> children = forum.getChildren(Integer.parseInt(parentId));
			contents = Message.toJson(children);
		} else if (command.equals(COMMANDS[2])) {
			String user = request.getParameter("user");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			if (user == null || title == null || content == null) {
				error(out, "POST_BODY_NOT_FOUND");
				return;
			}
			forum.post(user, title, content);
			contents = "SUCCESSFUL_POST";
		} else if (command.equals(COMMANDS[3])) {
			String user = request.getParameter("user");
			String content = request.getParameter("content");
			String parentId = request.getParameter("parentId");
			if (user == null || content == null || parentId == null) {
				error(out, "REPLY_BODY_NOT_FOUND");
				return;
			}
			forum.reply(user, content, Integer.parseInt(parentId));
			contents = "SUCCESSFUL_REPLY";
		}
		out.println(contents);
		out.close();
	}
	
	private void error(PrintWriter o, String e) {
		o.println(e);
		o.close();
	}
	
}
