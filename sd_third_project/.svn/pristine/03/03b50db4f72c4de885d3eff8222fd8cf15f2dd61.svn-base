/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.thread;

import utils.Combo;
import genclass.GenericIO;
import interfaces.CustomerStoreInterface;
import java.rmi.RemoteException;

/**
 *  Este tipo de dados implementa o thread cliente.<p>
 * 
 * @author luis
 * @author joelpinheiro
 */
public class CustomerThread extends Thread{

    private final int ID;
    private final CustomerStoreInterface STOREMONITOR;
    private boolean insideTheStore;
    private boolean exitShop;
    private String state;
    private int[] vectorClock;
    private final int vectorClockPosition; 

    /**
     * Instanciação do thread cliente.
     *
     * @param ID ID
     * @param storeMonitor storeMonitor
     */
    public CustomerThread(int ID, CustomerStoreInterface storeMonitor, int[] vectorClock, int vectorClockPosition) {
        this.ID = ID;
        this.STOREMONITOR = storeMonitor;
        this.insideTheStore = false;
        this.exitShop = false;
        this.state = "CODC";

        this.vectorClock = vectorClock;
        this.vectorClockPosition = vectorClockPosition;
    }

    /**
     * Ciclo de vida do thread cliente.
     */
    @Override
    public void run() {
        Combo combo;
        
        boolean doorOpen;
        try {
            do {
                this.state = "CODC";
                livingNormalLife();

                this.state = "CSDO";
                vectorClock[vectorClockPosition]++;
                combo = this.STOREMONITOR.goShopping(this.ID, this.state, vectorClock, vectorClockPosition);
                this.vectorClock = combo.getVectorClock().clone();
                
                this.state = "CSDO";
                vectorClock[vectorClockPosition]++;
                combo = this.STOREMONITOR.isDoorOpen(vectorClock, vectorClockPosition);
                doorOpen = (Boolean) combo.getObject();
                this.vectorClock = combo.getVectorClock().clone();
                while (doorOpen) {
                    this.state = "AOID";
                    vectorClock[vectorClockPosition]++;
                    combo = this.STOREMONITOR.enterShopCustomer(this.ID, this.state, vectorClock, vectorClockPosition);
                    this.vectorClock = combo.getVectorClock().clone();
                    this.insideTheStore = true;

                    this.state = "AOID";
                    vectorClock[vectorClockPosition]++;
                    combo = this.STOREMONITOR.perusingAround(vectorClock, vectorClockPosition);
                    this.exitShop = (Boolean) combo.getObject();
                    lookAround();

                    if (this.exitShop) {
                        this.state = "CODC";
                        vectorClock[vectorClockPosition]++;
                        combo = this.STOREMONITOR.exitShopCustomer(this.ID, this.state, vectorClock, vectorClockPosition);
                        this.vectorClock = combo.getVectorClock().clone();
                        this.insideTheStore = false;
                        break;
                    }

                    int numberOfProductsToBuy = NumberOfGoodsToBuy();

                    this.state = "BSG ";
                    vectorClock[vectorClockPosition]++;
                    combo = this.STOREMONITOR.iWantThis(this.ID, numberOfProductsToBuy, this.state, vectorClock, vectorClockPosition);
                    this.vectorClock = combo.getVectorClock().clone();
                    
                    this.state = "CODC";
                    vectorClock[vectorClockPosition]++;
                    combo = this.STOREMONITOR.exitShopCustomer(this.ID, this.state, vectorClock, vectorClockPosition);
                    this.vectorClock = combo.getVectorClock().clone();
                    this.insideTheStore = false;

                    break;

                }
            } while (!this.endOperationCustomer(this.ID, this.insideTheStore, this.state));
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota de método "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Operação que retorna o número de produtos a comprar pelo cliente.
     * 70% - 1 produtos.
     * 30% - 2 produtos.
     * 
     * @return return result 
     */
    public int NumberOfGoodsToBuy(){
        if(Math.random() <= 0.7) {
            return 1;
        }
        else{
            return 2;
        }
    }
    
    /**
     * Operação que verifica se o cliente quer ou não comprar alguma coisa.
     * 80% - Quer.
     * 20% - Não quer.
     * @return return result 
     */
    private boolean exitShop() {
        
        double l = Math.random();
        return l <= 0.7;
        
    }
 
    /**
     * Viver a vida normalmente (operação interna).
     */
    private void livingNormalLife() {
        try
        { 
            sleep ((long) (1 + 100 * Math.random ()));
        }
        catch (InterruptedException e) {
        }
    }
    
    /**
     * Cliente procura o que comprar pela loja (operação interna).
     */
    private void lookAround(){
        try
        { 
            sleep ((long) (1 + 100 * Math.random ()));
        }
        catch (InterruptedException e) {
        }
    }

    /**
     *
     * @return return result
     */
    public synchronized int getCurrentId() {
        return this.ID;
    }

    /**
     *
     * @return return result
     */
    public synchronized String getCurrentState() {
        return this.state;
    }

    private boolean endOperationCustomer(int ID, boolean insideTheStore, String state) {
        Combo combo;
        try {
            vectorClock[vectorClockPosition]++;
            combo = this.STOREMONITOR.canEndOperationCustomer(ID, insideTheStore, state, vectorClock, vectorClockPosition);
            this.vectorClock = combo.getVectorClock().clone();
            if ((Boolean) combo.getObject()) {
                this.state = "DIED";
                vectorClock[vectorClockPosition]++;
                combo = this.STOREMONITOR.endOperationCustomer(ID, insideTheStore, this.state, vectorClock, vectorClockPosition);
                this.vectorClock = combo.getVectorClock().clone();
                return true;
            } else {
                return false;
            }
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota do método "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }
}
