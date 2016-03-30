/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store.monitor;

import utils.Combo;
import java.util.LinkedList;
import java.util.Queue;
import interfaces.StoreLogInterface;
import interfaces.StoreRemoteInterfacesWrapper;
import java.rmi.RemoteException;


/**
 * StoreMonitor e uma classe monitor que gere recursos partilhados, cujo 
 * acesso e concorrente, tais como:
 * <ul>
 * <li>Quantidade de produtos disponíveis na loja</li>
 * <li>Quantidade de vendidos na loja</li>
 * </ul>
 * Esta classe esta preparada para ambientes multi-threaded.
 * Os objetos thread que interagem com esta classe sao:
 * <ul>
 * <li><code>CustomerThread</code></li>
 * <li><code>EntrepreneurThread</code></li>
 * </ul>
 * 
 * @author Luis Assuncao
 * @author Joel Pinheiro
 * @version %I%, %G%
 * @since 1.0
 */
public class StoreMonitor implements StoreRemoteInterfacesWrapper{
    private int numberOfAvailableProducts;   
    private int customersInStore;
    private boolean isDoorOpen;
    private boolean primeMaterialAreNeeded;
    private boolean callCollectProducts;
    private Queue<Integer> customersThatWantToBuyQueue;
    private final StoreLogInterface LOGMONITOR;
    private int[] accumulatedBoughtProducts;
    private int[] amountToBuyCustomer;
    
    private int[] vectorClock = new int[0];
    
    /**
     * Construtor da classe monitor <code>StoreMonitor</code>.
     * Constroi um novo StoreMonitor capaz de gerir e manter o sincronismo entre varias instancias do tipo
     * <code>CustomerThread</code> e <code>EntrepreneurThread</code>.
     * 
     * @param logMonitor Uma instancia de <code>LogMonitor</code> para se poder reportar as alterações do estado interno.
     * @param sizeCustomers Quantidade de objetos do tipo <code>CustomerThread</code> que vao interagir com este monitor.
     * @param vectorClock
     * @see <code>EntrepreneurLogInterface</code> 
     * @see <code>EntrepreneurStoreInterface</code>
     * @see <code>CustomerLogInterface</code> 
     * @see <code>CustomerInterface</code>
     * @since 1.0
     */    
    public StoreMonitor(StoreLogInterface logMonitor, int sizeCustomers, int[] vectorClock) throws RemoteException {
        this.numberOfAvailableProducts = 0;
        this.customersInStore = 0;
        this.isDoorOpen = true;
        this.primeMaterialAreNeeded = false;
        this.customersThatWantToBuyQueue = new LinkedList<Integer>(){};
        this.primeMaterialAreNeeded = false;
        this.callCollectProducts = false;
        this.LOGMONITOR = logMonitor;
        this.accumulatedBoughtProducts = new int[sizeCustomers];
        this.amountToBuyCustomer = new int[sizeCustomers];
        
        for(int i = 0; i < sizeCustomers; i++){
            this.accumulatedBoughtProducts[i] = 0;
        }
        
        for(int i = 0; i < this.LOGMONITOR.getSizeCustomers(this.vectorClock); i++){
            this.amountToBuyCustomer[i] = 0;
        }
        
        this.vectorClock = vectorClock;

    }

    /**
     * Mudança de estado.
     * @param state state state
     */
    @Override
    public synchronized Combo prepareToWork(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        this.LOGMONITOR.reportStatusEntrepreneur(state, this.vectorClock);
        return combo;
    }
    
