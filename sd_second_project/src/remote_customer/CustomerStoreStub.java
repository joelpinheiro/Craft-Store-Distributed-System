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
 * CustomerStoreStub.
 * @author luis
 */
public class CustomerStoreStub implements CustomerStoreInterface{
    private final int portNumbStore;
    private final String storeServerUrl;

    CustomerStoreStub(String serverUrl, int portNumbStore) {
        this.portNumbStore = portNumbStore;
        this.storeServerUrl = serverUrl;
    }

    @Override
    public void goShopping(int ID) {
        ClientCom con = new ClientCom(this.storeServerUrl, this.portNumbStore);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.GOSHOPING_CUSTOMER, ID, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK)
           { 
               GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
    }

    @Override
    public boolean isDoorOpen() {
        ClientCom con = new ClientCom(this.storeServerUrl, this.portNumbStore);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.ISDOOROPEN_CUSTOMER, getId(), getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.TRUE && inMessage.getType () != Message.FALSE)
           { 
               GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.FALSE + 
                    " ou " + Message.TRUE);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
        
        return inMessage.getType() == Message.TRUE;
    }

    @Override
    public void enterShopCustomer(int ID) {
        ClientCom con = new ClientCom(this.storeServerUrl, this.portNumbStore);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.ENTERSHOP_CUSTOMER, ID, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK)
           { 
               GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
    }

    @Override
    public void iWantThis(int ID, int numberOfProductsToBuy) {
        ClientCom con = new ClientCom(this.storeServerUrl, this.portNumbStore);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.IWANTTHIS_CUSTOMER, ID, numberOfProductsToBuy, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK)
           { 
               GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
    }

    @Override
    public void exitShopCustomer(int ID) {
        ClientCom con = new ClientCom(this.storeServerUrl, this.portNumbStore);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.EXITSHOP_CUSTOMER, ID, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK)
           { 
               GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
    }

    @Override
    public void endOperationCustomer(int ID, boolean insideTheStore) {
        ClientCom con = new ClientCom(this.storeServerUrl, this.portNumbStore);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.ENDOPERATION_CUSTOMER, ID, insideTheStore, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK)
           { 
               GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
    }

    @Override
    public boolean perusingAround() {
        ClientCom con = new ClientCom(this.storeServerUrl, this.portNumbStore);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.PERUSINGAROUND_CUSTOMER, getId(), getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.TRUE && inMessage.getType () != Message.FALSE)
           { GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.FALSE + 
                    " ou " + Message.TRUE);
               
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
        
        return inMessage.getType() == Message.TRUE;
    }

    @Override
    public boolean canEndOperationCustomer(int ID, boolean insideTheStore) {
        ClientCom con = new ClientCom(this.storeServerUrl, this.portNumbStore);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.CANENDOPERATION_CUSTOMER, ID, insideTheStore, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.TRUE && inMessage.getType () != Message.FALSE)
           { 
               GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.FALSE + 
                    " ou " + Message.TRUE);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
        
        return inMessage.getType() == Message.TRUE;
    }
    
    private String getState() {
        return ((CustomerThread)Thread.currentThread()).getCurrentState();
    }
    
    private int getId() {
        return ((CustomerThread)Thread.currentThread()).getCurrentId();
    }
}
