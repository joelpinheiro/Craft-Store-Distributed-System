/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package craftsman.thread;

import genclass.GenericIO;
import interfaces.ConfigurationInterface;
import interfaces.CraftsmanWorkshopInterface;
import interfaces.WorkshopRemoteInterfacesWrapper;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author luis
 */
public class ClientCraftsman {

    public ClientCraftsman(String host) {
        String[] array = {host};
        ClientCraftsman.main(array);
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
        String nameEntryWorkshopMonitor = "workshopMonitor";
        String nameEntryConfManager = "confManager";
        CraftsmanWorkshopInterface craftsmanInter = null;             // interface da barbearia (objecto remoto)
        ConfigurationInterface confInter = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            craftsmanInter = (WorkshopRemoteInterfacesWrapper) registry.lookup(nameEntryWorkshopMonitor);
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

        int nCraftsman = 0;
        int nCustomers = 0;
        try {
            nCraftsman = confInter.getAmountCraftsman();
            nCustomers = confInter.getAmountCustomers();
        } catch (RemoteException ex) {
            GenericIO.writelnString("Erro a ler configuracoes: " + ex.getMessage() + "!");
            ex.printStackTrace();
            System.exit(1);
        }
        CraftsmanThread[] craftsman = new CraftsmanThread[nCraftsman];      // array de threads cliente

        // TODO: Deve vir do configurador.
        int[] vectorClock = null;
        try {
            vectorClock = new int[1 + confInter.getAmountCraftsman() + confInter.getAmountCustomers()];
        } catch (RemoteException ex) {
            GenericIO.writelnString("Erro a ler configuracoes: " + ex.getMessage() + "!");
            ex.printStackTrace();
            System.exit(1);
        }
        int vectorClockPosition;
        
        for (int i = 0; i < nCraftsman;  i++) {
            try {
                vectorClockPosition = 1 + nCustomers + i;
                craftsman[i] = new CraftsmanThread(i, confInter.getAmountProductsManufacturedPerPrimeMaterial(), confInter.getAmountPrimeMaterialsTakenToTable(),
                        craftsmanInter, confInter.getElapsedTime(), vectorClock, vectorClockPosition);
            } catch (RemoteException ex) {
                GenericIO.writelnString("A thread craftsman deu erro na instanciacao: " + ex.getMessage() + "!");
                ex.printStackTrace();
                System.exit(1);
            }
        }

        for (int i = 0;
                i < nCraftsman;
                i++) {
            craftsman[i].start();
        }

        /* aguardar o fim da simulação */
        GenericIO.writelnString();
        for (int i = 0;
                i < nCraftsman;
                i++) {
            try {
                craftsman[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("O craftsman " + i + " terminou.");
        }

        GenericIO.writelnString();
    }
}
