/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_craftman;

import communication.*;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import remote_craftman.interfaces.CraftsmanWorkshopInterface;

/**
 *
 * CraftmanWorkshopStub.
 * 
 * @author joelpinheiro
 */
public class CraftmanWorkshopStub implements CraftsmanWorkshopInterface {

  /**
   *  Nome do sistema computacional onde está localizado o servidor
   *    @serialField serverHostName
   */

   private String serverHostName = null;

  /**
   *  Número do port de escuta do servidor
   *    @serialField serverPortNumb
   */

   private int serverPortNumb;

    CraftmanWorkshopStub(String hostUrl, int portNum) {
        this.serverHostName = hostUrl;
        this.serverPortNumb = portNum;
    }

    @Override
    public boolean isThereWorkLeft(int ID) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message (Message.ISTHEREWORKLEFT_WORKSHOP, ID, getState());
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.TRUE) && (inMessage.getType() != Message.FALSE)) {
            GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.FALSE + 
                    " ou " + Message.TRUE);
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();

        if(inMessage.getType() == Message.TRUE) {
            return true;                                         
        } else {
            return false;                                    
        }
    }

    @Override
    public boolean checkForMaterials(int ID, int amountToCollect) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.CHECKFORMATERIALS_WORKSHOP, ID, amountToCollect, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.TRUE) && (inMessage.getType() != Message.FALSE)) {
            GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.FALSE + 
                    " ou " + Message.TRUE);
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
        
        if (inMessage.getType() == Message.TRUE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean collectMaterials(int ID, int amountToCollect) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.COLLECTMATERIALS_WORKSHOP, ID, amountToCollect, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.TRUE) && (inMessage.getType() != Message.FALSE)) {
            GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.FALSE + 
                    " ou " + Message.TRUE);
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
        
        if (inMessage.getType() == Message.TRUE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean goToStore(int ID, int productStep) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.GOTOSTORE_WORKSHOP, ID, productStep, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.TRUE) && (inMessage.getType() != Message.FALSE)) {
            GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.FALSE + 
                    " ou " + Message.TRUE);
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
        
        if (inMessage.getType() == Message.TRUE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void batchReadyForTransfer(int ID) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.BATCHREADYFORTRANSFER_WORKSHOP, ID, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK) {
            GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        con.close ();
    }

    @Override
    public void backToWork(int ID) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.BACKTOWORK_WORKSHOP, ID, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void primeMaterialsNeeded(int ID) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.PRIMEMATERIALISNEEDED_WORKSHOP, ID, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void prepareToProduce(int ID, int productStep) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.PREPARETOPRODUCE_WORKSHOP, ID, productStep, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK) {
            GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        con.close ();
    }

    @Override
    public void endOperationCraftsman(int ID) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.ENDOPERATIONCRAFTMAN_WORKSHOP, ID, getState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType () != Message.ACK) {
            GenericIO.writelnString ("Thread: Tipo inválido! teve: " + inMessage.getType () + " esperava " + Message.ACK);
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        con.close ();
    }

    private String getState() {
        return ((CraftsmanThread)Thread.currentThread()).getCurrentState();
    }
}
