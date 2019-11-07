package bean;

import connection.Conexao;
import controller.ClienteController;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cliente;
import pattern.dao.ClienteDao;

public class Server implements CRServer {

    private static final long serialVersionUID = 1L;
    private String clientName[] = {"aluno1", "aluno2", "aluno3", "aluno4", "aluno5"};
    private String clientPass[] = {"12345", "123456", "12345", "123456", "12345"};
    private ArrayList<CRClient> clientList;
    private ClienteController cr;

    private ClienteDao dao;

    public Server() throws RemoteException {
        try {
            this.dao = new ClienteDao(Conexao.getInstancia());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientList = new ArrayList<>();
    }

    @Override
    public synchronized boolean checkClientCredintials(String clientHost,
            String name, String pass) throws RemoteException {
        boolean checkLog = false;
        /**
         * Acessa a lista de usuários autorizados para validar a credencial de
         * acesso
         */
        for (int i = 0; i < clientName.length; i++) {
            String credential = clientName[i];
            String password = clientPass[i];
            if (credential.equals(name) && password.equals(pass)) {
                try {
                    checkLog = true;
                    Registry registro = LocateRegistry.getRegistry(clientHost, 1099);
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
        for (int i = 0; i < clientList.size(); i++) {
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
            LocateRegistry.createRegistry(1099);
            Registry registro = LocateRegistry.getRegistry();
            // Adiciona o objeto remoto ao registro.
            registro.rebind("RMIServer", server);
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.printf("Server iniciado no IP: %s Porta: 1099\n", ip);
        } catch (RemoteException ex) {
            System.out.println("!Erro no servidor: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public List<Cliente> listarClientes(String nome, String email, String celular) throws RemoteException {
        List<String> criterio = new ArrayList<>();
        if (nome != null && !"".equals(nome)) {
            criterio.add("UPPER(nome) LIKE UPPER('%" + nome + "%')");
        }
        if (email != null && !"".equals(email)) {
            criterio.add("UPPER(email) LIKE UPPER('%" + email + "%')");
        }
        if (celular != null && !"".equals(celular)) {
            criterio.add("celular LIKE '%" + celular + "%'");
        }
        String criteria = "";
        if (!criterio.isEmpty()) {
            for (int i = 0; i < criterio.size(); i++) {
                if (i > 0) {
                    criteria += " AND ";
                }
                criteria += criterio.get(i);
            }
        }
        return (List<Cliente>) dao.list(criteria);
    }

    @Override
    public Cliente buscarClienteById(Integer id) throws RemoteException {
        System.out.println("Buscando o cliente: "+dao.findById(id).toString());
        return (Cliente)dao.findById(id);
    }

    @Override
    public void criarOuAlterarCliente(Cliente c) throws RemoteException {
        System.out.println("Criando o cliente: "+c.toString());
        try {
            dao.saveOrUpdate(c);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deletarCliente(Integer id) throws RemoteException {
        System.out.println("Deletando o cliente: "+dao.findById(id).toString());
        try {
            dao.delete(dao.findById(id));
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
