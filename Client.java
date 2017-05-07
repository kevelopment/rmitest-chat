package test.rmi.chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Kevin on 06.05.17.
 */
public class Client extends Application
{
    private ListView<String> chatList;
    private Chat chat;

    private String clientName = "ClientXY";

    private final int WIDTH = 400;
    private final int HEIGHT = 300;

    public static void main(String[] args) throws IOException, NotBoundException
    {
        launch(args);
    }

    @Override public void start(Stage primaryStage) throws Exception
    {
        String address = "rmi://localhost/".concat(Server.CHAT_ADDRESS);
        chat = (Chat) Naming.lookup(address);

        showUserNameDialog();

        Parent root = initRoot();
        Thread updater = new Thread(new Updater(chatList, chat));
        updater.setDaemon(true);
        updater.start();

        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    private void showUserNameDialog()
    {
        TextInputDialog dialog = new TextInputDialog("Name");
        dialog.setTitle("Username Dialog");
        dialog.setHeaderText("A chat needs a name");
        dialog.setContentText("Please enter your name:");
        Optional<String> nameList = dialog.showAndWait();
        nameList.ifPresent(name -> this.clientName = name);
    }

    private Parent initRoot()
    {
        VBox box = new VBox();
        Button send = new Button("Send");
        TextField tf = new TextField();
        chatList = new ListView<>();
        send.setOnAction(e -> sendMessage(tf));
        send.setDefaultButton(true);
        box.getChildren().addAll(chatList, tf, send);
        return box;
    }

    private void sendMessage(TextField tf)
    {
        String msg = String.format("%s: \t%s", clientName,tf.getText());
        new Thread(new SendText(msg, chat, chatList)).start();
        tf.clear();
    }
}


class Updater implements Runnable
{

    private ListView<String> chatHistory;
    private Chat chat;

    public Updater(ListView<String> chatHistory, Chat chat)
    {
        this.chatHistory = chatHistory;
        this.chat = chat;
    }

    @Override public void run()
    {
        while(true)
        {
            try
            {
                ArrayList<String> history = chat.getList();
                Platform.runLater(() -> chatHistory.setItems(FXCollections.observableArrayList(history)));
                Thread.sleep(50);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}


class SendText implements Runnable
{

    private String text;
    private Chat chat;
    private ListView<String> chatHistory;

    public SendText(String text, Chat chat, ListView<String> chatHistory)
    {
        this.text = text;
        this.chat = chat;
        this.chatHistory = chatHistory;
    }

    @Override public void run()
    {
        ArrayList<String> chatResponse;
        try
        {
            chatResponse = chat.sendMsg(text);
            Platform.runLater(() -> chatHistory.setItems(FXCollections.observableArrayList(chatResponse)));
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}