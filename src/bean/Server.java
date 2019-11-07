package bean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class Server implements CRServer {
    private static final long serialVersionUID = 1L;
    private String clientName [] = {"aluno1","aluno2","aluno3","aluno4","aluno5"};
    private String clientPass [] = {"12345","123456","12345","123456","12345"};
    private ArrayList<CRClient> clientList;

    public Server() throws RemoteException {
        clientList = new ArrayList<>();
    }
    
    @Override
    public synchronized boolean checkClientCredintials(String clientHost,
            String name, String pass) throws RemoteException {
        boolean checkLog = false;
        /**
         * Acessa a lista de usuários autorizados para validar a credencial
         * de acesso
         */
        for(int i = 0; i < clientName.length; i++) {
            String credential = clientName[i];
            String password = clientPass[i];
            if (credential.equals(name) && password.equals(pass)) {
                try {
                    checkLog = true;
                    Registry registro = LocateRegistry.getRegistry(clientHost,1099);
                    CRClient clientLookup = (CRClient) registro.lookup(name);
                    clientList.add(clientLookup);
                } catch (NotBoundException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                } catch (AccessException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
        return checkLog;
    }

    @Override
    public synchronized void broadcastMessage(String name, String message) 
            throws RemoteException {
        /**
         * Envia mensagem para toda a lista de clientes conectados
         */
        for(int i = 0; i < clientList.size(); i++) {
            CRClient client = clientList.get(i);
            client.receiveMessage(String.format("%s : %s", 
                    name.toUpperCase(), message));
        }
    }
    
    public static void main(String[] args) throws UnknownHostException {
        try {
            // Cria um objeto remoto para ser adicionado ao Registro
            CRServer server = (CRServer) UnicastRemoteObject.exportObject(new Server(), 0);
            // Cria um Registry na porta 1099
            Registry registro = LocateRegistry.createRegistry(12345);
            // Adiciona o objeto remoto ao registro.
            registro.rebind("RMIServer", server);
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.printf("Server iniciado no IP: %s Porta: 12345\n", ip);
        } catch (RemoteException ex) {
            System.out.println("!Erro no servidor: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public List<Client> listarClientes() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Cliente buscarClienteById() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterarDadosDoCliente(Cliente c) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cadastrarNovoCliente(Cliente c) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
