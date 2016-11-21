package mongodb1;

import com.mongodb.*;

import com.mongodb.util.JSON;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;


public class MongoConnection {

    private DB db;
    private MongoClient mongo;

    public boolean connect() {

        //Connect
        mongo = new MongoClient();
        db = mongo.getDB("eleicoes");

        List<String> dbs = mongo.getDatabaseNames();
        System.out.println("database " + dbs);
        System.out.println("Connected to database successfully");
        return true;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public MongoClient getMongo() {
        return mongo;
    }

    public void setMongo(MongoClient mongo) {
        this.mongo = mongo;
    }
    public void getStatus() {
        CommandResult resultSet = db.getStats();
        System.out.println(resultSet);
        System.out.println(resultSet.get("count"));
        System.out.println(resultSet.get("avgObjSize"));
    }

    public void displayTable(String colecao) {
        DBCollection table = db.getCollection(colecao);
        BasicDBObject searchQuery = new BasicDBObject();
        DBCursor cursor = table.find(searchQuery);

        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    public void displayCollectionNames(JComboBox jc) {
        Set<String> collections = db.getCollectionNames();
        for (String collectionName : collections) {
            jc.addItem(collectionName);
        }
    }
    public Vector<String> selectFields(String colecao){
        DBCollection table = db.getCollection(colecao);
        BasicDBObject searchQuery = new BasicDBObject();
        DBCursor cursor = table.find(searchQuery);

        HashSet<String> allFields = new HashSet<>();
        Set<String> tempSet;
        while (cursor.hasNext()) {       /*ineficiente para colecoes muito grandes*/
            tempSet = cursor.next().keySet();

            for (String s : tempSet)
                allFields.add(s);
        }
        /*print opcional*/
        for (String s : allFields)
            System.out.println(s);

        return new Vector<>(allFields);
    }
    public void executeQuery(String query, String table, JTextArea area) {

        BasicDBObject obj = (BasicDBObject) JSON.parse(query);
        DBCollection tableObj = db.getCollection(table);
        DBCursor cursor = tableObj.find(obj);
        area.setText("");
        while (cursor.hasNext()) {
            BasicDBObject obj2 = (BasicDBObject) cursor.next();
            System.out.println(obj2);
            area.append(String.valueOf(obj2) + "\n");
        }

    }

}
