package test.rmi.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Kevin on 06.05.17.
 */
public interface Chat extends Remote
{
    ArrayList<String> sendMsg(String msg) throws RemoteException;

    ArrayList<String> getList() throws RemoteException;
}
