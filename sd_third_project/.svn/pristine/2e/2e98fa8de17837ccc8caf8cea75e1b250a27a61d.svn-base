/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage.monitor;

import genclass.GenericIO;
import interfaces.ConfigurationInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import interfaces.EntrepreneurStorageInterface;
import interfaces.LogRemoteInterfacesWrapper;
import interfaces.Register;
import interfaces.StorageLogInterface;

/**
 *
 * @author luis
 */
public class ServerStorageMonitor {

    /**
     * Programa principal.
     */
    public ServerStorageMonitor(String host) {
        String[] array = {host};
        ServerStorageMonitor.main(array);
    }

    public static void main(String[] args) {
        /* obtenção da localização do serviço de registo RMI */

        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço
        int storageMonitorPort = 22272;             // change this
        String nameEntryLogMonitor = "logMonitor";                  // name used on dns lookup for remote objects
        String nameEntryStorageMonitor = "storageMonitor";     // name used on dns lookup for remote objects
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
        StorageMonitor sMonitor = null;                              // barbearia (representa o objecto remoto)
        EntrepreneurStorageInterface entrepreneurStorageInter = null;             // interface da barbearia
        Register reg = null;

        // e preciso retornar o objeto remoto, neste caso o log (client mode)
        StorageLogInterface storageLogInter = null;
        ConfigurationInterface confInter = null;
        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            storageLogInter = (LogRemoteInterfacesWrapper) registry.lookup(nameEntryLogMonitor);
            confInter = (ConfigurationInterface) registry.lookup(nameEntryConfManager);
            reg = (Register) registry.lookup(nameEntryRegisterHandler);
            GenericIO.writelnString("Cast feito com sucesso!");
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do StorageMonitor: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("O StorageMonitor não está registado: " + e.getMessage() + "!");
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

        try {
            sMonitor = new StorageMonitor(confInter.getStockPrimeMaterials(), storageLogInter, vectorClock);
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
            System.exit(0);
        }
        try {
            entrepreneurStorageInter = (EntrepreneurStorageInterface) UnicastRemoteObject.exportObject(sMonitor, storageMonitorPort);    // alterar a porta de acordo com o segundo trabalho
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na geração do stub para a StorageMonitor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O stub para a StorageMonitor foi gerado!");

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
            reg.bind(nameEntryStorageMonitor, entrepreneurStorageInter);
            //registry.bind(nameEntryStorageMonitor, entrepreneurStorageInter);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção no registry bind da StorageMonitor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("A StorageMonitor já está registada: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("A StorageMonitor foi registada!");
    }
}
