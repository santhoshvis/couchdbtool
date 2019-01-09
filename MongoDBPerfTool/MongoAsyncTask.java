import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.SingleResultCallback;
import org.bson.Document;
 
public class MongoAsyncTask implements Runnable {
    private String name;
	MongoCollection<Document> table;
	Document document = null;
 
    public MongoAsyncTask(String id, String data, MongoCollection<Document> table) {
        this.name = id;
        this.table = table;
		
		document = new Document();
		document.append("_id", name);
        document.append("text", data);
    }
  
    public void run() {
		try {
		  table.insertOne(document, new SingleResultCallback<Void>() {
                   @Override
                   public void onResult(final Void result, final Throwable t) {
                        System.out.println("Inserted!");
                   }
          });
          System.out.println("------------------- Record created successfully ------------------- ");
        }
		catch(Exception e) { e.printStackTrace(); }
	    return;
    }
}