package com.classical;

import com.mongodb.BasicDBObject;

public abstract class MongoDoc {
	/**
	 * Makes interacting with the database easier
	 * @return BasicDBDocument version of this object
	 */
	public abstract BasicDBObject toDocument();
}