/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entrepreneur.thread;

import utils.Combo;
import genclass.GenericIO;
import interfaces.EntrepreneurStoreInterface;
import interfaces.EntrepreneurWorkshopInterface;
import interfaces.EntrepreneurStorageInterface;
import java.rmi.RemoteException;

/**
 * Este tipo de dados implementa o thread dona.<p>
 *
 * @author luis
 * @author joelpinheiro
 */
public class EntrepreneurThread extends Thread {

    private final int amountProductsToPickUp;
    private final int numberOfReplenish;
    private String currentState;
    private final EntrepreneurStoreInterface STOREMONITOR;
    private final EntrepreneurWorkshopInterface WORKSHOPMONITOR;
    private final EntrepreneurStorageInterface STORAGEMONITOR;
    private int[] vectorClock;
    private final int vectorClockPosition;

    /**
     * Instanciação do thread dona.
     *
     * @param entrepreneurID entrepreneurID
     * @param amountProductsToPickUp amountProductsToPickUp
     * @param numberOfReplenish numberOfReplenish
     * @param storeMonitor storeMonitor
     * @param workshopMonitor workshopMonitor
     * @param storageMonitor storageMonitor
     * @param vectorClock
     * @param vectorClockPosition
     */
    public EntrepreneurThread(int entrepreneurID,
            int amountProductsToPickUp,
            int numberOfReplenish,
            EntrepreneurStoreInterface storeMonitor,
            EntrepreneurWorkshopInterface workshopMonitor,
            EntrepreneurStorageInterface storageMonitor,
            int[] vectorClock,
            int vectorClockPosition) {

        this.amountProductsToPickUp = amountProductsToPickUp;
        this.numberOfReplenish = numberOfReplenish;
        this.currentState = "OTS";
        this.STOREMONITOR = storeMonitor;
        this.WORKSHOPMONITOR = workshopMonitor;

        this.STORAGEMONITOR = storageMonitor;
        
        this.vectorClock = vectorClock;
        this.vectorClockPosition = vectorClockPosition;
    }

