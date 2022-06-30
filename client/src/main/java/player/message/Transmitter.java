package player.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import player.TcpClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Optional;

public class Transmitter {

    public static void send(Message message) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            DataOutputStream out = new DataOutputStream(TcpClient.socket.getOutputStream());

            String json = objectMapper.writeValueAsString(message);
            out.writeUTF(json);
        }
        catch (IOException e) {
            throw  new IllegalArgumentException(e);
        }
    }

    public static Optional<Message> read() {
        ObjectMapper objectMapper = new ObjectMapper();

        Message message = new Message();
        try {
            DataInputStream in = new DataInputStream(TcpClient.socket.getInputStream());

            String jsonIn = null;
            try {
                jsonIn = in.readUTF();
            }
            catch (EOFException ignored) {
                return Optional.empty();
            }

            JsonNode jsonNode = objectMapper.readValue(jsonIn, JsonNode.class);
            message.type = TypeMessage.valueOf(jsonNode.get("type").asText());
            message.sourceCol = jsonNode.get("sourceCol").asInt();
            message.sourceRow = jsonNode.get("sourceRow").asInt();
            message.targetCol = jsonNode.get("targetCol").asInt();
            message.targetRow = jsonNode.get("targetRow").asInt();
        }
        catch (IOException e) { }

        return Optional.of(message);
    }
}
