import java.io.IOException;
import java.net.URI;

/**
 * Write a web server called ChatServer that supports the path and behavior
 * described below. It should keep track of a single string that gets added to
 * by incoming requests. The requests should look like this:
 * 
 * /add-message?s=<string>&user=<string>
 * The effect of this request is to concatenate the string given after user=, a
 * colon (:), and then the string after s, a newline (\n), and then respond with
 * the entire string so far. That is, it adds a chat message of the form <user>:
 * <message>
 * 
 * So, for example, after
 * 
 * /add-message?s=Hello&user=jpolitz
 * The page should show
 * 
 * jpolitz: Hello
 * and after
 * 
 * /add-message?s=How are you&user=yash
 * the page should show
 * 
 * jpolitz: Hello
 * yash: How are you
 */

class Handler implements URLHandler {
    // The data this server will keep track of, chat
    // each new message will be concatenated to
    String chat = "Welcome to the ChatServer! Make a request to begin.\n\n";

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            return chat;
        } else {
            String message;
            String user;
            String[] parameters = url.getQuery().split("&");
            if (parameters[0].substring(0, 2).equals("s=")) {
                message = parameters[0].substring(2);
            } else {
                return "Error in parsing message string!";
            }
            if (parameters[1].substring(0, 5).equals("user=")) {
                user = parameters[1].substring(5);
            } else {
                return "Error in parsing user string!";
            }
            chat += String.format("%s: %s\n", user, message);
            return chat;
        }
    }
}

class ChatServer {
    public static void main(String[] args) throws IOException {
        int port;
        // If no arguments provided, return
        if (args.length == 0) {
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException error) {
            System.out.println("Port must be a number! Try any number between 1024 and 49151");
            throw error;
        }

        Server.start(port, new Handler());
    }
}
