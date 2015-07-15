/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package craftsman.thread;

import utils.Combo;
import genclass.GenericIO;
import interfaces.CraftsmanWorkshopInterface;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Este tipo de dados implementa o thread artesão.<p>
 *
 * @author luis, joelpinheiro
 */
public class CraftsmanThread extends Thread {

    private final int ID;
    private final CraftsmanWorkshopInterface WORKSHOPMONITOR;
    private int productStep;
    private int amountToCollect;
    private int elapsedTime;
    private String state;
    
    private int[] vectorClock;
    private final int vectorClockPosition;
    

    /**
     *
     * @param ID ID
     * @param productStep productStep
     * @param amountToCollect amountToCollect
     * @param workshopMonitor workshopMonitor
     * @param elapsedTime elapsedTime
     */
    public CraftsmanThread(int ID, int productStep, int amountToCollect,
            CraftsmanWorkshopInterface workshopMonitor, int elapsedTime, int[] vectorClock, int vectorClockPosition) {
        this.ID = ID;
        this.productStep = productStep;
        this.amountToCollect = amountToCollect;
        this.WORKSHOPMONITOR = workshopMonitor;
        this.elapsedTime = elapsedTime;
        this.state = "FPMS";
        
        this.vectorClock = vectorClock;
        this.vectorClockPosition = vectorClockPosition;
    }

    private void shapingItUp(int elapsedTime) {
        long time = (long) (1 + 100 * Math.random());

        try {
            if (elapsedTime >= 0) {
                time = elapsedTime;
            }
            sleep(time);
        } catch (InterruptedException ex) {
            // we may need to address this
            System.out.println("Sleep error here!!");
        }
    }

    @Override
    public void run() {
        Combo combo;
        
        boolean enoughPrimeMaterials;

        try {
            while (this.isThereWorkLeft(this.ID, this.state)) {
                this.state = "FPMS";
                vectorClock[vectorClockPosition]++;
                combo = this.WORKSHOPMONITOR.checkForMaterials(this.ID, this.amountToCollect, this.state, vectorClock, vectorClockPosition);
                enoughPrimeMaterials = (Boolean) combo.getObject();
                this.vectorClock = combo.getVectorClock().clone();

                if (enoughPrimeMaterials) {
                    this.state = "FPMS";
                    vectorClock[vectorClockPosition]++;
                    combo = this.WORKSHOPMONITOR.collectMaterials(this.ID, this.amountToCollect, this.state, vectorClock, vectorClockPosition);
                    boolean hasMaterials = (Boolean) combo.getObject();
                    this.vectorClock = combo.getVectorClock().clone();

                    if (hasMaterials) {
                        this.state = "PANP";
                        vectorClock[vectorClockPosition]++;
                        combo = this.WORKSHOPMONITOR.prepareToProduce(this.ID, this.productStep, this.state, vectorClock, vectorClockPosition);
                        this.vectorClock = combo.getVectorClock().clone();
                        
                        this.state = "PANP";
                        shapingItUp(this.elapsedTime);

                        this.state = "SIFT";
                        vectorClock[vectorClockPosition]++;
                        combo = this.WORKSHOPMONITOR.goToStore(this.ID, this.productStep, this.state, vectorClock, vectorClockPosition);
                        boolean productThresholdReached = (Boolean) combo.getObject();
                        this.vectorClock = combo.getVectorClock().clone();

                        if (productThresholdReached) {
                            this.state = "CTE ";
                            vectorClock[vectorClockPosition]++;
                            combo = this.WORKSHOPMONITOR.batchReadyForTransfer(this.ID, this.state, vectorClock, vectorClockPosition);
                            this.vectorClock = combo.getVectorClock().clone();
                            
                            this.state = "FPMS";
                            vectorClock[vectorClockPosition]++;
                            combo = this.WORKSHOPMONITOR.backToWork(this.ID, this.state, vectorClock, vectorClockPosition);
                            this.vectorClock = combo.getVectorClock().clone();
                        } else {
                            this.state = "FPMS";
                            vectorClock[vectorClockPosition]++;
                            combo = this.WORKSHOPMONITOR.backToWork(this.ID, this.state, vectorClock, vectorClockPosition);
                            this.vectorClock = combo.getVectorClock().clone();
                        }
                    } else {
                        this.state = "CTE ";
                        vectorClock[vectorClockPosition]++;
                        combo = this.WORKSHOPMONITOR.primeMaterialsNeeded(this.ID, this.state, vectorClock, vectorClockPosition);
                        this.vectorClock = combo.getVectorClock().clone();
                        
                        this.state = "FPMS";
                        vectorClock[vectorClockPosition]++;
                        combo = this.WORKSHOPMONITOR.backToWork(this.ID, this.state, vectorClock, vectorClockPosition);
                        this.vectorClock = combo.getVectorClock().clone();
                    }
                } else {
                    this.state = "CTE ";
                    vectorClock[vectorClockPosition]++;
                    combo = this.WORKSHOPMONITOR.primeMaterialsNeeded(this.ID, this.state, vectorClock, vectorClockPosition);
                    this.vectorClock = combo.getVectorClock().clone();
                    
                    this.state = "FPMS";
                    vectorClock[vectorClockPosition]++;
                    combo = this.WORKSHOPMONITOR.backToWork(this.ID, this.state, vectorClock, vectorClockPosition);
                    this.vectorClock = combo.getVectorClock().clone();
                }
            }
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

    private boolean isThereWorkLeft(int ID, String state) {
        Combo combo = new Combo();
        try {
            combo = this.WORKSHOPMONITOR.isThereWorkLeft(ID, state, vectorClock, vectorClockPosition);
            this.vectorClock = combo.getVectorClock().clone();
        } catch (RemoteException ex) {
            Logger.getLogger(CraftsmanThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        try {
            if (!(Boolean)combo.getObject()) {
                this.state = "DIED";
                this.WORKSHOPMONITOR.endOperationCraftsman(ID, this.state, vectorClock, vectorClockPosition);
                return false;
            } else {
                return true;
            }
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota de método "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }
}
