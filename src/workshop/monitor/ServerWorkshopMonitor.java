/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop.monitor;

import genclass.GenericIO;
import interfaces.ConfigurationInterface;
import interfaces.LogRemoteInterfacesWrapper;
import interfaces.Register;
import interfaces.StoreRemoteInterfacesWrapper;
import interfaces.WorkshopLogInterface;
import interfaces.WorkshopRemoteInterfacesWrapper;
import interfaces.WorkshopStoreInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author luis
 */
public class ServerWorkshopMonitor {

    /**
     * Programa principal.
     *
     * @param args
     * @throws java.rmi.RemoteException
     */

    public ServerWorkshopMonitor(String machineUrl) {
        try {
            String[] array = {machineUrl};
            ServerWorkshopMonitor.main(array);
        } catch (RemoteException ex) {
            GenericIO.writelnString("Excepção na instanciacao do StoreMonitor: " + ex.getMessage() + "!");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) throws RemoteException {
        /* obtenção da localização do serviço de registo RMI */

        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço
        int workshopMonitorPort = 22274;             // change this
        String nameEntryLogMonitor = "logMonitor";                  // name used on dns lookup for remote objects
        String nameEntryStoreMonitor = "storeMonitor";     // name used on dns lookup for remote objects
        String nameEntryWorkshopMonitor = "workshopMonitor";
        String nameEntryConfManager = "confManager";
        String nameEntryRegisterHandler = "RegisterHandler";

        if (!args[0].equals("")) {
            rmiRegHostName = args[0];
        } else {
            GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo? ");
            rmiRegHostName = GenericIO.readlnString();
        }
        rmiRegPortNumb = 22270;

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa a barbearia e geração de um stub para ele */
        WorkshopMonitor wMonitor = null;                                               // barbearia (representa o objecto remoto)
        WorkshopRemoteInterfacesWrapper WorkshopInter = null;                             // interface da barbearia

        // e preciso retornar o objeto remoto, neste caso o log (client mode)
        WorkshopLogInterface workshopLogInter = null;
        WorkshopStoreInterface workshopStoreInter = null;
        ConfigurationInterface confInter = null;
        Register reg = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            workshopLogInter = (LogRemoteInterfacesWrapper) registry.lookup(nameEntryLogMonitor);
            workshopStoreInter = (StoreRemoteInterfacesWrapper) registry.lookup(nameEntryStoreMonitor);
            confInter = (ConfigurationInterface) registry.lookup(nameEntryConfManager);
            reg = (Register) registry.lookup(nameEntryRegisterHandler);
            GenericIO.writelnString("Cast feito com sucesso!");
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do StoreMonitor ou LogMonitor: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("O StoreMonitor ou LogMonitor nao estao registados: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        int[] vectorClock = null;
        try {
            vectorClock = new int[1 + confInter.getAmountCraftsman() + confInter.getAmountCustomers()];
        } catch (RemoteException ex) {
            GenericIO.writelnString("Erro a ler configuracoes: " + ex.getMessage() + "!");
            ex.printStackTrace();
            System.exit(1);
        }
        
        wMonitor = new WorkshopMonitor(confInter.getCraftsmanThreshold(), workshopLogInter, workshopStoreInter, confInter.getAmountCraftsman(), vectorClock);
        try {
            WorkshopInter = (WorkshopRemoteInterfacesWrapper) UnicastRemoteObject.exportObject(wMonitor, workshopMonitorPort);    // alterar a porta de acordo com o segundo trabalho
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na geração do stub para a WorkshopMonitor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O stub para a WorkshopMonitor foi gerado!");

        /* seu registo no serviço de registo RMI */
        Registry registry = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na criação do registo RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O registo RMI foi criado!");

        try {
            reg.bind(nameEntryWorkshopMonitor, WorkshopInter);
            //registry.bind(nameEntryWorkshopMonitor, WorkshopInter);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção no registry bind da WorkshopMonitor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("A WorkshopMonitor já está registada: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("A WorkshopMonitor foi registada!");
    }
}
