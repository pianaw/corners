package message;

public class Message {

    public TypeMessage type;
    public Integer sourceCol;
    public Integer sourceRow;
    public Integer targetCol;
    public Integer targetRow;
    public Message() { };

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type + ",\n" +
                " sourceCol=" + sourceCol + ",\n" +
                " sourceRow=" + sourceRow + ",\n" +
                " targetCol=" + targetCol + ",\n" +
                " targetRow=" + targetRow +  "\n" +
                '}';
    }
}
