import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import message.Message;
import message.TypeMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler {
    private TcpServer server;
    private final Socket socket;

    public ClientHandler(TcpServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public Message readMessage() {
        String jsonIn;
        Message message = new Message();
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            jsonIn = in.readUTF();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readValue(jsonIn, JsonNode.class);

            message = new Message();
            message.type = TypeMessage.valueOf(jsonNode.get("type").asText());
            message.sourceCol = jsonNode.get("sourceCol").asInt();
            message.sourceRow = jsonNode.get("sourceRow").asInt();
            message.targetCol = jsonNode.get("targetCol").asInt();
            message.targetRow = jsonNode.get("targetRow").asInt();

        }
        catch (IOException e) {
            try {
                socket.close();
            }
            catch (IOException ignored) {
                System.out.println("Client disconnected");
            }
        }

        return message;
    }

    public void sendMessage(Message message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String jsonOut = objectMapper.writeValueAsString(message);
            out.writeUTF(jsonOut);
        }
        catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ignored) { }
        }
    }

    public void clientDown() {
        try {
            this.socket.close();
        } catch (IOException e) { }
    }
}
