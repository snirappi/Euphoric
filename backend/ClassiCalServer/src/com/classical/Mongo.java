package com.classical;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class Mongo {
	
	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	
	private static Mongo instance;
	private DB university;
	private DBCollection users;
	private DBCollection courses;
	private List<DBCollection> chatMessages;
	private List<DBCollection> forumMessages;
	
	private Mongo(String host, int port) {
		MongoClient client = null;
		try {
			client = new MongoClient(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		//initialize basic collections
		university = client.getDB("purdue");
		users = university.getCollection("users");
		courses = university.getCollection("courses");
		//build list of message collections (each course will have its own collection of messages)
		chatMessages = new LinkedList<DBCollection>();
		forumMessages = new LinkedList<DBCollection>();
		DBCursor cursor = courses.find();
		while (cursor.hasNext()) {
			DBCollection course = university.getCollection((String)cursor.next().get("crn"));
			chatMessages.add(course.getCollection("chatMessages"));
			forumMessages.add(course.getCollection("forumMessages"));
		}
	}
	
	public static Mongo getInstance() {
		if (instance == null) {
			instance = new Mongo(HOST, PORT);
		}
		return instance;
	}
	
	public DBCollection getUsers() {
		return users;
	}
	
	public DBCollection getCourses() {
		return users;
	}
	
	public DBCollection getChatMessages(String crn) {
		return getMessages(chatMessages, "chatMessages", crn);
	}
	
	public DBCollection getForumMessages(String crn) {
		return getMessages(forumMessages, "forumMessages", crn);
	}
	
	public List<DBCollection> getChatMessages() {
		return chatMessages;
	}
	
	public List<DBCollection> getForumMessages() {
		return forumMessages;
	}
	
	private static DBCollection getMessages(List<DBCollection> messages, String name, String crn) {
		for (int i = 0; i < messages.size(); i++) {
			DBCollection col = messages.get(i);
			if (col.getName().equals(crn + "." + name)) {
				return col;
			}
		}
		return null;
	}
	
	//TODO: POPULATE COURSES/MESSAGES
	//TODO: SIMPLE FUNCTIONS
	
	public static void main(String args[]) {
		try {
			// To connect to mongodb server
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			// Now connect to your databases
			DB db = mongoClient.getDB("test");

			System.out.println("Connect to database successfully");

			DBCollection test = db.getCollection("test");
			while (test.count() != 0) {
				test.remove(test.findOne());
			}
//			BasicDBObject doc = new BasicDBObject("title", "MongoDB").append("value", 0);
			for (int i = 0; i < 10; i++) {
				BasicDBObject doc = new BasicDBObject("title", "MongoDB").append("value", i);
				test.insert(doc);
			}
			DBCursor cursor = test.find();
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
			cursor.close();
			// boolean auth = db.authenticate(myUserName, myPassword);
			// System.out.println("Authentication: "+auth);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}