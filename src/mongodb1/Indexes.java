package mongodb1;
import java.util.Vector;
/**
 * Created by Sabrina Faceroli on 15/11/2016.
 */
public class Indexes {

    public void criarIndexes(){
        try{
            OracleConnection o = new OracleConnection();
            BSONgenerator gen = new BSONgenerator();

            String[] chaves;
            Vector<String> tabelas;

            o.displayTableNames();
            tabelas = o.getTableNames();

            int i;
            for(i = 0; i < tabelas.size(); i++) {
                chaves = o.displayPKName(tabelas.get(i));
                gen.IndexCreation(tabelas.get(i), chaves);
            }

        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
