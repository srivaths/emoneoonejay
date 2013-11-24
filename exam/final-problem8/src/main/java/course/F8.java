package course;

import org.bson.types.ObjectId;
import com.mongodb.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class F8 {



        public static void main(String[] args) throws IOException {
            MongoClient c =  new MongoClient(new MongoClientURI("mongodb://localhost"));
            DB db = c.getDB("test");
            DBCollection animals = db.getCollection("animals");


            BasicDBObject animal = new BasicDBObject("animal", "monkey");
            System.out.printf("Object id before inserting 'monkey': %s\n", animal.getObjectId("_id"));
            animals.insert(animal);
            System.out.printf("Object id after inserting 'monkey' : %s\n", animal.getObjectId("_id"));
            animal.removeField("animal");
            animal.append("animal", "cat");
            animals.insert(animal);
            System.out.printf("Now attempting to insert 'cat'\n");
            System.out.printf("Object id after inserting 'cat'    : %s\n", animal.getObjectId("_id"));
            animal.removeField("animal");
            animal.append("animal", "lion");
            animals.insert(animal);
						System.out.printf("There are %d animals in the 'animals' collection.\n", animals.count());

        }
}

