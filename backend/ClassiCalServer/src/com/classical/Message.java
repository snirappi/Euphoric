package com.classical;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Message extends MongoDoc {
	
	public static final int DOES_NOT_EXIST = -1;
	
	private Date date;
	private int upnotes;
	private String user;
	private String title;
	private String content;
	private List<String> upnoters;
	private int id = DOES_NOT_EXIST;
	private int parentId = DOES_NOT_EXIST;
	
	public Message(String user, String content, int id, int parentId) {
		this(user, "", content, id, parentId);
	}
	
	public Message(String user, String title, String content, int id, int parentId) {
		this(user, title, content, id, parentId, new Date(), 0, new LinkedList<String>());
	}
	
	public Message(DBObject document) {
		this(
			(String)document.get("user"),
			(String)document.get("title"),
			(String)document.get("content"),
			(int)document.get("id"),
			(int)document.get("parentId"),
			(Date)document.get("date"),
			(int)document.get("upnotes"),
			(List<String>)document.get("upnoters"));
	}

	public Message(String user, String title, String content, int id, int parentId, Date date, int upnotes, List<String> upnoters) {
		this.user = user;
		this.title = title;
		this.content = content;
		this.id = id;
		this.parentId = parentId;
		this.date = date;
		this.upnotes = upnotes;
		this.upnoters = upnoters;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getTitle() {
		return Generator.clean(title);
	}
	
	public String getContent() {
		return Generator.clean(content);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean hasParent() {
		return parentId != DOES_NOT_EXIST;
	}
	
	public int getParentId() {
		return parentId;
	}
	
	public Date getDate() {
		return date;
	}
	
	public int getUpnotes() {
		return upnotes;
	}
	
	public boolean hasUpnoted(String user) {
		return upnoters.contains(user);
	}
	
	public void upnote(String user) {
		if (!hasUpnoted(user)) {
			upnoters.add(user);
			upnotes++;
		}
	}
	
	public void report(DBCollection reports, String whistleblower) {
		BasicDBObject reporter = new BasicDBObject("wb", whistleblower);
		reporter.put("content", content);
		reporter.put("reportee", user);
		reports.save(reporter);
	}
	
	public String toJson() {
		return "{\"user\":\"" + user +
				"\",\"title\":\"" + title +
				"\",\"content\":\"" + content +
				"\",\"id\":\"" + id +
				"\",\"parentId\":\"" + parentId + "\"}";
	}
	
	public static String toJson(List<Message> messages) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"messages\":[");
		for (int i = 0; i < messages.size(); i++) {
			builder.append(messages.get(i).toJson());
			if (i < messages.size() - 1) {
				builder.append(",");
			}
		}
		builder.append("]}");
		return builder.toString();
	}
	
	public static List<Message> toMessages(DBCollection collection) {
		List<Message> messages = new LinkedList<Message>();
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			messages.add(new Message(cursor.next()));
		}
		return messages;
	}

	@Override
	public BasicDBObject toDocument() {
		return 
			new BasicDBObject("id", id).
				append("parentId", parentId).
				append("user", user).
				append("title", title).
				append("content", content).
				append("date", date).
				append("upnotes", upnotes).
				append("upnoters", upnoters);
	}
	
}
