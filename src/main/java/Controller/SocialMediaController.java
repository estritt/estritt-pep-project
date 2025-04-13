package Controller;

import Model.*;
import Service.*;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        // finding acct username only needs implementation in DAO / it's used by other service methods
        // app.get("accounts/{username}". this::getAccountByUsernameHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAccountMessagesHandler);

        // app.start(8080);

        return app;
    }

    /**
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
            // don't need bc 200 is the default
            // ctx.status(200);
        } else {ctx.status(400);}
    }

    // needed to check if username is present before registration
    // don't need this actually - just the dao method which is used inside a different service methods
    // private void getAccountByUsernameHandler(Context ctx) throws JsonProcessingException {
    //     ObjectMapper mapper = new ObjectMapper();
    //     String username = mapper.readValue(ctx.body(), String.class);
    //     Account accountWithUsername = accountService.findAccountByUsername(username);
    //     if (accountWithUsername != null) {
    //         ctx.json(mapper.writeValueAsString(accountWithUsername));
    //         ctx.status(200);
    //     } else {ctx.status(400);}
    // }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.login(account);
        if (loggedInAccount != null) {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
        } else {ctx.status(401);}
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
        } else {ctx.status(400);}
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> allMessages = messageService.getAllMessages();
        ctx.json(mapper.writeValueAsString(allMessages));
    }

    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = (int)Integer.parseInt(ctx.pathParam("message_id"));
        Message foundMessage = messageService.getMessageByID(id);
        if (foundMessage != null) {ctx.json(mapper.writeValueAsString(foundMessage));}
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = (int)Integer.parseInt(ctx.pathParam("message_id"));
        Message foundMessage = messageService.deleteMessage(id);
        if (foundMessage != null) {ctx.json(mapper.writeValueAsString(foundMessage));}
    }

    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = (int)Integer.parseInt(ctx.pathParam("message_id"));
        // String text = mapper.readValue(ctx.body(), String.class);
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message patchedMessage = messageService.patchMessage(id, message.getMessage_text());
        if (patchedMessage != null) {ctx.json(mapper.writeValueAsString(patchedMessage));}
        else {ctx.status(400);}
    }

    private void getAccountMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = (int)Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountID(id);
        ctx.json(mapper.writeValueAsString(messages));
    }

}