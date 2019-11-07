package bean;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Cliente;

public interface CRServer extends Remote {
    boolean checkClientCredintials(String clientHost, String clientName, String clientPass) throws RemoteException;
    void broadcastMessage(String name,String message) throws RemoteException;
    List<Client> listarClientes() throws RemoteException;
    Cliente buscarClienteById() throws RemoteException;
    void alterarDadosDoCliente(Cliente c) throws RemoteException;
    void cadastrarNovoCliente(Cliente c) throws RemoteException;
}
