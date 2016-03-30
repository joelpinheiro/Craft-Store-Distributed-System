/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainProgram;

import genclass.GenericIO;
import communication.Message;
import communication.MessageException;
/**
 *
 * @author luis
 */
class MainServerProxy extends Thread{
    /**
   *  Contador de threads lançados
   *
   *    @serialField nProxy
   */
   private static int nProxy;

  /**
   *  Canal de comunicação
   *
   *    @serialField sconi
   */
   private mainProgram.ServerCom sconi;
    
    public MainServerProxy(ServerCom socket) {
        super ("Proxy_" + getProxyId ());
        
        this.sconi = socket;
    }
    
    @Override
    public void run() {
        Message inMessage = null,                       // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (Message) sconi.readObject();                                   // ler pedido do cliente
        try {
            outMessage = processAndReply(inMessage);               // processá-lo
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                // enviar resposta ao cliente
        sconi.close();                                                // fechar canal de comunicação
    }

    /**
     * Geração do identificador da instanciação.
     *
     * @return return result identificador da instanciação
     */
    private static int getProxyId() {
        Class<mainProgram.MainServerProxy> cl = null;             // representação do tipo de dados StorageProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = (Class<MainServerProxy>) Class.forName("mainProgram.MainServerProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("O tipo de dados MainServerProxy não foi encontrado!");
            System.exit(1);
        }

        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }

    private Message processAndReply(Message inMessage) throws MessageException{
        Message outMessage = null;

        if(MainProgram.getPortNumbStorage() == -1 || MainProgram.getPortNumbStore() == -1 || MainProgram.getPortNumbLog() == -1 || 
                MainProgram.getPortNumbWorkshop() == -1 || MainProgram.getServerUrlLog() == null || MainProgram.getServerUrlStorage() == null || 
                MainProgram.getServerUrlStore() == null || MainProgram.getServerUrlWorkshop() == null){
            MainProgram.setAllConfParametersReady(false);
        }
        else{
            MainProgram.setAllConfParametersReady(true);
            System.out.println();
        }
        
        switch (inMessage.getType()) {
            case Message.GETCONFIGURATIONPARAMETERS_ENTREPRENEUR:
                if(MainProgram.allConfParametersCollected()){
                    int amountProductsToPickUp = MainProgram.getAmountProductsToPickUp();
                    int amountOfReplenishWorkshop = MainProgram.getAmountOfReplenishWorkshop();;

                    int portNumbStorage = MainProgram.getPortNumbStorage();
                    String serverUrlStorage = MainProgram.getServerUrlStorage();

                    int portNumbStore = MainProgram.getPortNumbStore();
                    String serverUrlStore = MainProgram.getServerUrlStore();

                    int portNumbWorkshop = MainProgram.getPortNumbWorkshop();
                    String serverUrlWorkshop = MainProgram.getServerUrlWorkshop();
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_ENTREPRENEUR_RESULT, amountProductsToPickUp, amountOfReplenishWorkshop,
                            portNumbStorage, serverUrlStorage, portNumbStore, serverUrlStore, portNumbWorkshop, serverUrlWorkshop);            // gerar confirmação
                    System.out.println("Entrepreneur converged successfuly, vector dispatched");
                }
                else{
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_NOTREADY);
                    System.out.println("Entrepreneur requests sync vectors to attempt convergence but fails, not enough data yet");
                }
                break;
                
