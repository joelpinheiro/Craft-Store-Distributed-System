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

/**
 * EntrepreneurStoreStub.
 * @author luis
 */
public class EntrepreneurStoreStub implements EntrepreneurStoreInterface{
    
    private int portNumb;
    private String serverUrl;

    EntrepreneurStoreStub(String serverUrlStore, int portNumbStore) {
        this.portNumb = portNumbStore;
        this.serverUrl = serverUrlStore;
    }

    @Override
    public void addressCustomer() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.ADDRESSCUSTOMER_ENTREPRENEUR, getState());
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
    public void sayGoodByeToCustomer() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.SAYGOODBYETOCUSTOMER_ENTREPRENEUR, getState());
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
    public void closeTheDoor() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.CLOSETHEDOOR_ENTREPRENEUR, getState());
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
    public boolean customerInShop() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.CUSTOMERINSHOP_ENTREPRENEUR, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.TRUE && inMessage.getType() != Message.FALSE)
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
    public void prepareToLeave() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.PREPARETOLEAVE_ENTREPRENEUR, getState());
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
    public void addProducts(int amountProductsToPickUpFromWorkshop) {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.ADDPRODUCTS_ENTREPRENEUR, amountProductsToPickUpFromWorkshop, getState());
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
    public void visitSuppliers() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.VISITSUPPLIERS_ENTREPRENEUR, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK)
           { GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
    }

    @Override
    public void returnToShop() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.RETURNTOSHOP_ENTREPRENEUR, getState());
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
    public char appraiseSit() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.APPRAISESIT_ENTREPRENEUR, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.APPRAISESITRESULT_ENTREPRENEUR)
           { 
               GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.APPRAISESITRESULT_ENTREPRENEUR);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
        
        return inMessage.getAppraiseSitResult();
    }

    @Override
    public void replenishStock() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.REPLENISHSTOCK_ENTREPRENEUR, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK)
           { GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
    }

    @Override
    public void endOperationEntrepreneur() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.ENDOPERATIONENTREPRENEUR_ENTREPRENEUR, getState());
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
    public void serviceCustomer() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.SERVICECUSTOMER_ENTREPRENEUR, getState());
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
    public void prepareToWork() {
        ClientCom con = new ClientCom(this.serverUrl, this.portNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.PREPARETOWORK_ENTREPRENEUR, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK)
           { GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
           }
        con.close ();
    }
    
    private String getState() {
        return ((EntrepreneurThread)Thread.currentThread()).getCurrentState();
    }
}
