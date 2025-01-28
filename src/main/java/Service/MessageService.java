package Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;

import DAO.MessageDAO;
import Model.Message;
import Util.ConnectionUtil;

public class MessageService {
    public MessageDAO messageDAO;
    Connection connection = ConnectionUtil.getConnection();

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public boolean accountExists(int account_id) throws SQLException{
        String sql = "SELECT * FROM Account WHERE account_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,account_id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public Message createMessage(Message message) throws SQLException{
        String messageText = message.getMessage_text();
        if(messageText.length() > 255 || messageText == "" || messageText == null){
            return null;
        }
        if(!accountExists(message.getPosted_by())){
            return null;
        }
        return messageDAO.createMessage(message);
        
    }

    public List<Message> getAllMessages() throws SQLException{
        return messageDAO.getMessages();
    }

    public Message pullMessageByID(int message) throws SQLException{
        return messageDAO.getMessageByID(message);
    }

    public Message deleteMessageByID(int message) throws SQLException{
        Message deletedMessage = messageDAO.getMessageByID(message);
        if(deletedMessage != null){
            boolean deleted = messageDAO.deleteMessageByID(message);
            if(deleted){
                return deletedMessage;
            }
        }
        return null;
    }

    public Message patchMessageByID(int messageID, String updateText) throws SQLException{
        Message oldMessage = messageDAO.getMessageByID(messageID);

        if(oldMessage != null){
            boolean updatedMessage = messageDAO.updateMessageByID(messageID,updateText);
            if(updatedMessage){
                oldMessage.setMessage_text(updateText);
                return oldMessage;
            }
        }
        return null;
    }

    public List<Message> getMessagesByAccountID(int account_id){
        return messageDAO.getAccountMessages(account_id);
    }
}