            case Message.GETCONFIGURATIONPARAMETERS_CUSTOMER:
                if(MainProgram.allConfParametersCollected()){
                    int portNumbStore = MainProgram.getPortNumbStore();
                    String serverUrlStore = MainProgram.getServerUrlStore();
                    int customerSize = MainProgram.getHowManyCustomers();
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_CUSTOMER_RESULT, portNumbStore, customerSize, serverUrlStore);            // gerar confirmação
                    System.out.println("Customer converged successfuly, vector dispatched");
                }
                else{
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_NOTREADY);
                    System.out.println("Customer requests sync vectors to attempt convergence but fails, not enough data yet");
                }
                break;
                
            case Message.GETCONFIGURATIONPARAMETERS_CRAFTSMAN:
                if(MainProgram.allConfParametersCollected()){
                    int portNumbWorkshop = MainProgram.getPortNumbWorkshop();
                    String serverUrlWorkshop = MainProgram.getServerUrlWorkshop();
                    int howManyCraftsman = MainProgram.getHowManyCraftsmanThreads();
                    int productStep = MainProgram.getProductStep();
                    int ammountToCollectPrimeMaterials = MainProgram.getAmountToCollectPrimeMaterialsCraftsman();
                    int elapsedTime = MainProgram.getElapsedTime();
                    
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_CRAFTSMAN_RESULT, portNumbWorkshop, howManyCraftsman, 
                            productStep, ammountToCollectPrimeMaterials, elapsedTime, serverUrlWorkshop);            // gerar confirmação
                    System.out.println("Craftman converged successfuly, vector dispatched");
                }
                else{
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_NOTREADY);
                    System.out.println("Craftsman requests sync vectors to attempt convergence but fails, not enough data yet");
                }
                break;
            
            case Message.SUBMITCONFIGURATION_LOG:
                MainProgram.setPortNumbLog(inMessage.getPortNumbLog());
                MainProgram.setServerUrlLog(inMessage.getServerUrlLog());
                outMessage = new Message(Message.ACK);
                System.out.println("Log Monitor concluded the synchronization sequence.");
                break;
                
            case Message.GETCONFIGURATIONPARAMETERS_LOG:
                if(MainProgram.allConfParametersCollected()){
                    int numberOfCustomers = MainProgram.getHowManyCustomers();
                    int storageStock = MainProgram.getStockPrimeMaterials();
                    int numberOfCraftmen = MainProgram.getHowManyCraftsmanThreads();
                    String fileName = MainProgram.getLogFileName();
                    String filePath = MainProgram.getLogFilePath();
                    int maxSpaceToIntegers = inMessage.getCharacterSpacing();
                    
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_LOG_RESULT, numberOfCustomers, storageStock, 
                            numberOfCraftmen, maxSpaceToIntegers, fileName, filePath);            // gerar confirmação
                    System.out.println("Log Monitor converged successfuly, vector dispatched");
                }
                else{
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_NOTREADY);
                    System.out.println("Log requests sync vectors to attempt convergence but fails, not enough data yet");
                }
                break;
                
            case Message.SUBMITCONFIGURATION_STORAGE:
                MainProgram.setPortNumbStorage(inMessage.getPortNumbStorage());
                MainProgram.setServerUrlStorage(inMessage.getServerUrlStorage());
                outMessage = new Message(Message.ACK);
                System.out.println("Storage Monitor concluded the synchronization sequence.");
                break;
                
            case Message.GETCONFIGURATIONPARAMETERS_STORAGE:
                if(MainProgram.allConfParametersCollected()){
                    int portNumbLog = MainProgram.getPortNumbLog();
                    String serverUrlLog = MainProgram.getServerUrlLog();
                    int stockPrimeMaterials = MainProgram.getStockPrimeMaterials();
                    
                    System.out.println(stockPrimeMaterials);
                    
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_STORAGE_RESULT, stockPrimeMaterials, portNumbLog, 
                            serverUrlLog);            // gerar confirmação
                    System.out.println("Storage monitor converged successfuly, vector dispatched");
                }
                else{
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_NOTREADY);
                    System.out.println("Storage requests sync vectors to attempt convergence but fails, not enough data yet");
                }
                break;
                
            case Message.SUBMITCONFIGURATION_STORE:
                MainProgram.setPortNumbStore(inMessage.getPortNumbStore());
                MainProgram.setServerUrlStore(inMessage.getServerUrlStore());
                outMessage = new Message(Message.ACK);
                System.out.println("Store Monitor concluded the synchronization sequence.");
                break;
                
            case Message.GETCONFIGURATIONPARAMETERS_STORE:
                if(MainProgram.allConfParametersCollected()){
                    int portNumbLog = MainProgram.getPortNumbLog();
                    String serverUrlLog = MainProgram.getServerUrlLog();
                    int customerSize = MainProgram.getHowManyCustomers();
                    
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_STORE_RESULT, customerSize, portNumbLog, 
                            serverUrlLog);            // gerar confirmação
                    System.out.println("Store monitor converged successfuly, vector dispatched");
                }
                else{
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_NOTREADY);
                    System.out.println("Store requests sync vectors to attempt convergence but fails, not enough data yet");
                }
                break;
                
            case Message.SUBMITCONFIGURATION_WORKSHOP:
                MainProgram.setServerUrlWorkshop(inMessage.getServerUrlWorkshop());
                MainProgram.setPortNumbWorkshop(inMessage.getPortNumbWorkshop());
                outMessage = new Message(Message.ACK);
                System.out.println("Workshop Monitor concluded the synchronization sequence.");
                break;
                
            case Message.GETCONFIGURATIONPARAMETERS_WORKSHOP:
                if(MainProgram.allConfParametersCollected()){
                    int portNumbLog = MainProgram.getPortNumbLog();
                    String serverUrlLog = MainProgram.getServerUrlLog();
                    int portNumbStore = MainProgram.getPortNumbStore();
                    String serverUrlStore = MainProgram.getServerUrlStore();
                    int productThreshold = MainProgram.getProductThreshold();
                    int craftmanSize = MainProgram.getHowManyCraftsmanThreads();
                    
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_WORKSHOP_RESULT, portNumbLog, portNumbStore, 
                            productThreshold, craftmanSize, serverUrlLog, serverUrlStore);            // gerar confirmação
                    System.out.println("Workshop monitor converged successfuly, vector dispatched");
                }
                else{
                    outMessage = new Message(Message.GETCONFIGURATIONPARAMETERS_NOTREADY);
                    System.out.println("Workshop requests sync vectors to attempt convergence but fails, not enough data yet");
                }
                break;
        }
        return outMessage; 
    }
}
