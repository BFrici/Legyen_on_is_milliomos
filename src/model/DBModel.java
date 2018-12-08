package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DBModel implements IModel {

    private Connection conn;
    private PreparedStatement getAllKerdesPstmt;
    private PreparedStatement AddKerdesPstmt;
    private PreparedStatement UpdateKerdesPstmt;
    private PreparedStatement RemoveKerdesPstmt;

    public DBModel(Connection conn) throws SQLException {
        this.conn = conn;
        getAllKerdesPstmt = conn.prepareStatement("SELECT * FROM kerdesek");
        AddKerdesPstmt = conn.prepareStatement("INSERT INTO kerdesek (kerdes, valasz0, valasz1, valasz2, valasz3, helyesvalasz) VALUES (?,?,?,?,?,?)");
        UpdateKerdesPstmt = conn.prepareStatement("UPDATE kerdesek SET kerdes=?, valasz0=?, valasz1=?, valasz2=?, valasz3=?, helyesValasz=? WHERE idkerdesek=?");
        RemoveKerdesPstmt = conn.prepareStatement("DELETE FROM kerdesek WHERE idkerdesek=?");
    }

    @Override
    public List<Kerdes> getAllKerdesek() throws SQLException {
        List<Kerdes> kerdesek = new ArrayList<>();

        ResultSet rs = getAllKerdesPstmt.executeQuery();
        while (rs.next()) {
            int idKerdesek = rs.getInt("idkerdesek");
            String kerdes = rs.getString("kerdes");
            String valasz0 = rs.getString("valasz0");
            String valasz1 = rs.getString("valasz1");
            String valasz2 = rs.getString("valasz2");
            String valasz3 = rs.getString("valasz3");
            int helyesValasz = rs.getInt("helyesvalasz");

            Kerdes sz = new Kerdes(idKerdesek, kerdes, valasz0, valasz1, valasz2, valasz3, helyesValasz);
            kerdesek.add(sz);
        }
        rs.close();

        return kerdesek;
    }

    @Override
    public void close() throws SQLException{
            conn.close();
    }

    @Override
    public void AddKerdes(Kerdes k) throws SQLException {
        AddKerdesPstmt.setString(1, k.getKerdes());
        AddKerdesPstmt.setString(2, k.getValasz0());
        AddKerdesPstmt.setString(3, k.getValasz1());
        AddKerdesPstmt.setString(4, k.getValasz2());
        AddKerdesPstmt.setString(5, k.getValasz3());
        AddKerdesPstmt.setInt(6, k.getHelyesValasz());
        
        AddKerdesPstmt.executeUpdate();
    }

    @Override
    public void UpdateKerdes(Kerdes k) throws SQLException {
        UpdateKerdesPstmt.setString(1, k.getKerdes());
        UpdateKerdesPstmt.setString(2, k.getValasz0());
        UpdateKerdesPstmt.setString(3, k.getValasz1());
        UpdateKerdesPstmt.setString(4, k.getValasz2());
        UpdateKerdesPstmt.setString(5, k.getValasz3());
        UpdateKerdesPstmt.setInt(6, k.getHelyesValasz());
        UpdateKerdesPstmt.setInt(7, k.getIdKerdesek());
        
        UpdateKerdesPstmt.executeUpdate();
    }

    @Override
    public void RemoveKerdes(Kerdes k) throws SQLException {
        RemoveKerdesPstmt.setInt(1, k.getIdKerdesek());
        
        RemoveKerdesPstmt.executeUpdate();
    }

}
