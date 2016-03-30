/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_entrepreneur;

import communication.Message;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import remote_entrepreneur.interfaces.EntrepreneurStoreInterface;
import remote_entrepreneur.interfaces.EntrepreneurStorageInterface;
import remote_entrepreneur.interfaces.EntrepreneurWorkshopInterface;


/**
 * ClientEntrepreneur.
 * @author joelpinheiro
 */
public class ClientEntrepreneur {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO: this comes from args
        String mainServerUrl = "localhost";
        int mainServerPort = 22279;
        if(args.length == 1){
            mainServerUrl = args[0];
            System.out.println("Main Hub configured: " + mainServerUrl + ":" + mainServerPort);
        }
        else{
            System.out.println("Main Hub default configuration used: " + mainServerUrl + ":" + mainServerPort);
        }

        int amountProductsToPickUp;
        int amountOfReplenishWorkshop;

        int portNumbStorage;
        String serverUrlStorage;

        int portNumbStore;
        String serverUrlStore ;

        int portNumbWorkshop;
        String serverUrlWorkshop;

        while(true){
            ClientCom con = new ClientCom(mainServerUrl, mainServerPort);
            Message inMessage, outMessage;

            while (!con.open()) // aguarda ligação
            {
                try {
                    sleep((long) (10));
                } catch (InterruptedException e) {
                }
            }

            outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_ENTREPRENEUR);
            con.writeObject(outMessage);
            inMessage = (Message) con.readObject();
            if (inMessage.getType() != Message.GETCONFIGURATIONPARAMETERS_ENTREPRENEUR_RESULT && inMessage.getType() != Message.GETCONFIGURATIONPARAMETERS_NOTREADY) {
                GenericIO.writelnString("Thread: Tipo inválido! teve: " + inMessage.getType() + " esperava " + Message.GETCONFIGURATIONPARAMETERS_ENTREPRENEUR_RESULT);
                GenericIO.writelnString(inMessage.toString());
                System.exit(1);
            }
            if (inMessage.getType() == Message.GETCONFIGURATIONPARAMETERS_ENTREPRENEUR_RESULT) {
                amountProductsToPickUp = inMessage.getAmoutProductsToPickUp_conf_entrepreneur();
                amountOfReplenishWorkshop = inMessage.getAmountOfReplenishWorkshop_conf_entrepreneur();

                portNumbStorage = inMessage.getPortNumbStorage();
                serverUrlStorage = inMessage.getServerUrlStorage();

                portNumbStore = inMessage.getPortNumbStore();
                serverUrlStore = inMessage.getServerUrlStore();

                portNumbWorkshop = inMessage.getPortNumbWorkshop();
                serverUrlWorkshop = inMessage.getServerUrlWorkshop();
                con.close();
                break;
            }
            con.close();
        }

        
//        int amountProductsToPickUp = 3;
//        int amountOfReplenishWorkshop = 3;
//        
//        final int portNumbStorage = 22271;
//        final String serverUrlStorage = "localhost";
//        
//        final int portNumbStore = 22274;
//        final String serverUrlStore = "localhost";
//        
//        final int portNumbWorkshop = 22273;
//        final String serverUrlWorkshop = "localhost";
        
        EntrepreneurStorageStub entrepreneurStorageStub;
        
        EntrepreneurStoreStub entrepreneurStoreStub;
        
        EntrepreneurWorkshopStub entrepreneurWorkshopStub;
        
        EntrepreneurThread entrepreneurThread;
        
        entrepreneurStorageStub = new EntrepreneurStorageStub(serverUrlStorage, portNumbStorage);
        entrepreneurStoreStub = new EntrepreneurStoreStub(serverUrlStore, portNumbStore);
        entrepreneurWorkshopStub = new EntrepreneurWorkshopStub(serverUrlWorkshop, portNumbWorkshop);
        
        entrepreneurThread = new EntrepreneurThread(0, amountProductsToPickUp, 
            amountOfReplenishWorkshop, 
            (EntrepreneurStoreInterface) entrepreneurStoreStub,
            (EntrepreneurWorkshopInterface) entrepreneurWorkshopStub,
            (EntrepreneurStorageInterface) entrepreneurStorageStub);
        
        System.out.println("Entrepreneur is running . . .");
        
        entrepreneurThread.start();
        
        try {
            entrepreneurThread.join();
        } catch (InterruptedException ex) {
        }
    }
}
