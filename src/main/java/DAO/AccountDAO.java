package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    public Account insertAccount(Account account) {
        Connection con = Util.ConnectionUtil.getConnection();
        
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int pkey = (int) pkeyResultSet.getLong(1);
                // this next line feels like it should retrieve the values from the db to ensure they were set correctly
                return new Account(pkey, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {System.out.println(e.getMessage());}
        return null;
    }

    public Account selectAccountByUsername(String username) {
        // since this is only being using to check if it exists for verification, returning password probably adds unnecessary risk
        Connection con = Util.ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {System.out.println(e.getMessage());}
        return null;
    }

    public Account selectAccountByID(int id) {
        // since this is only being using to check if it exists for verification, returning password probably adds unnecessary risk
        Connection con = Util.ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {System.out.println(e.getMessage());}
        return null;
    }

}
