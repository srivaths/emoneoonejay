package course;


import com.mongodb.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class F7 {
    
    private DB f7DB;

    public static void main(String[] args) throws IOException {
      F7 f7 = new F7();
      f7.doit();
    }

    public F7() throws IOException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost"));
        f7DB = mongoClient.getDB("f7");
    }
    
    public void doit() {
      DBCollection imagesCollection = f7DB.getCollection("images");
      DBCollection albumsCollection = f7DB.getCollection("albums");
      // Get all image IDs.
      DBCursor imagesCursor = imagesCollection.find(new BasicDBObject(), new BasicDBObject("_id", new Integer(1)));
      List<DBObject> images = imagesCursor.toArray();
      System.out.printf("Original number of images: %d\n", images.size());
      ArrayList<Integer> removalList = new ArrayList<Integer>();
      for(DBObject imageObject:images) {
        Integer id = (Integer)imageObject.get("_id");
        // Check if image is in any album
        DBCursor albumsCursor = albumsCollection.find(new BasicDBObject("images", new BasicDBObject("$in", new Integer[]{ id})),
          new BasicDBObject("_id", "1"));
        boolean foundInAlbum = (albumsCursor.length() > 0);
        if(!foundInAlbum) {
          removalList.add(id);
        }
      }

      System.out.printf("Number of images to be axed: %d\n", removalList.size());
      BasicDBObject sunrisesTagCriterion = new BasicDBObject("tags", new BasicDBObject("$in", new String[]{"sunrises"}));
      System.out.printf("Number of images with the 'sunrises' tag: %d\n", imagesCollection.count(sunrisesTagCriterion));

      // Delete orphan images
      System.out.println("Removing images in no album...");
      for(Integer imageId: removalList) {
        imagesCollection.remove(new BasicDBObject("_id", imageId));
      }
      // Get count of images tagged with 'sunrises'
      System.out.printf("Number of images left with the 'sunrises' tag: %d\n", imagesCollection.count(sunrisesTagCriterion));
    }

}
