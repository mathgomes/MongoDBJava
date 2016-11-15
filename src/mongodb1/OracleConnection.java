package mongodb1;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.Objects;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class OracleConnection {

    private Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private ResultSet rs2;
    private Vector<String> tableNames;
    private Vector<String> columnNames;

    public OracleConnection(){
        conectar();
    }

    public boolean conectar(){

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        // Dentro dos laboratorios
            /*connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@192.168.183.15:1521:orcl",
                    "a8532321",
                    "a8532321");*/
            // Fora dos laboratorios
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@grad.icmc.usp.br:15215:orcl",
                    "a8532321",
                    "a8532321");
            stmt = connection.createStatement();
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet executeQuery(String name) throws SQLException {
        rs = stmt.executeQuery("select * from "+ name);
        return rs;
    }

    public void displayTableNames(){
        String s = "";
        try {
            s = "select table_name from user_tables";
            rs = stmt.executeQuery(s);
            tableNames = new Vector<>();
            while (rs.next()) {
                tableNames.add(rs.getString("table_name"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void retrieveColumnNames(String name) {

        try {
            rs = executeQuery(name);
            ResultSetMetaData metaData = rs.getMetaData();

            // Names of columns
            columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            for(String a : columnNames) {
                System.out.println(a);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int countPrimaryKeys(String name) {

        try {
            rs2 = stmt.executeQuery("SELECT count(*) FROM user_constraints cons, user_cons_columns cols" +
                     " WHERE cols.table_name = " + "'" + name + "'" +
                    " AND cons.constraint_type = 'P'" +
                    " AND cons.constraint_name = cols.constraint_name");
            if (rs2.next()) {
                return rs2.getInt(1);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return 1;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public Vector<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(Vector<String> tableNames) {
        this.tableNames = tableNames;
    }

    public Vector<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Vector<String> columnNames) {
        this.columnNames = columnNames;
    }
}
