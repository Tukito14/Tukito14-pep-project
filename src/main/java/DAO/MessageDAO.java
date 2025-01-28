package DAO;
import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (message_text, posted_by, time_posted_epoch) VALUES (?,?,?)" ;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1,message.getMessage_text());
            ps.setInt(2,message.getPosted_by());
            ps.setLong(3,message.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    public List<Message> getMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message" ;
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return messages;
    }

    public Message getMessageByID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?" ;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,message_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"),rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    public boolean deleteMessageByID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,message_id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            
        }
        return false;
    }

    public boolean updateMessageByID(int message_id, String message_text){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message_text);
            ps.setInt(2, message_id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            
        }
        return false;
    }

    public List<Message> getAccountMessages(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?" ;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,account_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return messages;
    }
}
    

