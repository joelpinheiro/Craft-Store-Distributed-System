/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log.monitor;

import genclass.GenericIO;
import interfaces.ConfigurationInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import interfaces.LogRemoteInterfacesWrapper;
import interfaces.Register;
import java.rmi.NotBoundException;

/**
 *
 * @author luis
 */
public class ServerLogMonitor {

    public ServerLogMonitor(String host) {
        String[] array = {host};
        ServerLogMonitor.main(array);
    }

    public static void main(String[] args) {
        /* obtenção da localização do serviço de registo RMI */

        String rmiRegHostName;                          // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                             // port de escuta do serviço
        int logMonitorPort = 22271;                     // change this
        String nameEntryLogMonitor = "logMonitor";
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
        LogMonitor lMonitor = null;                              // barbearia (representa o objecto remoto)
        LogRemoteInterfacesWrapper logRemoteInter = null;
        ConfigurationInterface confInter = null;
        Register reg = null;
        
        /* seu registo no serviço de registo RMI */
        Registry registry = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            confInter = (ConfigurationInterface) registry.lookup(nameEntryConfManager);
            reg = (Register) registry.lookup(nameEntryRegisterHandler);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na criação do registo RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException ex) {
            GenericIO.writelnString("Excepção na criação do registo RMI: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O registo RMI foi criado!");

        try {
            lMonitor = new LogMonitor(confInter.getAmountCustomers(), confInter.getStockPrimeMaterials(),
                    confInter.getAmountCraftsman(), confInter.getFileName(), confInter.getFilePath(),
                    3);
        } catch (RemoteException ex) {
            GenericIO.writelnString("Excepção na criação do LogMonitor: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
        try {
            // Object tmpMonitor = UnicastRemoteObject.exportObject(lMonitor, logMonitorPort);
            logRemoteInter = (LogRemoteInterfacesWrapper) UnicastRemoteObject.exportObject(lMonitor, logMonitorPort);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na geração do stub para o LogMonitor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O stub para o LogMonitor foi gerado!");

        try {
            reg.bind(nameEntryLogMonitor, logRemoteInter);
            //registry.bind(nameEntryLogMonitor, logRemoteInter);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção no registry bind do LogMonitor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("O LogMonitor já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O LogMonitor foi registado!");
    }
}
