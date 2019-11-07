package bean;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CRClient extends Remote {

    void receiveMessage(String message) throws RemoteException;
}
