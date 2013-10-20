import com.mongodb.MongoClient;
import com.mongodb.*;
/**
* CAUTION: This program is not idempotent!  
* It will modify the target database collection.
*
*
*/
public class Hw22 {
	private MongoClient client;
	private DB db;
	private DBCollection collection;
	private static final String STUDENT_ID = "student_id";
	private static final String STUDENT_SCORE = "score";
  private static final int ASCENDING = 1;
  private static final int DESCENDING = -1;
	public Hw22() {
  }
  public void connect(final String dbName, final String collectionName) throws Exception {
    client = new MongoClient();
    db = client.getDB(dbName);
    collection = db.getCollection(collectionName);
	}
	public void doit() throws Exception {
    // Connect to student.grades
		connect("students", "grades");
		System.out.printf("Collection has %d documents.\n", collection.count());
    
    // Define the search criteria
		BasicDBObject searchCriteria = new BasicDBObject();
		searchCriteria.append("type", "homework");
    
    // Define the sort criteria
		BasicDBObject sortCriteria = new BasicDBObject(STUDENT_ID, ASCENDING);
		sortCriteria.append(STUDENT_SCORE, ASCENDING);

    // Search & sort
		DBCursor cursor = collection.find(searchCriteria);
		cursor.sort(sortCriteria);
    
    // Iterate
    int lastStudent = -1;
		while(cursor.hasNext()) {
			DBObject object = cursor.next();
      int currentStudent = (int)object.get(STUDENT_ID);
      String deleteString = (lastStudent==currentStudent)?"": "--> delete";
			System.out.printf("Student: %d, score: %f %s\n", currentStudent, object.get(STUDENT_SCORE), deleteString);
      if(lastStudent != currentStudent) {
        collection.remove(object);
      }
      lastStudent = currentStudent;
		}
	}

  // Driver.
	public static void main(String[] x) throws Exception {
		Hw22 hw2 = new Hw22();
		hw2.doit();
	}
}
