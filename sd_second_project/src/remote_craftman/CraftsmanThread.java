/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_craftman;

import remote_craftman.interfaces.CraftsmanWorkshopInterface;

/**
 *  Este tipo de dados implementa o thread artesão.<p>
 * 
 * @author luis, joelpinheiro
 */
public class CraftsmanThread extends Thread{
    private final int ID;
    private final CraftsmanWorkshopInterface WORKSHOPMONITOR;
    private int productStep;
    private int amountToCollect;
    private int elapsedTime;
    private String state;
    
    /**
     *
     * @param ID ID
     * @param productStep productStep
     * @param amountToCollect amountToCollect
     * @param workshopMonitor workshopMonitor
     * @param elapsedTime elapsedTime
     */
    public CraftsmanThread(int ID, int productStep, int amountToCollect,
            CraftsmanWorkshopInterface workshopMonitor, int elapsedTime) {
        this.ID = ID;
        this.productStep = productStep;
        this.amountToCollect = amountToCollect;
        this.WORKSHOPMONITOR = workshopMonitor;
        this.elapsedTime = elapsedTime;
        this.state = "FPMS";
    }

    private void shapingItUp(int elapsedTime){
        long time = (long)(1 + 100 * Math.random());
        
        try {
            if(elapsedTime >= 0)
                time = elapsedTime;
            sleep(time);
        } catch (InterruptedException ex) {
            // we may need to address this
            System.out.println("Sleep error here!!");
        }
    }
    
    @Override
    public void run(){
        boolean enoughPrimeMaterials;
        
        while(this.isThereWorkLeft(this.ID)){
            this.state = "FPMS";
            enoughPrimeMaterials = this.WORKSHOPMONITOR.checkForMaterials(this.ID, this.amountToCollect);
            
            if(enoughPrimeMaterials){
                this.state = "FPMS";
                boolean hasMaterials= this.WORKSHOPMONITOR.collectMaterials(this.ID, this.amountToCollect);
                
                if (hasMaterials){
                    this.state = "PANP";
                    this.WORKSHOPMONITOR.prepareToProduce(this.ID, this.productStep);

                    this.state = "PANP";
                    shapingItUp(this.elapsedTime);

                    this.state = "SIFT";
                    boolean productThresholdReached = this.WORKSHOPMONITOR.goToStore(this.ID, this.productStep);

                    if (productThresholdReached) {
                        this.state = "CTE ";
                        this.WORKSHOPMONITOR.batchReadyForTransfer(this.ID);

                        this.state = "FPMS";
                        this.WORKSHOPMONITOR.backToWork(this.ID);
                    } else {
                        this.state = "FPMS";
                        this.WORKSHOPMONITOR.backToWork(this.ID);
                    }
                }
                else{
                    this.state = "CTE ";
                    this.WORKSHOPMONITOR.primeMaterialsNeeded(this.ID);

                    this.state = "FPMS";
                    this.WORKSHOPMONITOR.backToWork(this.ID);
                }
            }
            else{
                this.state = "CTE ";
                this.WORKSHOPMONITOR.primeMaterialsNeeded(this.ID);
                
                this.state = "FPMS";
                this.WORKSHOPMONITOR.backToWork(this.ID);
            }
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

    private boolean isThereWorkLeft(int ID) {
        if(!this.WORKSHOPMONITOR.isThereWorkLeft(ID)){
            this.state = "DIED";
            this.WORKSHOPMONITOR.endOperationCraftsman(ID);
            return false;
        }
        else {
            return true;
        }
    }
}
