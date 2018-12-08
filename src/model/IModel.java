package model;

import java.util.List;
import java.sql.SQLException;

public interface IModel {
    
    List<Kerdes> getAllKerdesek() throws SQLException;
    void AddKerdes(Kerdes k) throws SQLException;
    void UpdateKerdes(Kerdes k) throws SQLException;
    void RemoveKerdes(Kerdes k) throws SQLException;
    void close() throws SQLException;
    
}
