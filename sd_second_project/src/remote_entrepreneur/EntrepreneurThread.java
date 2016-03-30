/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_entrepreneur;
   
import remote_entrepreneur.interfaces.EntrepreneurStoreInterface;
import remote_entrepreneur.interfaces.EntrepreneurWorkshopInterface;
import remote_entrepreneur.interfaces.EntrepreneurStorageInterface;

/**
 *  Este tipo de dados implementa o thread dona.<p>
 * 
 * @author luis
 * @author joelpinheiro
 */
public class EntrepreneurThread extends Thread{
    private final int amountProductsToPickUp;
    private final int numberOfReplenish;
    private String currentState;
    private final EntrepreneurStoreInterface STOREMONITOR;
    private final EntrepreneurWorkshopInterface WORKSHOPMONITOR;
    private final EntrepreneurStorageInterface STORAGEMONITOR;
    
    /**
     * Instanciação do thread dona.
     * 
     * @param entrepreneurID entrepreneurID
     * @param amountProductsToPickUp amountProductsToPickUp
     * @param numberOfReplenish numberOfReplenish
     * @param storeMonitor storeMonitor
     * @param workshopMonitor workshopMonitor
     * @param storageMonitor storageMonitor
     */
    public EntrepreneurThread(int entrepreneurID, 
            int amountProductsToPickUp,
            int numberOfReplenish,
            EntrepreneurStoreInterface storeMonitor,
            EntrepreneurWorkshopInterface workshopMonitor,
            EntrepreneurStorageInterface storageMonitor) {
        
        this.amountProductsToPickUp = amountProductsToPickUp;
        this.numberOfReplenish = numberOfReplenish;
        this.currentState = "OTS";
        this.STOREMONITOR = storeMonitor;
        this.WORKSHOPMONITOR = workshopMonitor;
        
        this.STORAGEMONITOR = storageMonitor;
    }
    
    /**
     * Ciclo de vida da thread da dona.
     */
    @Override
    public void run() {
        boolean stayInStore;
        char appraiseSit = ' ';
        
        lifecycle: do
        {
            stayInStore = true;
            this.currentState = "WFNT";
            this.STOREMONITOR.prepareToWork();
            while(stayInStore)                                                         // While in store
            {
                appraiseSit = this.STOREMONITOR.appraiseSit();
                switch(appraiseSit)
                {
                    case 'C':                                                   // Attending a Customer
                    {
                        this.currentState = "AAC ";
                        this.STOREMONITOR.addressCustomer();
                        
                        this.STOREMONITOR.serviceCustomer();
                        
                        this.currentState = "WFNT";
                        this.STOREMONITOR.sayGoodByeToCustomer();
                        
                        break;
                    }
                    case 'M':                                                   // Buying prime materials
                    case 'T':                                                   // Collecting a batch of products
                    {
                        stayInStore = this.STOREMONITOR.customerInShop();
                        
                        this.currentState = "WFNT";
                        this.STOREMONITOR.closeTheDoor();
                        break;
                    }
                    case 'E':                                                   // Say Goodbye
                    {    
                        stayInStore = this.STOREMONITOR.customerInShop();
                        
                        this.currentState = "WFNT";
                        this.STOREMONITOR.closeTheDoor();
                        
                        if(this.endOperationEntrepreneur())
                            break lifecycle;
                        
                        break;
                    }
                }
            }
            
            this.currentState = "CTS ";
            this.STOREMONITOR.prepareToLeave();
            
            if (appraiseSit == 'T')
            {
                this.currentState = "CABP";
                int gatheredProducts = this.WORKSHOPMONITOR.collectABatchOfProducts(this.amountProductsToPickUp);
                
                this.STOREMONITOR.addProducts(gatheredProducts);
              
            }
            
            if (appraiseSit == 'M')
            {
                this.currentState = "BPM ";
                this.STOREMONITOR.visitSuppliers();
                
                this.currentState = "DPM ";
                replenishStock(this.numberOfReplenish);
                
            }
            
            this.currentState = "OTS ";
            this.STOREMONITOR.returnToShop();
            
        } while(true);//while(!endOperationEntrepreneur()); // !this.STOREMONITOR.endOperationEntrepreneur());
    }

    /**
     * Operação de transportar matérias primas do armazém para a loja.
     * @param numberOfReplenish 
     */
    private void replenishStock(int numberOfReplenish) {
        int amountPrimeMAterials = this.STORAGEMONITOR.removePrimeMaterial(numberOfReplenish);
                
        this.STOREMONITOR.replenishStock();
        
        this.WORKSHOPMONITOR.addPrimeMaterial(amountPrimeMAterials);
    }

    /**
     *
     * @return return result
     */
    public synchronized String getCurrentState() {
        return this.currentState;
    }

    private boolean endOperationEntrepreneur() {

        this.currentState = "CTS ";
        this.STOREMONITOR.prepareToLeave();
        this.currentState = "DIED";
        this.STOREMONITOR.endOperationEntrepreneur();
        return true;

    }
}
