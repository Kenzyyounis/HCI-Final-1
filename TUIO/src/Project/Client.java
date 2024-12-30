package Project;

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class Client {
    public static Client instance;
    
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    private BufferedReader in = null;

    // Constructor to put IP address and port
    public Client(String address, int port) {
        try {
            if (instance != null){
                return;
            }
            
            // Establish a connection
            socket = new Socket(address, port);
            System.out.println("Connected to server");

            // Input from terminal
            input = new DataInputStream(System.in);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            // Start a new thread to keep reading messages from the server
            new Thread(new ServerListener()).start();
            
            instance = this;
        } catch (UnknownHostException u) {
            System.out.println("Unknown host: " + u.getMessage());
        } catch (IOException i) {
            System.out.println("IO error: " + i.getMessage());
        }
    }
    
    public void sendJson(String key, String value) {
        try {
            // Construct the JSON string manually
            String json = "{" + "\"" + key + "\":\"" + value + "\"}";

            // Send the JSON string as a message
            out.writeUTF(json);
            out.flush();
            System.out.println("Sent: " + json);
        } catch (IOException e) {
            System.out.println("Error sending JSON: " + e.getMessage());
        }
    }

    // Inner class to handle incoming messages from the server in a separate thread
    private class ServerListener implements Runnable {
        public void run() {
            System.out.println("Listening for messages from server...");
            String receivedMessage;
            try {
                while ((receivedMessage = in.readLine()) != null) {
                    System.out.println("Received: " + receivedMessage);
                    
                    try {
                        HashMap<String, String> message = parseJson(receivedMessage);
                        System.out.println("Parsed JSON: " + message);
                        
                        if (message.containsKey("Action") && message.containsKey("Value")){
                            String action = message.get("Action");
                            String value = message.get("Value");
                            if (action.startsWith("FaceIdentification")){
                                if (CircleMenu.instance != null && CircleMenu.instance.getPublicModeActive() == false){
                                    CircleMenu.instance.toggleButtonVisibility(Boolean.parseBoolean(value));
                                }
                            }
                            else if (action.startsWith("UserAge")){
                                if (CircleMenu.instance != null){
                                    CircleMenu.instance.togglePrivacyScreen(Boolean.parseBoolean(value));
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error parsing JSON: " + e.getMessage());
                    }
                }
            } catch (IOException i) {
                System.out.println("Connection closed: " + i.getMessage());
            } finally {
                try {
                    in.close();
                    input.close();
                    out.close();
                    socket.close();
                } catch (IOException i) {
                    System.out.println("Error closing connection: " + i.getMessage());
                }
            }
        }
        
        private HashMap<String, String> parseJson(String json) throws Exception {
            HashMap<String, String> map = new HashMap<>();

            // Remove surrounding braces and split by commas
            json = json.trim();
            if (!json.startsWith("{") || !json.endsWith("}")) {
                throw new Exception("Invalid JSON format");
            }
            json = json.substring(1, json.length() - 1); // Remove the braces

            // Split the key-value pairs
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length != 2) {
                    throw new Exception("Invalid key-value pair: " + pair);
                }

                // Remove quotes and trim whitespace
                String key = keyValue[0].trim().replaceAll("^\"|\"$", "");
                String value = keyValue[1].trim().replaceAll("^\"|\"$", "");
                map.put(key, value);
            }

            return map;
        }
    }
}