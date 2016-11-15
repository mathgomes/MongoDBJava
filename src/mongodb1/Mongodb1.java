/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongodb1;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

import com.mongodb.ServerAddress;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author junio
 */
public class Mongodb1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {             //Connect

            MongoClient mongo = new MongoClient();
            DB db = mongo.getDB("labbd2016");
            System.out.println("Connect to database successfully");

            DBCollection table = db.getCollection("alunos");
            System.out.println("Collection retrieved successfully");

            List<String> dbs = mongo.getDatabaseNames();
            System.out.println(dbs);

            Set<String> collections = db.getCollectionNames();
            System.out.println(collections);
            //Insert
            BasicDBObject document = new BasicDBObject();
            document.put("nome", "Adao");
            document.put("idade", 30);
            table.insert(WriteConcern.SAFE, document);
            for (DBObject doc : table.find()) {
                System.out.println(doc);
            }            
            //Find
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("idade", new BasicDBObject("$gt",1));
            DBCursor cursor = table.find(searchQuery);
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
