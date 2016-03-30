/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_entrepreneur;

import remote_entrepreneur.interfaces.EntrepreneurStorageInterface;
import communication.*;
import genclass.GenericIO;
import static java.lang.Thread.sleep;

/**
 * EntrepreneurStorageStub.
 * @author joelpinheiro
 */
public class EntrepreneurStorageStub implements EntrepreneurStorageInterface {

    private int portNum;
    private String serverUrl;

    EntrepreneurStorageStub(String serverUrl, int portNumbStorage) {
        this.portNum = portNumbStorage;
        this.serverUrl = serverUrl;
    }

    @Override
    public int getStock() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int removePrimeMaterial(int numberOfReplenish) {
        
        ClientCom con = new ClientCom(this.serverUrl, this.portNum);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.REMOVEPRIMEMATERIALSTORAGE, numberOfReplenish, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.PRIMEMATERIALSTORAGEREPLENISHAMOUNT)
           { 
               GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.PRIMEMATERIALSTORAGEREPLENISHAMOUNT);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();

        System.out.println("Removed prime material: " + inMessage.getRemovedPrimeMaterialsFromStorage());
        return inMessage.getRemovedPrimeMaterialsFromStorage();
    }
    
    private String getState() {
        return ((EntrepreneurThread)Thread.currentThread()).getCurrentState();
    }
}
