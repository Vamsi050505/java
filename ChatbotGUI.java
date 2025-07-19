import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatbotGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private Chatbot bot;

    public ChatbotGUI() {
        bot = new Chatbot();
        setTitle("AI Chatbot");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = inputField.getText();
                if (userInput.trim().isEmpty()) return;
                chatArea.append("You: " + userInput + "\n");

                String response = bot.getResponse(userInput);
                chatArea.append("Bot: " + response + "\n\n");

                inputField.setText("");
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatbotGUI gui = new ChatbotGUI();
            gui.setVisible(true);
        });
    }
}
