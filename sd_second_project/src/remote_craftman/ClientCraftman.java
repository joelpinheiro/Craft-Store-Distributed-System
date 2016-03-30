/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_craftman;

import communication.Message;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import remote_craftman.interfaces.CraftsmanWorkshopInterface;

/**
 * ClientCraftman.
 * @author joelpinheiro
 */
public class ClientCraftman {

    private static int howManyCraftsmanThreads;
    private static int productStep;
    private static int amountToCollectPrimeMaterialsCraftsman;
    private static int elapsedTime;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // TODO: comes from args
        String mainServerUrl = "localhost";
        int mainServerPort = 22279;
        if(args.length == 1){
            mainServerUrl = args[0];
            System.out.println("Main Hub configured: " + mainServerUrl + ":" + mainServerPort);
        }
        else{
            System.out.println("Main Hub default configuration used: " + mainServerUrl + ":" + mainServerPort);
        }
        
        int portNumbWorkshop;
        String serverUrlWorkshop;


//        howManyCraftsmanThreads = 3;
//        productStep = 1;
//        amountToCollectPrimeMaterialsCraftsman = 1;
//        elapsedTime = 5;
        
        
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

            outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_CRAFTSMAN);
            con.writeObject(outMessage);
            inMessage = (Message) con.readObject();
            if (inMessage.getType() != Message.GETCONFIGURATIONPARAMETERS_CRAFTSMAN_RESULT && inMessage.getType() != Message.GETCONFIGURATIONPARAMETERS_NOTREADY) {
                GenericIO.writelnString("Thread: Tipo inválido! teve: " + inMessage.getType() + " esperava " + Message.GETCONFIGURATIONPARAMETERS_CRAFTSMAN_RESULT);
                GenericIO.writelnString(inMessage.toString());
                System.exit(1);
            }
            if (inMessage.getType() == Message.GETCONFIGURATIONPARAMETERS_CRAFTSMAN_RESULT) {
                howManyCraftsmanThreads = inMessage.getCraftsmanSize();
                productStep = inMessage.getCraftsmanProductStep_conf();
                amountToCollectPrimeMaterialsCraftsman = inMessage.getCraftsmanAmountToCollectPrimeMaterials_conf();
                elapsedTime = inMessage.getCraftsmanElapsedTime_conf();
                serverUrlWorkshop = inMessage.getServerUrlWorkshop();
                portNumbWorkshop = inMessage.getPortNumbWorkshop();
                con.close();
                break;
            }
            con.close();
        }
        
        

        CraftsmanThread[] craftsman = new CraftsmanThread[howManyCraftsmanThreads];

        for (int i = 0; i < howManyCraftsmanThreads; i++) {
            craftsman[i] = new CraftsmanThread(i,
                    productStep,
                    amountToCollectPrimeMaterialsCraftsman,
                    (CraftsmanWorkshopInterface) new CraftmanWorkshopStub(serverUrlWorkshop, portNumbWorkshop),
                    elapsedTime);

            System.out.println("Craftsman " + i + " is running . . .");
            craftsman[i].start();
        }
        
        System.out.println("Craftsman are running . . .");
        
        for(int i = 0; i < howManyCraftsmanThreads; i++){
            try {
                craftsman[i].join();
            } catch (InterruptedException ex) {
            }
        }
    }
}
