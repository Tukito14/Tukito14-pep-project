package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
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
            context.status(500).result("An error occurred: " + e.getMessage());
        }
    }


}