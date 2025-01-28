package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    AccountService accountService;
    MessageService messageService;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::postRegisterHandler);
        app.post("login", this::postLoginHandler);
        app.post("messages", this::postMessageHandler);
        app.get("messages", this::getMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIDHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("messages/{message_id}", this::patchMessageByIDHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesByAccountID);
        return app;
    }

    /**z`
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     */
    private void postRegisterHandler(Context context) throws JsonProcessingException{
        try{
            ObjectMapper om = new ObjectMapper();
            Account account = om.readValue(context.body(), Account.class);
            System.out.println("Attempting to register account: " + account);
            Account registeredAccount = accountService.addAccount(account);
            System.out.println("Registered Account: " + registeredAccount);
            if(registeredAccount!=null){
                context.status(200).json(registeredAccount);
                System.out.println("Received registration request: " + context.body());
            }else{
                context.status(400);
            }
        }catch(Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    private void postLoginHandler(Context context) throws JsonProcessingException{
        try{
            ObjectMapper om = new ObjectMapper();
            Account account = om.readValue(context.body(), Account.class);
            Account accountLogin = accountService.accountLogin(account);
            if(accountLogin!=null){
                context.status(200).json(accountLogin);
            }else{
                context.status(401);
            }
        }catch(Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException{
        try{
            ObjectMapper om = new ObjectMapper();
            Message message = om.readValue(context.body(), Message.class);
            Message postMessage = messageService.createMessage(message);
            if(postMessage!=null){
                context.status(200).json(postMessage);
            }else{
                context.status(400);
            }
        }catch(Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    private void getMessagesHandler(Context context) throws JsonProcessingException{
        try{
            List<Message> getMessage = messageService.getAllMessages();
            if(getMessage!=null){
                context.status(200).json(getMessage);
            }
        }catch(Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException{
        try{
            int messageID = Integer.parseInt(context.pathParam("message_id"));
            Message messageByID = messageService.pullMessageByID(messageID);
            if(messageByID!=null){
                context.status(200).json(messageByID);
            }
        }catch(Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException{
        try{
            int messageID = Integer.parseInt(context.pathParam("message_id"));
            Message deletedMessageByID = messageService.deleteMessageByID(messageID);
            if(deletedMessageByID!=null){
                context.status(200).json(deletedMessageByID);
            }
        }catch(Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    private void patchMessageByIDHandler(Context context) throws JsonProcessingException{
        try{
            int messageID = Integer.parseInt(context.pathParam("message_id"));
            ObjectMapper om = new ObjectMapper();
            Message updateCheck = om.readValue(context.body(), Message.class);

            if(updateCheck.getMessage_text() == null || updateCheck.getMessage_text().isBlank() || updateCheck.getMessage_text().length() > 255){
                context.status(400);
                return;
            }
            Message patchedMessageByID = messageService.patchMessageByID(messageID,updateCheck.getMessage_text());
            if(patchedMessageByID!=null){
                context.status(200).json(patchedMessageByID);
            }else{
                context.status(400);
            }
        }catch(Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    private void getMessagesByAccountID(Context context) throws JsonProcessingException{
        try{
            int accountID = Integer.parseInt(context.pathParam("account_id"));
            List<Message> getMessageByID = messageService.getMessagesByAccountID(accountID);
            if(getMessageByID!=null){
                context.status(200).json(getMessageByID);
            }
        }catch(Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

}