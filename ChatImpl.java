package test.rmi.chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Kevin on 06.05.17.
 */
public class ChatImpl extends UnicastRemoteObject implements Chat
{
    private ArrayList<String> chatList;

    protected ChatImpl() throws RemoteException
    {
        this.chatList = new ArrayList<>();
    }

    @Override public ArrayList<String> sendMsg(String msg)
    throws RemoteException
    {
        chatList.add(msg);
        return this.chatList;
    }

    @Override public ArrayList<String> getList() throws RemoteException
    {

        return this.chatList;
    }
}
