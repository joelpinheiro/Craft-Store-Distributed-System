/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entrepreneur.thread;

import genclass.GenericIO;
import interfaces.ConfigurationInterface;
import interfaces.EntrepreneurStorageInterface;
import interfaces.EntrepreneurStoreInterface;
import interfaces.EntrepreneurWorkshopInterface;
import interfaces.StoreRemoteInterfacesWrapper;
import interfaces.WorkshopRemoteInterfacesWrapper;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author luis
 */
public class ClientEntrepreneur {

    public ClientEntrepreneur(String host) {
        String[] array = {host};
        ClientEntrepreneur.main(array);
    }

    public static void main(String[] args) {
        EntrepreneurThread entrepreneur = null;      // array de threads cliente
        String rmiRegHostName;                                // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                                   // port de escuta do serviço

        /* obtenção da localização do serviço de registo RMI */
        if (!args[0].equals("")) {
            rmiRegHostName = args[0];
        } else {
            GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo? ");
            rmiRegHostName = GenericIO.readlnString();
        }
        rmiRegPortNumb = 22270;

        /* localização por nome do objecto remoto no serviço de registos RMI */
        String nameEntryStoreMonitor = "storeMonitor";
        String nameEntryStorageMonitor = "storageMonitor";
        String nameEntryWorkshopMonitor = "workshopMonitor";
        String nameEntryConfManager = "confManager";
        EntrepreneurStoreInterface entrepreneurStoreInter = null;             // interface da barbearia (objecto remoto)
        EntrepreneurStorageInterface entrepreneurStorageInter = null;
        EntrepreneurWorkshopInterface entrepreneurWorkshopInter = null;
        ConfigurationInterface confInter = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            entrepreneurWorkshopInter = (WorkshopRemoteInterfacesWrapper) registry.lookup(nameEntryWorkshopMonitor);
            entrepreneurStorageInter = (EntrepreneurStorageInterface) registry.lookup(nameEntryStorageMonitor);
            entrepreneurStoreInter = (StoreRemoteInterfacesWrapper) registry.lookup(nameEntryStoreMonitor);
            confInter = (ConfigurationInterface) registry.lookup(nameEntryConfManager);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do WorkshopMonitor: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("A WorkshopMonitor não está registada: " + e.getMessage() + "!");
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
        int vectorClockPosition = 0;
        
        try {
            entrepreneur = new EntrepreneurThread(0, confInter.getAmountProductsToCarryToWorkshop(), confInter.getAmountPrimeMaterialsToReplenish(),
                    entrepreneurStoreInter, entrepreneurWorkshopInter, entrepreneurStorageInter, vectorClock, vectorClockPosition);
        } catch (RemoteException ex) {
            GenericIO.writelnString("Erro a ler configuracoes: " + ex.getMessage() + "!");
            ex.printStackTrace();
            System.exit(1);
        }

        entrepreneur.start();

        /* aguardar o fim da simulação */
        GenericIO.writelnString();

        try {
            entrepreneur.join();
        } catch (InterruptedException e) {
        }
        GenericIO.writelnString("A dona terminou.");
    }
}
