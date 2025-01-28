package Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import DAO.AccountDAO;
import Model.Account;
import Util.ConnectionUtil;

public class AccountService {
    public AccountDAO accountDAO;
    Connection connection = ConnectionUtil.getConnection();

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public boolean accountExists(String username) throws SQLException{
        String sql = "SELECT * FROM Account WHERE username = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public Account addAccount(Account account) throws SQLException{
        System.out.println("attempting to add account");
        if(account.getUsername() == "" || account.getUsername() == null){
            System.out.println("username is null");
            return null;
        }else if(account.getPassword().length() < 4){
            System.out.println("pass is shorter than 4");
            return null;
        }else if(accountExists(account.getUsername()) == true){
            System.out.println("account already exists");
            return null;
        }else{
            System.out.println("registered account");
            return accountDAO.registerAccount(account);
        }

    }

    public Account accountLogin(Account account) throws SQLException{
        if(!accountExists(account.getUsername())){
            System.out.println("account doesnt exist");
            return null;
        }else{
            System.out.println("logging in");
            return accountDAO.getCredentials(account);
        }
    }
    
}
