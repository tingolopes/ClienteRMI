package bean;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Cliente;

public interface CRServer extends Remote {
    boolean checkClientCredintials(String clientHost, String clientName, String clientPass) throws RemoteException;
    void broadcastMessage(String name,String message) throws RemoteException;
    List<Cliente> listarClientes(String nome, String email, String celular) throws RemoteException;
    Cliente buscarClienteById(Integer id) throws RemoteException;
    void criarOuAlterarCliente(Cliente c) throws RemoteException;
    void deletarCliente(Integer id) throws RemoteException;
}
