package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public Message insertMessage(Message message) {
        Connection con = Util.ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int pkey = (int) pkeyResultSet.getLong(1);
                // this next line feels like it should retrieve the values from the db to ensure they were set correctly
                return new Message(pkey, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {System.out.println(e.getMessage());}
        return null;
    }

    public ArrayList<Message> selectAllMessages() {
        Connection con = Util.ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<Message>();

        try {
            String sql = "SELECT * FROM Message";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                messages.add(
                    new Message(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getLong(4)
                    )
                );
            }
        } catch (SQLException e) {System.out.println(e.getMessage());}

        return messages;
    }

    public Message selectMessageByID(int id) {
        Connection con = Util.ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return new Message(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getString(3),
                    rs.getLong(4)
                );
            }
        } catch (SQLException e) {System.out.println(e.getMessage());}

        return null;
    }

    public Message deleteMessage(int id) {
        Connection con = Util.ConnectionUtil.getConnection();

        try {

            Message message = selectMessageByID(id);
            if (message == null) {return null;}

            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            int checkSuccess = preparedStatement.executeUpdate();
            if (checkSuccess == 0) {System.out.println("Deletion failed");}
            
            return message;
        } catch (SQLException e) {System.out.println(e.getMessage());}

        return null;
    }

    public Message patchMessage(int id, String text) {
        Connection con = Util.ConnectionUtil.getConnection();

        try {

            Message originalMessage = selectMessageByID(id);
            if (originalMessage == null) {return null;} // must exist

            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(2, id);
            preparedStatement.setString(1, text);

            int checkSuccess = preparedStatement.executeUpdate();
            if (checkSuccess == 0) {System.out.println("Patch failed");}
            else {
                return selectMessageByID(id);
            }
            
        } catch (SQLException e) {System.out.println(e.getMessage());}

        return null;
    }

    public ArrayList<Message> selectMessagesByAccountID(int id) {
        Connection con = Util.ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<Message>();

        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                messages.add(
                    new Message(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getLong(4)
                    )
                );
            }
        } catch (SQLException e) {System.out.println(e.getMessage());}

        return messages;
    }

}
