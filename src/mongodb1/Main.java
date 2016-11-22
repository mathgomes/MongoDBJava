package mongodb1;

import java.sql.ResultSet;

public class Main {

    public static void main(String[] args) {
        try {

            MongoConnection c = new MongoConnection();
            OracleConnection o = new OracleConnection();
            BSONgenerator gen = new BSONgenerator();
            // Exercicio 5
            c.connect("testManyTuples");
            c.insertRandomTuples(100);

            long startTime = System.nanoTime();
            c.searchRandomTuples(100);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println("Search: " + duration);
            c.eraseTestDB();

            // Exercicio 1 e 2
            c.connect("eleicoes");

            Indexes indice = new Indexes();
            indice.criarIndexes(gen,o);
            o.displayTableNames();
            String name;
            int pkNum;
            ResultSet rs;
            for( int i = 0; i < 13; i ++) {
                name = o.getTableNames().get(i);
                pkNum = o.countPrimaryKeys(name);
                o.retrieveColumnNames(name);
                rs = o.getRs();
                gen.init(name);
                gen.tuple_to_BSON(rs,name,pkNum);
            }
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
