package mongodb1;

import com.mongodb.*;
import com.mongodb.CommandResult;

import com.mongodb.util.JSON;
import org.bson.Document;
import sun.applet.resources.MsgAppletViewer_zh_CN;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;
import java.text.SimpleDateFormat;

public class MongoConnection {

    private DB db;
    private MongoClient mongo;
    private BasicDBObject Cache[];
    private int MAX_CACHE = 100;


    public boolean connect(String name) {

        //Connect
        mongo = new MongoClient();
        db = mongo.getDB(name);

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


    public void searchRandomTuples(int numOfSearches) {
        DBCollection datab = db.getCollection("testManyTuples");
        Random rd = new Random();
        int i;

        for (i = 0; i < numOfSearches; i++) {
            BasicDBObject query = new BasicDBObject();
            query.put("id", Cache[rd.nextInt(MAX_CACHE)].get("id"));
           /* DBCursor cursor = */datab.find(query);
           /* while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }*/
        }
    }

    public void insertRandomTuples(int numOfTuples) {
        DBCollection datab = db.getCollection("testManyTuples");
        BasicDBObject document;
        int i;
        int countCache = 0;
        SimpleDateFormat formatarDate = new SimpleDateFormat("ddMMyyHHmms.SSS");
        Cache = new BasicDBObject[MAX_CACHE];
        long duration = 0;

        //CRIANDO O INDEX
        datab.createIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));

        for (i = 0; i < numOfTuples; i++) {
            if (countCache < MAX_CACHE) {
                document = new BasicDBObject();
                Date data = new Date(System.currentTimeMillis());
                document.put("id", formatarDate.format(data));
                document.put("attr1", randomString(2000));
                document.put("attr2", randomString(2000));
                document.put("attr3", randomString(2000));
                document.put("attr4", randomString(2000));
                document.put("attr5", randomString(2000));
                document.put("attr6", randomString(2000));
                document.put("attr7", randomString(2000));
                document.put("attr8", randomString(2000));
                document.put("attr9", randomString(2000));
                document.put("attr10", randomString(2000));

                Cache[countCache] = document;
                countCache++;
            } else {
                document = new BasicDBObject();
                Date data = new Date(System.currentTimeMillis());
                document.put("id", formatarDate.format(data));
                document.put("attr1", randomString(2000));
                document.put("attr2", randomString(2000));
                document.put("attr3", randomString(2000));
                document.put("attr4", randomString(2000));
                document.put("attr5", randomString(2000));
                document.put("attr6", randomString(2000));
                document.put("attr7", randomString(2000));
                document.put("attr8", randomString(2000));
                document.put("attr9", randomString(2000));
                document.put("attr10", randomString(2000));
            }

            long startTime = System.nanoTime();
            datab.insert(document);
            long endTime = System.nanoTime();
            duration = duration + (endTime - startTime);
        }

        System.out.println("Insert: " + duration);
    }

    public String randomString(int size){
        int i;
        String t;
        t = "1";
        Random rd = new Random();
        for (i = 1; i < size; i++) {
            t = t + rd.nextInt(9);
        }
        return t;
    }

    public void eraseTestDB() {
        DBCollection datab = db.getCollection("testManyTuples");
        DBCursor cursor = datab.find();
        while (cursor.hasNext()) {
            datab.remove(cursor.next());
        }
    }
}
