package controller;

import bean.CRServer;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Cliente;

public class ClienteController {

    private CRServer stub;

    public ClienteController() throws ClassNotFoundException, SQLException {
        String host = JOptionPane.showInputDialog(null, "Digite o IP do servidor", "localhost");
        try {
            Registry registro = LocateRegistry.getRegistry(host);
            try {
                stub = (CRServer) registro.lookup("RMIServer");
            } catch (NotBoundException | AccessException ex) {
                Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salvarOuAtualizar(Integer id, String nome, String email, String celular) throws SQLException {
        Cliente cliente = new Cliente(id, nome, email, celular);
        try {
            stub.criarOuAlterarCliente(cliente);
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Integer id) throws SQLException {
        try {
            stub.deletarCliente(id);
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List listar(String nome, String email, String celular) {
        List<Cliente> lista = new ArrayList<>();
        try {
            lista = stub.listarClientes(nome, email, celular);
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public List listar() {
        return this.listar("", "", "");
    }

    public Cliente buscarPorId(Integer id) {
        Cliente c = new Cliente();
        try {
            c = (Cliente) stub.buscarClienteById(id);
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }
}
