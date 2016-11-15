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

public class BSONgenerator {

    private FileWriter writer;

    public BSONgenerator() {
        File file = new File("CRUD.txt");
        try {
            file.createNewFile();
            writer = new FileWriter(file);
            writer.write("use eleicoes\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        // creates a FileWriter Object

    }

    public void init(String name) {
        try {
            writer.write("db.createCollection(\""+ name +"\")\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void tuple_to_BSON(ResultSet rs, String name, int pkSize) {

        try {

            while(rs.next()) {
                writer.write("db." + name + ".insert(");
                tuple_to_BSON_aux(rs,pkSize);
                writer.write(")\n");
            }
            writer.write("\n");
            writer.flush();

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
        writer.write("{");
        writer.write("_id:");
        if(pkSize == 1) {
            writer.write(rsTypes.get(0));
            j++;
        }
        else {
            writer.write("{");
            for( j = 1; j <= pkSize; j ++) {
                writer.write(metaData.getColumnName(j) + ":");
                writer.write(rsTypes.get(j-1));

                if( j < pkSize) writer.write(",");
            }
            writer.write("}");
        }
        writer.write(",");
        for( int i = j; i <= size; i ++) {
            if(Objects.equals(rsTypes.get(i - 1), "\"" + "null" + "\"")) continue;
            writer.write(metaData.getColumnName(i) + ":");
            writer.write(rsTypes.get(i-1));

            if( i < size) writer.write(",");
        }
        writer.write("}");
    }

}