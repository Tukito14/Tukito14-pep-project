package DAO;
import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AccountDAO {
    public Account registerAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?,?)" ;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1,account.getUsername());
            ps.setString(2,account.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Account(rs.getInt(1),account.getUsername(),account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    public Account getCredentials(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,account.getUsername());
            ps.setString(2,account.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"),rs.getString("username"),rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }
}
