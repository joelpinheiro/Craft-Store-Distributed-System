/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop.monitor;

import utils.Combo;
import interfaces.WorkshopLogInterface;
import interfaces.WorkshopStoreInterface;
import interfaces.WorkshopRemoteInterfacesWrapper;
import java.rmi.RemoteException;

/**
 * WorkshopMonitor e uma classe monitor que gere recursos partilhados, cujo 
 * acesso e concorrente, tais como:
 * <ul>
 * <li>Quantidade de materias primas em stock, atualmente, na oficina</li>
 * <li>Quantidade de produtos atualmente fabricados na oficina</li>
 * </ul>
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

public class WorkshopMonitor implements WorkshopRemoteInterfacesWrapper{
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
    
    int[] vectorClock; 
    int vectorClockPosition;
    
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
            WorkshopStoreInterface storeMonitor, int craftsmanSize, int[] vectorClock){
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
        
        this.vectorClock = vectorClock;
    }

    /**
     *
     * @param ID ID
     * @param amountToCollect amountToCollect
     * @param state state
     * @param vectorClock
     * @return return result Se a operacao teve sucesso
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo collectMaterials(int ID, int amountToCollect, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        if(this.stockPrimeMaterials >= amountToCollect){
            this.stockPrimeMaterials -= amountToCollect;
            this.LOGMONITOR.setStockPrimeMaterials(this.stockPrimeMaterials, this.vectorClock);
            
            
            combo = new Combo(true, this.vectorClock);
            return combo;
        }
        
        this.LOGMONITOR.setStockPrimeMaterials(this.stockPrimeMaterials, this.vectorClock);
        
        combo = new Combo(false, this.vectorClock);
        return combo;
    }

    /**
     *
     * @param ID ID
     * @param state state
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo primeMaterialsNeeded(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        if(this.entrepreneurCalledReplenishPrimeMaterials == false){
            vectorClock[vectorClockPosition]++;
            combo = this.STOREMONITOR.needMorePrimeMaterials(ID, vectorClock, vectorClockPosition);
            this.vectorClock = combo.getVectorClock().clone();
            this.entrepreneurCalledReplenishPrimeMaterials = true;
            this.LOGMONITOR.setCallTransferPrimeMaterials(this.entrepreneurCalledReplenishPrimeMaterials, this.vectorClock);
        }

        this.LOGMONITOR.reportStatusCraftman(ID, state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }

    /**
     *
     * @param ID ID
     * @param state state
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo batchReadyForTransfer(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.STOREMONITOR.callEntrepreneurToCollectProducts(ID, vectorClock, vectorClockPosition);
            this.entrepreneurCalledToCollectProducts = true;
            
        //this.LOGMONITOR.reportStatusCraftman(ID, "CTE ");
        this.LOGMONITOR.reportStatusCraftman(ID, state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }

    /**
     *
     * @param ID ID
     * @param amountToCollect amountToCollect
     * @param state state
     * @return return result
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo checkForMaterials(int ID, int amountToCollect, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);

        //this.LOGMONITOR.reportStatusCraftman(ID, "FPMS");
        this.LOGMONITOR.reportStatusCraftman(ID, state, this.vectorClock);
        
        while(true){
            try {
                if(!this.entrepreneurCalledReplenishPrimeMaterials)
                    break;
                
                wait();
            } catch (InterruptedException ex) {
            }
        }

        boolean enoughPrimeMaterialsHere = this.stockPrimeMaterials > amountToCollect;
        
        boolean enoughStorageStock = this.LOGMONITOR.isThereStockInStoreHouse(this.vectorClock);
        
        if(enoughStorageStock == false)
        {
            combo = new Combo(true, this.vectorClock);
            return combo;
        }

        combo = new Combo(enoughPrimeMaterialsHere, this.vectorClock);
        return combo;
            
    }
    
    /**
     *
     * @param amountPrimeMAterials amountPrimeMAterials
     * @param state state
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo addPrimeMaterial(int amountPrimeMAterials, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {

        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.stockPrimeMaterials += amountPrimeMAterials;
        this.primeMaterialStockResuply++;
        this.accumulatedSupliedPrimeMaterials += amountPrimeMAterials;
        
        this.entrepreneurCalledReplenishPrimeMaterials = false;
        this.LOGMONITOR.setCallTransferPrimeMaterials(this.entrepreneurCalledReplenishPrimeMaterials, this.vectorClock);
        this.LOGMONITOR.setStockPrimeMaterials(this.stockPrimeMaterials, this.vectorClock);
        this.LOGMONITOR.setResuplyPrimeMaterials(this.primeMaterialStockResuply, this.vectorClock);
        this.LOGMONITOR.setTotalAmountPrimeMaterialsSupplied(this.accumulatedSupliedPrimeMaterials, this.vectorClock);
        
        notifyAll();

        combo = new Combo(null, this.vectorClock);
        return combo;
    }
    
    /**
     *
     * @param ID ID
     * @param productStep productStep productStep
     * @param state state
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo prepareToProduce(int ID, int productStep, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.LOGMONITOR.reportStatusCraftman(ID, state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }

    /**
     *
     * @param ID ID
     * @param productStep productStep productStep
     * @param state state
     * @return return result
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo goToStore(int ID, int productStep, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.stockManufacturedMaterials += productStep;
        this.accumulatedProducedProducts += productStep;
        this.craftsmanAccumulatedManufacturedProducts[ID] += productStep;

        this.LOGMONITOR.setStockFinnishedProducts(this.stockManufacturedMaterials, this.vectorClock);
        this.LOGMONITOR.setTotalAmountProductsManufactured(this.accumulatedProducedProducts, this.vectorClock);
        this.LOGMONITOR.addCraftsmanAccManProducts(ID, this.craftsmanAccumulatedManufacturedProducts[ID], this.vectorClock);
        //this.LOGMONITOR.reportStatusCraftman(ID, "SIFT");
        this.LOGMONITOR.reportStatusCraftman(ID, state, this.vectorClock);
        

        combo = new Combo((this.stockManufacturedMaterials >= this.productThreshold && 
                this.entrepreneurCalledToCollectProducts == false), this.vectorClock);
        return combo;
    }

    /**
     *
     * @param ID ID
     * @param state state
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo backToWork(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        if(this.accumulatedProducedProducts == this.productThreshold){
            this.LOGMONITOR.reportStatusCraftman(ID, state, this.vectorClock);
        }
        else{
            this.LOGMONITOR.reportStatusCraftman(ID, state, this.vectorClock);
        }
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }

    /**
     *
     * @param amountProductsToPickUp amountProductsToPickUp
     * @param state state
     * @return return result
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo collectABatchOfProducts(int amountProductsToPickUp, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        if(this.stockManufacturedMaterials >= amountProductsToPickUp)
        {
            this.stockManufacturedMaterials -= amountProductsToPickUp;
            this.LOGMONITOR.setStockFinnishedProducts(this.stockManufacturedMaterials, this.vectorClock);
            this.entrepreneurCalledToCollectProducts = false;
            combo = new Combo(amountProductsToPickUp, this.vectorClock);
            return combo;
        }
        else
        {
            int tmp = this.stockManufacturedMaterials;
            this.stockManufacturedMaterials = 0;
            this.LOGMONITOR.setStockFinnishedProducts(this.stockManufacturedMaterials, this.vectorClock);
            this.entrepreneurCalledToCollectProducts = false;
            combo = new Combo(tmp, this.vectorClock);
            return combo;
        }
    }

    /**
     *
     * @param ID ID
     * @param state state
     * @return return result
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo isThereWorkLeft(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {

        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        if(this.LOGMONITOR.isThereStockInStoreHouse(this.vectorClock) == false){
            if(this.stockManufacturedMaterials > 0){
                if(this.stockPrimeMaterials == 0){
                    if(this.entrepreneurCalledToCollectProducts == false){
                        vectorClock[vectorClockPosition]++;
                        combo = this.STOREMONITOR.callEntrepreneurToCollectProducts(ID, vectorClock, vectorClockPosition);
                        this.vectorClock = combo.getVectorClock().clone();
                    }
                }
            }
        }
        
        if(this.LOGMONITOR.isThereStockInStoreHouse(this.vectorClock) == false){
            combo = new Combo((this.stockPrimeMaterials != 0), this.vectorClock);
            return combo;
        }
        combo = new Combo(true, this.vectorClock);
        return combo;
    }    
    
    /**
     *
     * @param ID ID
     * @param state state
     * @throws java.rmi.RemoteException
     */
    @Override
    public Combo endOperationCraftsman(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.LOGMONITOR.reportStatusCraftman(ID, state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }
}
