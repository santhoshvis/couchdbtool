import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
//import com.mongodb.WriteConcern;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;

public class MongoAsyncThreadPool
{
    public static void main(String[] args) throws Exception
    {
		ArrayList<MongoAsyncTask> tasks = new ArrayList<MongoAsyncTask>();
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(250);
        MongoClient mongoClient = null;
		
		try
        {
		  String table = args[0];
		  String content = new String(Files.readAllBytes(Paths.get(args[1])));
		  int repeat = Integer.parseInt(args[2]);
		  
		  //MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(10)
		  //                                                         .writeConcern(WriteConcern.UNACKNOWLEDGED)
		  //													     .build();
		  //MongoClient mongoClient = MongoClients.create();
		  
          // globally
          //CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
          //                              fromProviders(PojoCodecProvider.builder().automatic().build()));
										
		  mongoClient = MongoClients.create("mongodb://localhost");
		  
		  MongoDatabase database = mongoClient.getDatabase("library");
		  MongoCollection<Document> collection = database.getCollection(table);
		  
          for (int i = 1; i <= repeat; i++)
          {
			String name = "Task" + i;
			System.out.println(name);
            MongoAsyncTask task = new MongoAsyncTask(name, content, collection);
			tasks.add(task);
          }

		  long startTime = System.currentTimeMillis();
		  for (int j = 0; j < tasks.size(); j++)
          {
            executor.execute(tasks.get(j));
          }

		  long endTime = System.currentTimeMillis();
 		  long timeElapsed = endTime - startTime;
          System.out.println(timeElapsed);
		  
		  executor.shutdown();
		  while(!executor.isTerminated()) {}
		}
		catch(Exception e)
        {
          System.err.println(e.getMessage());
        }
		//finally { mongoClient.close(); }
    }
}