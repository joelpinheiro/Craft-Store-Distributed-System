/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.thread;

import genclass.GenericIO;
import interfaces.ConfigurationInterface;
import interfaces.CustomerStoreInterface;
import interfaces.StoreRemoteInterfacesWrapper;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author luis
 */
public class ClientCustomers {

    public ClientCustomers(String host) {
        String[] array = {host};
        ClientCustomers.main(array);
    }

    public static void main(String[] args) {
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
        String nameEntryConfManager = "confManager";
        CustomerStoreInterface customerInter = null;             // interface da barbearia (objecto remoto)
        ConfigurationInterface confInter = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            customerInter = (StoreRemoteInterfacesWrapper) registry.lookup(nameEntryStoreMonitor);
            confInter = (ConfigurationInterface) registry.lookup(nameEntryConfManager);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do StoreMonitor: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("A StoreMonitor não está registada: " + e.getMessage() + "!");
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
        int vectorClockPosition;
        
        int nCustomer = 0;
        try {
            nCustomer = confInter.getAmountCustomers();
        } catch (RemoteException ex) {
            GenericIO.writelnString("Erro a ler configuracoes: " + ex.getMessage() + "!");
            ex.printStackTrace();
            System.exit(1);
        }
        CustomerThread[] customer = new CustomerThread[nCustomer];      // array de threads cliente

        for (int i = 0; i < nCustomer; i++) {
            vectorClockPosition = i + 1;
            customer[i] = new CustomerThread(i, customerInter, vectorClock, vectorClockPosition);
        }

        for (int i = 0; i < nCustomer; i++) {
            customer[i].start();
        }

        /* aguardar o fim da simulação */
        GenericIO.writelnString();
        for (int i = 0; i < nCustomer; i++) {
            try {
                customer[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("O cliente " + i + " terminou.");
        }
        GenericIO.writelnString();
    }
}
