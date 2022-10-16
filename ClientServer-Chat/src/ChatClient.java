import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    //creating the GUI for the client window with swing
    public static cipherAlg viginere= new cipherAlg();


    static JFrame chatPage = new JFrame("Encrypted Chat Application");
    static JPanel panel=new JPanel();
    static JTextArea chatArea =new JTextArea(22,30);
    static JTextField messageField =new JTextField(" ",50);

    static JTextArea encryptedText= new JTextArea(10,15);

    static JLabel blankLabel=new JLabel(" ");
    static JButton sendButton=new JButton("send");
    static BufferedReader in;
    static PrintWriter out;
    static JLabel nameLabel=new JLabel("");

    ChatClient(){
        chatPage.setLayout(new FlowLayout());
        //chatPage.setBackground(Color.black);
        panel.setLayout(new GridLayout());
        panel.add(sendButton,BorderLayout.SOUTH);
        panel.setBackground(Color.DARK_GRAY);
        chatPage.add(panel);
        chatPage.add(nameLabel);
        chatPage.add(new JScrollPane(chatArea));
       // textField.setBackground(Color.pink);
        chatPage.add(messageField);

        chatPage.add(sendButton);

        //chatWindow.add(encryptedText);

        chatPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatPage.setSize(800,650);
        chatPage.setVisible(true);

        messageField.setEditable(false);
        chatArea.setEditable(false); // only for displaying the messages sent and not edditing them
       // encryptedText.setEditable(false); //encryptedtext cannot be editted
        sendButton.addActionListener(new Listener());
        messageField.addActionListener(new Listener());

    }

    void startChat() throws Exception{

        String ipAddress=JOptionPane.showInputDialog(chatPage, "Enter IP Address:",
                "Identify host!!" , JOptionPane.PLAIN_MESSAGE);
        Socket soc=new Socket("",4099);
        in=new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out =new PrintWriter(soc.getOutputStream(), true);

        while(true)
        {
        String str=in.readLine();

            if(str.equals("Name required"))
            {
                String name= JOptionPane.showInputDialog(chatPage,"Enter a unique name:",
                        "Name required", JOptionPane.PLAIN_MESSAGE);
                out.println(name);
            }
           else if (str.equals("User already exists, enter a unique name"))
           {
            String name= JOptionPane.showInputDialog(chatPage,"Enter a unique name:",
                    "Name required", JOptionPane.WARNING_MESSAGE);
            out.println(name);
            }
            else if (str.startsWith("Name Accepted"))
            {
                messageField.setEditable(true);
            }
            else
            {
                chatArea.append(str+ "\n");
            }

        }
    }

    public static void main(String[] args) throws Exception{


        ChatClient client =new ChatClient();
        client.startChat();

    }




}
class Listener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e){
         cipherAlg DES= new cipherAlg();
        //String key="13";

        String encryptMessage= DES.encrypt(ChatClient.messageField.getText());
        System.out.println("Encrypted text" + encryptMessage);
        String decryptedMessage = DES.decrypt(encryptMessage);
       // System.out.println(decryptedMessage);
      //  ChatClient.out.println(ChatClient.messageField.getText());
        ChatClient.out.println(decryptedMessage);
        ChatClient.messageField.setText("");
    }
}
