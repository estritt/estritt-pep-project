package Service;

import Model.Message;
import DAO.MessageDAO;
import Service.AccountService; // I am not sure what the best way to query the account table during message creation is

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message) {
        int textLength = message.getMessage_text().length();
        AccountService accountService = new AccountService();
        if (textLength == 0 || textLength >= 255) {
            System.out.println("Message must be between 1 and 255 characters.");
            return null;
        } else if (accountService.findAccountByID(message.getPosted_by()) == null) {
            System.out.println("Poster is not a valid user.");
            return null;
        }
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.selectAllMessages();
    }

    public Message getMessageByID(int id) {
        return messageDAO.selectMessageByID(id);
    }

    public Message deleteMessage(int id) {
        return messageDAO.deleteMessage(id);
    }

    public Message patchMessage(int id, String text) {
        int textLength = text.length();
        if (textLength == 0 || textLength >= 255) {
            System.out.println("Message must be between 1 and 255 characters.");
            return null;
        }

        return messageDAO.patchMessage(id, text);
    }

    public List<Message> getMessagesByAccountID(int id) {
        //could save time on non-existing users if that is checked first - would be slower if not needed though
        return messageDAO.selectMessagesByAccountID(id);
    }

}
