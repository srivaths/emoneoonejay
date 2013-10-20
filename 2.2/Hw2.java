import com.mongodb.MongoClient;
import com.mongodb.*;
public class Hw2 {
	private MongoClient client;
	private DB db;
	private DBCollection collection;
	public Hw2() {
  }
  public void connect() throws Exception {
    client = new MongoClient();
    db = client.getDB("test");
    collection = db.getCollection("first");
	}
	public void doit() throws Exception {
		connect();
		BasicDBObject document = new BasicDBObject();
		document
		.append("name", "Sri")
		.append("address", "1 Main St.");
		collection.insert(document);
	}
	public static void main(String[] x) throws Exception {
		Hw2 hw2 = new Hw2();
		hw2.doit();
	}
}
