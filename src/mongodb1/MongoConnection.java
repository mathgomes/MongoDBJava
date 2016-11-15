package mongodb1;

import com.mongodb.*;

import org.bson.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MongoConnection {

    private DB db;
    private MongoClient mongo;

    public void connect() {

        //Connect
        mongo = new MongoClient();
        db = mongo.getDB("eleicoes");

        List<String> dbs = mongo.getDatabaseNames();
        System.out.println("database " + dbs);
        System.out.println("Connected to database successfully");

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
}
