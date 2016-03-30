/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_customer;

import communication.Message;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import remote_customer.interfaces.CustomerStoreInterface;

/**
 * ClientCustomer.
 * @author luis
 */
public class ClientCustomer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        int ID, CustomerStoreInterface storeMonitor
        */
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
        
        String storeUrl;
        int portNumbStore;
        int customerSize;
        
        //final int portNumbStore = 22274;
        
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

            outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_CUSTOMER);
            con.writeObject(outMessage);
            inMessage = (Message) con.readObject();
            if (inMessage.getType() != Message.GETCONFIGURATIONPARAMETERS_CUSTOMER_RESULT && inMessage.getType() != Message.GETCONFIGURATIONPARAMETERS_NOTREADY) {
                GenericIO.writelnString("Thread: Tipo inválido! teve: " + inMessage.getType() + " esperava " + Message.GETCONFIGURATIONPARAMETERS_CUSTOMER_RESULT);
                GenericIO.writelnString(inMessage.toString());
                System.exit(1);
            }
            if (inMessage.getType() == Message.GETCONFIGURATIONPARAMETERS_CUSTOMER_RESULT) {
                storeUrl = inMessage.getServerUrlStore();
                portNumbStore = inMessage.getPortNumbStore();
                customerSize = inMessage.getCustomerSize();
                con.close();
                break;
            }
            con.close();
        }
        
        CustomerThread[] customers = new CustomerThread[customerSize];
        
        for(int i = 0; i < customerSize; i++){
            customers[i] = new CustomerThread(i, (CustomerStoreInterface) new CustomerStoreStub(storeUrl, portNumbStore));
        }
        
        for(int i = 0; i < customerSize; i++){
            System.out.println("Client " + i + " is running . . .");
            customers[i].start();
        }
        
        System.out.println("Clients are running . . .");
        
        for(int i = 0; i < customerSize; i++){
            try {
                customers[i].join();
            } catch (InterruptedException ex) {
            }
        }
    }
    
}