    /**
     * Ciclo de vida da thread da dona.
     */
    @Override
    public void run() {
        boolean stayInStore;
        char appraiseSit = ' ';
        Combo combo;

        try {
            lifecycle:
            do {
                stayInStore = true;
                this.currentState = "WFNT";
                
                vectorClock[vectorClockPosition]++;
                combo = this.STOREMONITOR.prepareToWork(this.currentState, vectorClock, vectorClockPosition);
                this.vectorClock = combo.getVectorClock().clone();
                
                while (stayInStore) // While in store
                {
                    vectorClock[vectorClockPosition]++;
                    combo = this.STOREMONITOR.appraiseSit(this.currentState, vectorClock, vectorClockPosition);
                    appraiseSit = (Character) combo.getObject();
                    this.vectorClock = combo.getVectorClock().clone();
                    
                    
                    switch (appraiseSit) {
                        case 'C': // Attending a Customer
                        {
                            this.currentState = "AAC ";
                            vectorClock[vectorClockPosition]++;
                            combo = this.STOREMONITOR.addressCustomer(this.currentState, vectorClock, vectorClockPosition);
                            this.vectorClock = combo.getVectorClock().clone();
                            
                            vectorClock[vectorClockPosition]++;
                            combo = this.STOREMONITOR.serviceCustomer(this.currentState, vectorClock, vectorClockPosition);
                            this.vectorClock = combo.getVectorClock().clone();
                            
                            this.currentState = "WFNT";
                            vectorClock[vectorClockPosition]++;
                            combo = this.STOREMONITOR.sayGoodByeToCustomer(this.currentState, vectorClock, vectorClockPosition);
                            this.vectorClock = combo.getVectorClock().clone();
                            
                            break;
                        }
                        case 'M':                                                   // Buying prime materials
                        case 'T': // Collecting a batch of products
                        {
                            vectorClock[vectorClockPosition]++;
                            combo = this.STOREMONITOR.customerInShop(this.currentState, vectorClock, vectorClockPosition);
                            stayInStore = (Boolean) combo.getObject();
                            this.vectorClock = combo.getVectorClock().clone();

                            
                            this.currentState = "WFNT";
                            vectorClock[vectorClockPosition]++;
                            combo = this.STOREMONITOR.closeTheDoor(this.currentState, vectorClock, vectorClockPosition);
                            this.vectorClock = combo.getVectorClock().clone();
                            break;
                        }
                        case 'E': // Say Goodbye
                        {
                            vectorClock[vectorClockPosition]++;
                            combo = this.STOREMONITOR.customerInShop(this.currentState, vectorClock, vectorClockPosition);
                            stayInStore = (Boolean) combo.getObject();
                            this.vectorClock = combo.getVectorClock().clone();
                            
                            this.currentState = "WFNT";
                            vectorClock[vectorClockPosition]++;
                            combo = this.STOREMONITOR.closeTheDoor(this.currentState, vectorClock, vectorClockPosition);
                            this.vectorClock = combo.getVectorClock().clone();
                            
                            if (this.endOperationEntrepreneur(this.currentState)) {
                                break lifecycle;
                            }

                            break;
                        }
                    }
                }

                this.currentState = "CTS ";
                vectorClock[vectorClockPosition]++;
                combo = this.STOREMONITOR.prepareToLeave(this.currentState, vectorClock, vectorClockPosition);
                this.vectorClock = combo.getVectorClock().clone();
                
                if (appraiseSit == 'T') {
                    this.currentState = "CABP";
                    vectorClock[vectorClockPosition]++;
                    combo = this.WORKSHOPMONITOR.collectABatchOfProducts(this.amountProductsToPickUp, this.currentState, vectorClock, vectorClockPosition);
                    int gatheredProducts = (Integer) combo.getObject();
                    this.vectorClock = combo.getVectorClock().clone();

                    vectorClock[vectorClockPosition]++;
                    combo = this.STOREMONITOR.addProducts(gatheredProducts, this.currentState, vectorClock, vectorClockPosition);
                    this.vectorClock = combo.getVectorClock().clone();
                }

                if (appraiseSit == 'M') {
                    this.currentState = "BPM ";
                    vectorClock[vectorClockPosition]++;
                    combo = this.STOREMONITOR.visitSuppliers(this.currentState, vectorClock, vectorClockPosition);
                    this.vectorClock = combo.getVectorClock().clone();
                    
                    this.currentState = "DPM ";
                    replenishStock(this.numberOfReplenish, this.currentState);

                }

                this.currentState = "OTS ";
                vectorClock[vectorClockPosition]++;
                combo = this.STOREMONITOR.returnToShop(this.currentState, vectorClock, vectorClockPosition);
                this.vectorClock = combo.getVectorClock().clone();
                
            } while (true);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota de método "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Operação de transportar matérias primas do armazém para a loja.
     *
     * @param numberOfReplenish
     */
    private void replenishStock(int numberOfReplenish, String state) {
        Combo combo;
        
        try {
            vectorClock[vectorClockPosition]++;
            combo = this.STORAGEMONITOR.removePrimeMaterial(numberOfReplenish, this.currentState, vectorClock, vectorClockPosition);
            int amountPrimeMAterials = (Integer) combo.getObject();
            this.vectorClock = combo.getVectorClock().clone();

            vectorClock[vectorClockPosition]++;
            combo = this.STOREMONITOR.replenishStock(this.currentState, vectorClock, vectorClockPosition);
            this.vectorClock = combo.getVectorClock().clone();
            
            vectorClock[vectorClockPosition]++;
            combo = this.WORKSHOPMONITOR.addPrimeMaterial(amountPrimeMAterials, this.currentState, vectorClock, vectorClockPosition);
            this.vectorClock = combo.getVectorClock().clone();
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota de método "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     *
     * @return return result
     */
    public synchronized String getCurrentState() {
        return this.currentState;
    }

    private boolean endOperationEntrepreneur(String state) {
        Combo combo;
        
        try {
            this.currentState = "CTS ";
            vectorClock[vectorClockPosition]++;
            combo = this.STOREMONITOR.prepareToLeave(state, vectorClock, vectorClockPosition);
            this.vectorClock = combo.getVectorClock().clone();
            
            this.currentState = "DIED";
            vectorClock[vectorClockPosition]++;
            combo = this.STOREMONITOR.endOperationEntrepreneur(this.currentState, vectorClock, vectorClockPosition);
            this.vectorClock = combo.getVectorClock().clone();
            
            return true;
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota de método "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }
}
