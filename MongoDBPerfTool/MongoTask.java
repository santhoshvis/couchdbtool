import com.mongodb.client.MongoCollection;

import org.bson.Document;
 
public class MongoTask implements Runnable {
    private String name;
	MongoCollection<Document> table;
	Document document = null;
 
    public MongoTask(String id, String data, MongoCollection<Document> table) {
        this.name = id;
        this.table = table;
		
		document = new Document();
		document.append("_id", name);
        document.append("text", data);
    }
  
    public void run() {
		try {
		  table.insertOne(document);
          System.out.println("------------------- Record created successfully ------------------- ");
        }
		catch(Exception e) { e.printStackTrace(); }
	    return;
    }
}