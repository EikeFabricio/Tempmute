package br.com.eike.tempmute.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class MyDB {

    private Connection connection;

    public MyDB(File path) {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:/" +
                    new File(path.getAbsolutePath(), "database.sqlite"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void update(String row) {
        try (PreparedStatement stm = getConnection().prepareStatement(row)) {
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Object> query(String row) {
        List<Object> objectList = new ArrayList<>();

        try (ResultSet rs = getConnection().prepareStatement(row).executeQuery()){
            int index = 1;
            while (rs.next()) {
                objectList.add(rs.getObject(index));
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objectList;
    }

    public void close() {
        try {
            if (getConnection() != null && !getConnection().isClosed()) getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
