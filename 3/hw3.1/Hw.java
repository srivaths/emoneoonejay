import com.mongodb.MongoClient;
import com.mongodb.*;
import java.util.*;
/**
* This is the solution for hw3.1 for m101j in Fall 2013.
* Prerequisites:
*  - Mongo 2.4+ is running on localhost
*  - There is a database called 'school' with a collection called 'students'
*
* CAUTION: This program is not idempotent!  
* It will modify the target database collection.
*
*/
public class Hw {
	private MongoClient client;
	private DB db;
	private DBCollection collection;
	private BasicDBObject document;
	public static final String MONGO_ID = "_id";
	public static final String SET_OPERATOR = "$set";

	private static final String STUDENT_ID = MONGO_ID;
	private static final String INDIVIDUAL_SCORE_TYPE = "type";
	private static final String INDIVIDUAL_SCORE = "score";
	
	private static final String STUDENT_SCORES = "scores";
	private static final String STUDENT_NAME = "name";
	private static final String VALUE_HOMEWORK = "homework";

  public void connect(final String dbName, final String collectionName) throws Exception {
    client = new MongoClient();
    db = client.getDB(dbName);
    collection = db.getCollection(collectionName);
	}
	public void doit() throws Exception {
    // Connect to schoo.students
		connect("school", "students");
		System.out.printf("Collection has %d documents.\n", collection.count());
    
		// Iterate over the collection
		DBCursor cursor = collection.find();
		while(cursor.hasNext()) {
			// Now working with a single student's document.
			DBObject document = cursor.next();
			ArrayList scores = (ArrayList)document.get(STUDENT_SCORES);
			
			// Find the lowest homework score
			// We assume that there will only be 2 homework scores.
			Iterator scoresIterator = scores.iterator();
			DBObject hw1 = null, hw2 = null;
			while(scoresIterator.hasNext()) {
				DBObject score = (DBObject)scoresIterator.next();
				if(VALUE_HOMEWORK.equals(score.get(INDIVIDUAL_SCORE_TYPE))) {
					if(hw1 == null) { 
						hw1 = score; 
					} else { hw2 = score; }
					// IF we have captured hw1&2, don't need to look any further 
					if(hw1 != null && hw2 != null) {
						break;
					}
				}
			}
			
			// Figure out which of hw1, hw2 to remove.
			Double hw1score = (Double)hw1.get(INDIVIDUAL_SCORE);
			Double hw2score = (Double)hw2.get(INDIVIDUAL_SCORE);
			String name = (String)document.get(STUDENT_NAME);

			// Remove from scores list
			scores.remove((hw1score <= hw2score) ? hw1: hw2);
			
			// Do the DB update.
			collection.update(
				new BasicDBObject(STUDENT_ID, document.get(STUDENT_ID)), // who
				new BasicDBObject("$set", new BasicDBObject(STUDENT_SCORES, scores)), // what 
				false, // upsert
				false); // multi
		}
	}

  // Driver.
	public static void main(String[] x) throws Exception {
		Hw hw = new Hw();
		hw.doit();
	}
}
