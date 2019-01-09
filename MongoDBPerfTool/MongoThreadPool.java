import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.*;
import java.util.*;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class MongoThreadPool
{
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception
    {
		ArrayList<MongoTask> tasks = new ArrayList<MongoTask>();
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(50);
        MongoClient mongoClient = null;

        final LogManager lm = LogManager.getLogManager();
        for( final Enumeration<String> i = lm.getLoggerNames(); i.hasMoreElements(); ) {
           lm.getLogger( i.nextElement()).setLevel( Level.OFF );
        }
		
		try
        {
		  String table = args[0];
		  String content = new String(Files.readAllBytes(Paths.get(args[1])));
		  int repeat = Integer.parseInt(args[2]);
		  
		  mongoClient = new MongoClient();
		  mongoClient.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
		  MongoClientOptions options = mongoClient.getMongoClientOptions();
		  MongoClientOptions.builder().connectionsPerHost(40).threadsAllowedToBlockForConnectionMultiplier(10).connectTimeout(15).build();
		  
		  MongoDatabase database = mongoClient.getDatabase("library");
		  MongoCollection<Document> collection = database.getCollection(table);
		  
          for (int i = 1; i <= repeat; i++)
          {
			String name = "Task" + i;
			System.out.println(name);
            MongoTask task = new MongoTask(name, content, collection);
			tasks.add(task);
          }

		  long startTime = System.currentTimeMillis();
		  for (int j = 0; j < tasks.size(); j++)
          {
            executor.execute(tasks.get(j));
          }
		  
		  executor.shutdown();
		  while(!executor.isTerminated()) {}

		  long endTime = System.currentTimeMillis();
 		  long timeElapsed = endTime - startTime;
          System.out.println("Elapsed time : " + timeElapsed);
		}
		catch(Exception e)
        {
          System.err.println(e.getMessage());
        }
		finally { mongoClient.close(); }
    }
}