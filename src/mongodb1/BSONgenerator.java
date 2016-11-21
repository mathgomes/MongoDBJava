package mongodb1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Vector;
import java.util.Random;

public class BSONgenerator {

    private FileWriter writer1;
    private FileWriter writer2;

    public BSONgenerator() {
        File file = new File("CRUD.txt");
        File file2 = new File("Indexes.txt");
        try {
            file.createNewFile();
            file2.createNewFile();
            writer1 = new FileWriter(file);
            writer1.write("use eleicoes\n");
            writer2 = new FileWriter(file2);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void init(String name) {
        try {
            writer1.write("db.createCollection(\""+ name +"\")\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void tuple_to_BSON(ResultSet rs, String name, int pkSize) {

        try {

            while(rs.next()) {
                writer1.write("db." + name + ".insert(");
                tuple_to_BSON_aux(rs,pkSize);
                writer1.write(")\n");
            }
            writer1.write("\n");
            writer1.flush();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    public void tuple_to_BSON_aux(ResultSet rs, int pkSize) throws SQLException, IOException {

        ResultSetMetaData metaData = rs.getMetaData();
        int size = rs.getMetaData().getColumnCount();
        Vector<Integer> types = new Vector<>();
        Vector<String> rsTypes = new Vector<>();
        for( int i =0; i < size; i ++) {
            types.add(metaData.getColumnType(i+1));
            if(types.get(i) == Types.CHAR || types.get(i) == Types.VARCHAR) {
                rsTypes.add( "\"" + rs.getString(i+1) + "\"");
            }
            else if(types.get(i) == Types.DOUBLE || types.get(i) == Types.FLOAT || types.get(i) == Types.INTEGER
                    || types.get(i) == Types.DECIMAL || types.get(i) == Types.NUMERIC) {
                rsTypes.add(String.valueOf(rs.getInt(i+1)));
            }
            else if (types.get(i) == Types.DATE) {
                rsTypes.add(String.valueOf(rs.getDate(i+1)));
            }
        }
        int j = 1;
        writer1.write("{");
        writer1.write("_id:");
        if(pkSize == 1) {
            writer1.write(rsTypes.get(0));
            j++;
        }
        else {
            writer1.write("{");
            for( j = 1; j <= pkSize; j ++) {
                writer1.write(metaData.getColumnName(j) + ":");
                writer1.write(rsTypes.get(j-1));

                if( j < pkSize) writer1.write(",");
            }
            writer1.write("}");
        }
        writer1.write(",");
        for( int i = j; i <= size; i ++) {
            if(Objects.equals(rsTypes.get(i - 1), "\"" + "null" + "\"")) continue;
            writer1.write(metaData.getColumnName(i) + ":");
            writer1.write(rsTypes.get(i-1));

            if( i < size) writer1.write(",");
        }
        writer1.write("}");
    }

    public void IndexCreation(String tabela, String[] chaves) throws IOException {

        if(chaves.length == 0)
            return;

        int i = 0;
        String command;
        command = "db." + tabela + ".createIndex( { ";

        do{
            if(i >= 1)
                command = command + ", ";

            command = command + chaves[i] + ": 1";
            i++;
        } while (i < (chaves.length-1));

        if(chaves.length >= 2) {
            command = command + ", " + chaves[(chaves.length - 1)] + ": 1 } )";
        } else
            command = command + " } )";

        writer2.write(command + "\n");
        writer2.flush();
    }

    public void insertRandomTuples(int numOfTuples) {
        int i;
        String command;
        command = "db.testManyTuples.insertMany( [";
        for (i = 0; i < numOfTuples; i++){
             command = command + "{ attr1 : \"" + randomString(2000) + "\"," +
                                    " attr2 : \"" + randomString(2000) + "\"," +
                                    " attr3 : \"" + randomString(2000) + "\"," +
                                    " attr4 : \"" + randomString(2000) + "\"," +
                                    " attr5 : \"" + randomString(2000) + "\"," +
                                    " attr6 : \"" + randomString(2000) + "\"," +
                                    " attr7 : \"" + randomString(2000) + "\"," +
                                    " attr8 : \"" + randomString(2000) + "\"," +
                                    " attr9 : \"" + randomString(2000) + "\"," +
                                    " attr10 : \"" + randomString(2000) + "\" }";
            if (i < (numOfTuples-1))
                command = command + ", ";
        }
        command = command + " ] )";
        System.out.println(command);
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
}