    /**
     * Operação bloqueante em que a dona procura o que fazer.
     * 
     * @param state state state
     * @return return result  próxima tarefa da dona
     */
    @Override
    public synchronized Combo appraiseSit(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
           
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        while(true){
            try {     
                if (this.LOGMONITOR.areCustomersDead(this.vectorClock)
                        && this.LOGMONITOR.areCraftmenDeath(this.vectorClock)) {
                    combo = new Combo('E', this.vectorClock);
                    return combo;
                }
                
                if (this.callCollectProducts || this.primeMaterialAreNeeded
                        || this.customersThatWantToBuyQueue.size() != 0) {
                    break;
                }

                if (this.LOGMONITOR.areCraftmenDeath(this.vectorClock) && this.LOGMONITOR.getStockFinnishedProducts(this.vectorClock) != 0) {
                    break;
                }
                
                wait();
            } catch (InterruptedException ex) {
            }
        }
        
        
        if(this.customersThatWantToBuyQueue.size() != 0 && this.numberOfAvailableProducts > 0)
        {
            combo = new Combo('C', this.vectorClock);
            return combo;
        }
        
        if(this.callCollectProducts || (this.LOGMONITOR.areCraftmenDeath(this.vectorClock) && this.LOGMONITOR.getStockFinnishedProducts(this.vectorClock) != 0)){
            combo = new Combo('T', this.vectorClock);
            return combo;
        }

        if(this.primeMaterialAreNeeded){
            combo = new Combo('M', this.vectorClock);
            return combo;
        }
        
        combo = new Combo(' ', this.vectorClock);
        return combo;
    }

