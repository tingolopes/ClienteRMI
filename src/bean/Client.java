package bean;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client implements Serializable, CRClient, Runnable {

    private static final long serialVersionUID = 1L;
    private CRServer server;
    private String clientName;
    private String clientPass;
    boolean checkExit = true;
    boolean checkLog = false;

    public Client(CRServer chatinterface, String clientname, String password) throws RemoteException {
        this.server = chatinterface;
        this.clientName = clientname;
        this.clientPass = password;

    }

    public void checkCredential() throws RemoteException, UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        /**
         * verifica se o objeto da classe cliente tem autorizaçao de acesso ao
         * servidor do chat
         */
        checkLog = server.checkClientCredintials(ip, clientName, clientPass);
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public void run() {
        // verifica se está logado
        if (checkLog) {
            System.out.println("Conexão com o RMI Server bem sucedida!");
            System.out.println("NOTE : Digite LOGOUT para sair do serviço");
            System.out.println("Agora você já pode iniciar sua conversa no Chat\n");
            Scanner scanner = new Scanner(System.in);
            String message;

            while (checkExit) {
                message = scanner.nextLine();
                if (message.equals("LOGOUT")) {
                    checkExit = false;
                } else {
                    try {
                        server.broadcastMessage(clientName, message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("\nLogout do RMI Chat Program"
                    + " realizado com sucesso!\n"
                    + "Obrigado por utilizá-lo...\n"
                    + "Desenvolvido por Nifal Nizar\n"
                    + "Adaptado por Nicholas Eduardo Lopes dos Santos");
        } else {
            System.out.println("\nNome ou senha de usuário incorreta...");
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, UnknownHostException {
        Scanner s = new Scanner(System.in);
        String clientName = "", clientPass = "", serverName = "";
        int port = 12345;

        System.out.println("\n~~ Bem-vindo ao RMI Chat Program ~~\n");
        System.out.print("Digite o nome do servidor: ");
        serverName = s.nextLine();
        System.out.print("Digite seu usuário: ");
        clientName = s.nextLine();
        System.out.print("Digite sua senha: ");
        clientPass = s.nextLine();

        System.out.println("\nConectando ao RMI Server...\n");

        Registry registro = LocateRegistry.getRegistry(serverName, port);
        CRServer crServer = (CRServer) registro.lookup("RMIServer");

        System.out.println("\nCriando o registro do cliente");

        Client client = new Client(crServer, clientName, clientPass);
        // cria a identificação do objeto remoto do cliente
        CRClient crClient = (CRClient) UnicastRemoteObject.exportObject(client, 0);

        Registry registroCliente = LocateRegistry.createRegistry(1099);
        // adiciona o objeto remoto ao registro
        registroCliente.rebind(clientName, crClient);
        client.checkCredential();

        new Thread(client).run();
    }
}
