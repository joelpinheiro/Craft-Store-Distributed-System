/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop;

import log.interfaces.WorkshopLogInterface;
import store.intefaces.WorkshopStoreInterface;
import workshop.interfaces.CraftsmanWorkshopInterface;
import workshop.interfaces.EntrepreneurWorkshopInterface;

/**
 * WorkshopMonitor e uma classe monitor que gere recursos partilhados, cujo 
 * acesso e concorrente, tais como:
 * <ul>
 * <li>Quantidade de materias primas em stock, atualmente, na oficina</li>
 * <li>Quantidade de produtos atualmente fabricados na oficina</li>
 * </ul>
 * <p></p>
 * Esta classe esta preparada para ambientes multi-threaded.
 * Os objetos thread que interagem com esta classe sao:
 * <ul>
 * <li><code>CraftsmanThread</code></li>
 * <li><code>EntrepreneurThread</code></li>
 * </ul>
 * 
 * @author Luis Assuncao
 * @author Joel Pinheiro
 * @version %I%, %G%
 * @since 1.0
 */

public class WorkshopMonitor implements CraftsmanWorkshopInterface, 
        EntrepreneurWorkshopInterface{
    private int stockPrimeMaterials;
    private int stockManufacturedMaterials;
    private final WorkshopLogInterface LOGMONITOR;
    private final WorkshopStoreInterface STOREMONITOR;
    private int primeMaterialStockResuply;
    private int accumulatedSupliedPrimeMaterials;
    private int accumulatedProducedProducts;
    private boolean entrepreneurCalledReplenishPrimeMaterials;
    private int productThreshold;
    private boolean entrepreneurCalledToCollectProducts;
    private int[] craftsmanAccumulatedManufacturedProducts;
    
    /**
     * Construtor da classe monitor <code>WorkshopMonitor</code>.
     * Constroi um novo WorkshopMonitor capaz de gerir e manter o sincronismo entre varias instancias do tipo
     * <code>CraftsmanThread</code> e <code>EntrepreneurThread</code>.
     * 
     * @param productThreshold Limite maximo de produtos acabados para o qual e imitido um aviso de resolha.
     * @param logMonitor Uma instancia de <code>LogMonitor</code> para se poder reportar as alterações do estado interno.
     * @param storeMonitor Uma instancia de <code>StoreMonitor</code> para se poder interagir com a instancia de
     * <code>EntrepreneurThread</code>.
     * @param craftsmanSize Quantidade de objetos do tipo <code>CraftsmanThread</code> que vao interagir com este monitor.
     * @see <code>WorkshopLogInterface</code> 
     * @see <code>WorkshopStoreInterface</code>
     * @since 1.0
     */
    public WorkshopMonitor(int productThreshold, WorkshopLogInterface logMonitor, 
            WorkshopStoreInterface storeMonitor, int craftsmanSize){
        this.stockManufacturedMaterials =0;
        this.stockPrimeMaterials = 0;
        this.LOGMONITOR = logMonitor;
        this.STOREMONITOR = storeMonitor;
        this.entrepreneurCalledReplenishPrimeMaterials = false;
        this.primeMaterialStockResuply = 0;
        this.accumulatedProducedProducts = 0;
        this.accumulatedSupliedPrimeMaterials = 0;
        this.productThreshold = productThreshold;
        this.entrepreneurCalledToCollectProducts = false;
        
        this.craftsmanAccumulatedManufacturedProducts = new int[craftsmanSize];
        
        for(int i = 0; i < craftsmanSize; i++){
            this.craftsmanAccumulatedManufacturedProducts[i] = 0;
        }
    }

    /**
     *
     * @param ID
     * @param amountToCollect
     * @return Se a operacao teve sucesso
     */
    @Override
    public synchronized boolean collectMaterials(int ID, int amountToCollect) {
        if(this.stockPrimeMaterials >= amountToCollect){
            this.stockPrimeMaterials -= amountToCollect;
            this.LOGMONITOR.setStockPrimeMaterials(this.stockPrimeMaterials);
            return true;
        }
        
        this.LOGMONITOR.setStockPrimeMaterials(this.stockPrimeMaterials);
        return false;
    }

    /**
     *
     * @param ID
     */
    @Override
    public synchronized void primeMaterialsNeeded(int ID) {
        if(this.entrepreneurCalledReplenishPrimeMaterials == false){
            this.STOREMONITOR.needMorePrimeMaterials(ID);
            this.entrepreneurCalledReplenishPrimeMaterials = true;
            this.LOGMONITOR.setCallTransferPrimeMaterials(this.entrepreneurCalledReplenishPrimeMaterials);
        }

        this.LOGMONITOR.reportStatusCraftman();
    }

    /**
     *
     * @param ID
     */
    @Override
    public synchronized void batchReadyForTransfer(int ID) {
        
        this.STOREMONITOR.callEntrepreneurToCollectProducts(ID);
            this.entrepreneurCalledToCollectProducts = true;
            
        //this.LOGMONITOR.reportStatusCraftman(ID, "CTE ");
        this.LOGMONITOR.reportStatusCraftman();
    }

    /**
     *
     * @param ID
     * @param amountToCollect
     * @return
     */
    @Override
    public synchronized boolean checkForMaterials(int ID, int amountToCollect) {
        //this.LOGMONITOR.reportStatusCraftman(ID, "FPMS");
        this.LOGMONITOR.reportStatusCraftman();
        
        while(true){
            try {
                if(!this.entrepreneurCalledReplenishPrimeMaterials)
                    break;
                
                wait();
            } catch (InterruptedException ex) {
            }
        }

        boolean enoughPrimeMaterialsHere = this.stockPrimeMaterials > amountToCollect;
        
        boolean enoughStorageStock = this.LOGMONITOR.isThereStockInStoreHouse();
        
        if(enoughStorageStock == false)
            return true;
        
        return enoughPrimeMaterialsHere;
            
    }
    
    /**
     *
     * @param amountPrimeMAterials
     */
    @Override
    public synchronized void addPrimeMaterial(int amountPrimeMAterials) {

        this.stockPrimeMaterials += amountPrimeMAterials;
        this.primeMaterialStockResuply++;
        this.accumulatedSupliedPrimeMaterials += amountPrimeMAterials;
        
        this.entrepreneurCalledReplenishPrimeMaterials = false;
        this.LOGMONITOR.setCallTransferPrimeMaterials(this.entrepreneurCalledReplenishPrimeMaterials);
        this.LOGMONITOR.setStockPrimeMaterials(this.stockPrimeMaterials);
        this.LOGMONITOR.setResuplyPrimeMaterials(this.primeMaterialStockResuply);
        this.LOGMONITOR.setTotalAmountPrimeMaterialsSupplied(this.accumulatedSupliedPrimeMaterials);
        
        notifyAll();
        
    }
    
    /**
     *
     * @param ID
     * @param productStep
     */
    @Override
    public synchronized void prepareToProduce(int ID, int productStep) {
        this.LOGMONITOR.reportStatusCraftman();
    }

    /**
     *
     * @param ID
     * @param productStep
     * @return
     */
    @Override
    public synchronized boolean goToStore(int ID, int productStep) {
        this.stockManufacturedMaterials += productStep;
        this.accumulatedProducedProducts += productStep;
        this.craftsmanAccumulatedManufacturedProducts[ID] += productStep;

        this.LOGMONITOR.setStockFinnishedProducts(this.stockManufacturedMaterials);
        this.LOGMONITOR.setTotalAmountProductsManufactured(this.accumulatedProducedProducts);
        this.LOGMONITOR.addCraftsmanAccManProducts(ID, this.craftsmanAccumulatedManufacturedProducts[ID]);
        //this.LOGMONITOR.reportStatusCraftman(ID, "SIFT");
        this.LOGMONITOR.reportStatusCraftman();
        
        return this.stockManufacturedMaterials >= this.productThreshold && 
                this.entrepreneurCalledToCollectProducts == false;
    }

    /**
     *
     * @param ID
     */
    @Override
    public synchronized void backToWork(int ID) {
        if(this.accumulatedProducedProducts == this.productThreshold){
            this.LOGMONITOR.reportStatusCraftman();
        }
        else{
            this.LOGMONITOR.reportStatusCraftman();
        }
    }

    /**
     *
     * @param amountProductsToPickUp
     * @return
     */
    @Override
    public synchronized int collectABatchOfProducts(int amountProductsToPickUp) {
        if(this.stockManufacturedMaterials >= amountProductsToPickUp)
        {
            this.stockManufacturedMaterials -= amountProductsToPickUp;
            this.LOGMONITOR.setStockFinnishedProducts(this.stockManufacturedMaterials);
            this.entrepreneurCalledToCollectProducts = false;
            return amountProductsToPickUp;
        }
        else
        {
            int tmp = this.stockManufacturedMaterials;
            this.stockManufacturedMaterials = 0;
            this.LOGMONITOR.setStockFinnishedProducts(this.stockManufacturedMaterials);
            this.entrepreneurCalledToCollectProducts = false;
            return tmp;
        }
    }

    /**
     *
     * @param ID
     * @return
     */
    @Override
    public synchronized boolean isThereWorkLeft(int ID) {
        if(this.LOGMONITOR.isThereStockInStoreHouse() == false){
            if(this.stockManufacturedMaterials > 0){
                if(this.stockPrimeMaterials == 0){
                    if(this.entrepreneurCalledToCollectProducts == false){
                        this.STOREMONITOR.callEntrepreneurToCollectProducts(ID);
                    }
                }
            }
        }
        
        if(this.LOGMONITOR.isThereStockInStoreHouse() == false){
            return this.stockPrimeMaterials != 0;
        }
        return true;
    }    
    
    /**
     *
     * @param ID
     */
    @Override
    public void endOperationCraftsman(int ID) {
        this.LOGMONITOR.reportStatusCraftman();
    }
}