    /**
     * Mudança de estado.
     * @param state state state
     */
    @Override
    public synchronized Combo addressCustomer(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.LOGMONITOR.reportStatusEntrepreneur(state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }
    
    /**
     * Mudança de estado.
     * @param state state state
     */
    @Override
    public synchronized Combo sayGoodByeToCustomer(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.LOGMONITOR.reportStatusEntrepreneur(state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }

    /**
     * Mudança de estado da dona, loja e porta.
     * @param state state state
     */
    @Override
    public synchronized Combo prepareToLeave(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.isDoorOpen = false;
        this.LOGMONITOR.setStoreState("CLSD", this.vectorClock);     
        this.LOGMONITOR.reportStatusEntrepreneur(state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;        
    }
    
    /**
     * Adiciona produtos à loja.
     * 
     * @param amountProductsToPickUpFromWorkshop    produtos a apanhar da oficina.
     * @param state state
     */
    @Override
    public synchronized Combo addProducts(int amountProductsToPickUpFromWorkshop, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.numberOfAvailableProducts += amountProductsToPickUpFromWorkshop;
        
        this.LOGMONITOR.setGoodsInDisplay(numberOfAvailableProducts, this.vectorClock);
        this.LOGMONITOR.reportStatusEntrepreneur(state, this.vectorClock);
        
        if(this.LOGMONITOR.areCraftmenDeath(this.vectorClock) && this.LOGMONITOR.getStockFinnishedProducts(this.vectorClock) != 0) {
            
        } 
        else
            resetCallCollectProducts();
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }    

    /**
     * Mudança de estado.
     * @param state state state
     */
    @Override
    public synchronized Combo visitSuppliers(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.LOGMONITOR.reportStatusEntrepreneur(state,this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }

    /**
     * Mudança de estado.
     * @param state state
     */
    @Override
    public synchronized Combo replenishStock(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.primeMaterialAreNeeded = false;
        
        this.LOGMONITOR.setCallTransferPrimeMaterials(this.primeMaterialAreNeeded, this.vectorClock);
        this.LOGMONITOR.reportStatusEntrepreneur(state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }    
    
    /**
     * Mudança de estado.
     * @param state state state
     */
    @Override
    public synchronized Combo returnToShop(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.isDoorOpen = true;
        
        this.LOGMONITOR.setStoreState("OPEN", this.vectorClock);
        this.LOGMONITOR.reportStatusEntrepreneur(state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }    
    
    /**
     * Verifica se existem clientes na loja.
     * 
     * @param state state
     * @return return result  se existem clientes na loja
     */
    @Override
    public synchronized Combo customerInShop(String state, int[] vectorClock, int vectorClockPosition) {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);

        //return (this.customersInStore != 0);
        
        combo = new Combo((this.customersInStore != 0), this.vectorClock);
        return combo;
    }
    
    /**
     * Mudança de estado.
     * @param id id id
     * @param state state state
     * @param vectorClock
     * @param vectorClockPosition
     * @return 
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized Combo goShopping(int id, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.LOGMONITOR.reportStatusCustomer(id, state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }    
    
    /**
     * Mudança de estado.
     * @param state state state
     * @param vectorClock
     * @param vectorClockPosition
     * @return 
     * @throws java.rmi.RemoteException
     */    
    @Override
    public synchronized Combo closeTheDoor(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {

        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.isDoorOpen = false;
        this.LOGMONITOR.setStoreState("CLSD", this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }
    
    /**
     * Verifica se a porta está aberta.
     * 
     * @return return result se a porta está aberta
     */
    @Override
    public synchronized Combo isDoorOpen(int[] vectorClock, int vectorClockPosition) {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        combo = new Combo(this.isDoorOpen, this.vectorClock);
        return combo;
    }
 
    /**
     * Operação de um cliente entrar na loja.
     * 
     * @param id id    ID do cliente que entrou na loja.
     * @param state state
     */
    @Override
    public synchronized Combo enterShopCustomer(int id, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.customersInStore++;
        
        this.LOGMONITOR.setCustomersInside(this.customersInStore, this.vectorClock);
        this.LOGMONITOR.reportStatusCustomer(id, state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }    
    
    /**
     * Operação em que o cliente se coloca na fila de espera e diz quantos produtos quer.
     * 
     * @param id id ID do cliente
     * @param numberOfProductsToBuy produtos que quer comprar
     * @param state state state
     */
    @Override
    public synchronized Combo iWantThis(int id, int numberOfProductsToBuy, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        
        if(this.LOGMONITOR.areCraftmenDeath(this.vectorClock) && this.numberOfAvailableProducts == 0)
            return combo;
        
        if((this.numberOfAvailableProducts > numberOfProductsToBuy) ||
                (this.LOGMONITOR.areCraftmenDeath(this.vectorClock)) )
        {   
            if(numberOfProductsToBuy == 2 && this.numberOfAvailableProducts == 1 && this.LOGMONITOR.areCraftmenDeath(this.vectorClock))
                numberOfProductsToBuy = this.numberOfAvailableProducts;
            
            if(numberOfProductsToBuy == 1 && this.numberOfAvailableProducts == 0 && this.LOGMONITOR.areCraftmenDeath(this.vectorClock))
                numberOfProductsToBuy = this.numberOfAvailableProducts;
            
            this.LOGMONITOR.reportStatusCustomer(id, state, this.vectorClock);

            this.customersThatWantToBuyQueue.add(id);

            this.amountToBuyCustomer[id] = numberOfProductsToBuy;

            notifyAll();

            do{
                try {
                    wait();
                } catch (InterruptedException ex) {

                }
            }while(this.customersThatWantToBuyQueue.contains(id));
        }
        
        return combo;
    }

    /**
     * Operação em que o cliente sinaliza que quer sair da loja.
     * 
     * @param id id ID do cliente que quer sair
     * @param state state
     */
    @Override
    public synchronized Combo exitShopCustomer(int id, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.customersInStore--;
        //TODO: verificar isto
        notifyAll();
        
        this.LOGMONITOR.setCustomersInside(this.customersInStore, this.vectorClock);
        this.LOGMONITOR.reportStatusCustomer(id, state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }

    /**
     * Sinaliza que são necessárias matérias primas e acorda quem está actualmente no monitor.
     * 
     * @param ID ID ID do artesão que fez a chamada.
     */
    @Override
    public synchronized Combo needMorePrimeMaterials(int ID, int[] vectorClock, int vectorClockPosition) {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.primeMaterialAreNeeded = true;
        notifyAll();
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }

    /**
     * Sinaliza a dona que é preciso vir apanhar produtos.
     * 
     * @param ID ID ID do artesão que fez a chamada
     */
    @Override
    public synchronized Combo callEntrepreneurToCollectProducts(int ID, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.callCollectProducts = true;
        notifyAll();
        
        this.LOGMONITOR.setCallTransferProducts(true, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }    
    
    /**
     * Sinaliza que a operação está concluída.
     */
    private Combo resetCallCollectProducts() throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.callCollectProducts = false;
        
        this.LOGMONITOR.setCallTransferProducts(false, this.vectorClock);  
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }
    


    /**
     * Operação de servir o primeiro cliente da fila de espera.
     * @param state state state
     */
    @Override
    public synchronized Combo serviceCustomer(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        while(this.customersThatWantToBuyQueue.size() != 0){
            int customerId = this.customersThatWantToBuyQueue.peek();
            int numberOfProductsToBuy = this.amountToBuyCustomer[customerId];
            
            if(this.numberOfAvailableProducts >= numberOfProductsToBuy){
                
                this.numberOfAvailableProducts -= numberOfProductsToBuy;

                customerId = this.customersThatWantToBuyQueue.remove();
                                
                this.accumulatedBoughtProducts[customerId] += numberOfProductsToBuy;
                

                this.LOGMONITOR.setGoodsInDisplay(this.numberOfAvailableProducts, this.vectorClock);
                
                this.LOGMONITOR.addCustomerAccumulatedBoughtGoods(customerId, this.accumulatedBoughtProducts[customerId], this.vectorClock);       
                
                notifyAll();
            }
            else{
                customerId = this.customersThatWantToBuyQueue.remove();
                notifyAll();
            }
        }
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }

    /**
     * Operação que verifica o número de produtos disponíveis na loja.
     * 
     * @return return result número de produtos disponíveis na loja
     */
    @Override
    public synchronized Combo perusingAround(int[] vectorClock, int vectorClockPosition) {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        combo = new Combo((this.numberOfAvailableProducts == 0), this.vectorClock);
        return combo;
    }

    /**
     * Fim de vida da dona.
     * 
     * @param state state
     */
    @Override
    public synchronized Combo endOperationEntrepreneur(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        this.LOGMONITOR.setStoreState("CLSD", this.vectorClock);
        this.LOGMONITOR.reportStatusEntrepreneur(state, this.vectorClock);

        combo = new Combo(null, this.vectorClock);
        return combo;
        
    }

    /**
     * Fim do ciclo de vida do cliente.
     * 
     * @param id id ID do cliente que finaliza o ciclo de vida
     * @param insideTheStore número de clientes na loja
     * @param state state state
     */
    @Override
    public synchronized Combo endOperationCustomer(int id, boolean insideTheStore, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        notifyAll();    // acordar a dona para ela se aperceber que todos morreram
        this.LOGMONITOR.reportStatusCustomer(id, state, this.vectorClock);
        
        combo = new Combo(null, this.vectorClock);
        return combo;
    }    
    
    /**
     *
     * @param ID ID ID
     * @param insideTheStore insideTheStore
     * @param state state state
     * @return return result
     */
    @Override
    public Combo canEndOperationCustomer(int ID, boolean insideTheStore, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        boolean isThereProductsInStore = this.numberOfAvailableProducts > 0;
        boolean isTherePrimeMaterialStorage = this.LOGMONITOR.isThereStockInStoreHouse(this.vectorClock);
        boolean isTherePrimeMaterialInWorkshop = this.LOGMONITOR.isTherePrimeMaterialInWorkshop(this.vectorClock);
        boolean isThereProductsInWorkshop = this.LOGMONITOR.isThereProductsInWotkshop(this.vectorClock);
        boolean areCraftsmanDead = this.LOGMONITOR.areCraftmenDeath(this.vectorClock);
        
        if(isTherePrimeMaterialStorage == false){
            if(isTherePrimeMaterialInWorkshop == false){
                if(isThereProductsInWorkshop == false) {
                    if (isThereProductsInStore == false) {
                        if (areCraftsmanDead) {
                            combo = new Combo((insideTheStore == false), this.vectorClock);
                            return combo;
                        }
                        combo = new Combo(false, this.vectorClock);
                        return combo;
                    }
                    combo = new Combo(false, this.vectorClock);
                    return combo;
                }
                combo = new Combo(false, this.vectorClock);
                return combo;
            }
            combo = new Combo(false, this.vectorClock);
            return combo;
        }
        combo = new Combo(false, this.vectorClock);
        return combo;    
    }
}
