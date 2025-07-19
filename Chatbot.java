import java.util.HashMap;
import java.util.Map;

public class Chatbot {
    private Map<String, String> knowledgeBase;

    public Chatbot() {
        knowledgeBase = new HashMap<>();
        trainBot();
    }

    private void trainBot() {
        // Frequently Asked Questions and Answers
        knowledgeBase.put("hello", "Hi there! How can I help you?");
        knowledgeBase.put("how are you", "I'm a bot, but I'm functioning perfectly!");
        knowledgeBase.put("what is your name", "I'm ChatBot, your virtual assistant.");
        knowledgeBase.put("bye", "Goodbye! Have a great day!");
        knowledgeBase.put("help", "You can ask me about our services or say hello!");
        // Add more Q&A here
    }

    public String getResponse(String input) {
        input = input.toLowerCase().trim();

        // Simple keyword matching:
        for (String key : knowledgeBase.keySet()) { 
            if (input.contains(key)) {
                return knowledgeBase.get(key);
            }
        }
        return "Sorry, I don't understand. Can you please rephrase?";
    }
}
