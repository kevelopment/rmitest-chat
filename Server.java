package test.rmi.chat;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Kevin on 06.05.17.
 */
public class Server
{

    public static final String CHAT_ADDRESS = "Chat";

    public static void main(String[] args) throws RemoteException
    {
        Registry registry = LocateRegistry.createRegistry(1099);
        ChatImpl chat = new ChatImpl();
        registry.rebind(CHAT_ADDRESS, chat);
        System.out.println("Chat Server started");
    }
}
