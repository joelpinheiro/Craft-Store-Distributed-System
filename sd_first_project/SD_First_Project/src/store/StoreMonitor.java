/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import java.util.LinkedList;
import java.util.Queue;
import store.intefaces.CustomerStoreInterface;
import store.intefaces.EntrepreneurStoreInterface;
import log.interfaces.StoreLogInterface;
import store.intefaces.WorkshopStoreInterface;


/**
 * StoreMonitor e uma classe monitor que gere recursos partilhados, cujo 
 * acesso e concorrente, tais como:
 * <ul>
 * <li>Quantidade de produtos disponíveis na loja</li>
 * <li>Quantidade de vendidos na loja</li>
 * </ul>
 * <p></p>
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
public class StoreMonitor implements EntrepreneurStoreInterface, 
        CustomerStoreInterface, WorkshopStoreInterface{
    private int numberOfAvailableProducts;   
    private int customersInStore;
    private boolean isDoorOpen;
    private boolean primeMaterialAreNeeded;
    private boolean callCollectProducts;
    private Queue<Integer> customersThatWantToBuyQueue;
    private final StoreLogInterface LOGMONITOR;
    private int[] accumulatedBoughtProducts;
    private int[] amountToBuyCustomer;
    
    /**
     * Construtor da classe monitor <code>StoreMonitor</code>.
     * Constroi um novo StoreMonitor capaz de gerir e manter o sincronismo entre varias instancias do tipo
     * <code>CustomerThread</code> e <code>EntrepreneurThread</code>.
     * 
     * @param logMonitor Uma instancia de <code>LogMonitor</code> para se poder reportar as alterações do estado interno.
     * @param sizeCustomers Quantidade de objetos do tipo <code>CustomerThread</code> que vao interagir com este monitor.
     * @see <code>EntrepreneurLogInterface</code> 
     * @see <code>EntrepreneurStoreInterface</code>
     * @see <code>CustomerLogInterface</code> 
     * @see <code>CustomerInterface</code>
     * @since 1.0
     */    
    public StoreMonitor(StoreLogInterface logMonitor, int sizeCustomers) {
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
        
        for(int i = 0; i < this.LOGMONITOR.getSizeCustomers(); i++){
            this.amountToBuyCustomer[i] = 0;
        }
    }

    /**
     * Mudança de estado.
     */
    @Override
    public synchronized void prepareToWork() {
        this.LOGMONITOR.reportStatusEntrepreneur();
    }
    
    /**
     * Operação bloqueante em que a dona procura o que fazer.
     * 
     * @return  próxima tarefa da dona
     */
    @Override
    public synchronized char appraiseSit() {
        while(true){
            try {     
                if (this.LOGMONITOR.areCustomersDead()
                        && this.LOGMONITOR.areCraftmenDeath()) {
                    return 'E';
                }
                
                if (this.callCollectProducts || this.primeMaterialAreNeeded
                        || this.customersThatWantToBuyQueue.size() != 0) {
                    break;
                }

                if (this.LOGMONITOR.areCraftmenDeath() && this.LOGMONITOR.getStockFinnishedProducts() != 0) {
                    break;
                }
                
                wait();
            } catch (InterruptedException ex) {
            }
        }
        
        
        if(this.customersThatWantToBuyQueue.size() != 0 && this.numberOfAvailableProducts > 0)
        {
            return 'C';
        }
        
        if(this.callCollectProducts || (this.LOGMONITOR.areCraftmenDeath() && this.LOGMONITOR.getStockFinnishedProducts() != 0)){
            return 'T';
        }

        if(this.primeMaterialAreNeeded){
            return 'M';
        }
        
        
        return ' ';
    }

    /**
     * Mudança de estado.
     */
    @Override
    public synchronized void addressCustomer() {
        this.LOGMONITOR.reportStatusEntrepreneur();
    }
    
    /**
     * Mudança de estado.
     */
    @Override
    public synchronized void sayGoodByeToCustomer() {
        this.LOGMONITOR.reportStatusEntrepreneur();
    }

    /**
     * Mudança de estado da dona, loja e porta.
     */
    @Override
    public synchronized void prepareToLeave() {
        this.isDoorOpen = false;
        this.LOGMONITOR.setStoreState("CLSD");     
        this.LOGMONITOR.reportStatusEntrepreneur();
    }
    
    /**
     * Adiciona produtos à loja.
     * 
     * @param amountProductsToPickUpFromWorkshop    produtos a apanhar da oficina.
     */
    @Override
    public synchronized void addProducts(int amountProductsToPickUpFromWorkshop) {
        this.numberOfAvailableProducts += amountProductsToPickUpFromWorkshop;
        
        this.LOGMONITOR.setGoodsInDisplay(numberOfAvailableProducts);
        this.LOGMONITOR.reportStatusEntrepreneur();
        
        if(this.LOGMONITOR.areCraftmenDeath() && this.LOGMONITOR.getStockFinnishedProducts() != 0) {
            
        } 
        else
            resetCallCollectProducts();
    }    

    /**
     * Mudança de estado.
     */
    @Override
    public synchronized void visitSuppliers() {
        this.LOGMONITOR.reportStatusEntrepreneur();
    }

    /**
     * Mudança de estado.
     */
    @Override
    public synchronized void replenishStock() {
        this.primeMaterialAreNeeded = false;
        
        this.LOGMONITOR.setCallTransferPrimeMaterials(this.primeMaterialAreNeeded);
        this.LOGMONITOR.reportStatusEntrepreneur();
    }    
    
    /**
     * Mudança de estado.
     */
    @Override
    public synchronized void returnToShop() {
        this.isDoorOpen = true;
        
        this.LOGMONITOR.setStoreState("OPEN");
        this.LOGMONITOR.reportStatusEntrepreneur();
    }    
    
    /**
     * Verifica se existem clientes na loja.
     * 
     * @return  se existem clientes na loja
     */
    @Override
    public synchronized boolean customerInShop() {
        return (this.customersInStore != 0);
    }
    
    /**
     * Mudança de estado.
     * @param id
     */
    @Override
    public synchronized void goShopping(int id) {
        this.LOGMONITOR.reportStatusCustomer();
    }    
    
    /**
     * Mudança de estado.
     */    
    @Override
    public synchronized void closeTheDoor() {

        this.isDoorOpen = false;
        this.LOGMONITOR.setStoreState("CLSD");
    }
    
    /**
     * Verifica se a porta está aberta.
     * 
     * @return se a porta está aberta
     */
    @Override
    public synchronized boolean isDoorOpen() {

        return this.isDoorOpen;
    }
 
    /**
     * Operação de um cliente entrar na loja.
     * 
     * @param id    ID do cliente que entrou na loja.
     */
    @Override
    public synchronized void enterShopCustomer(int id) {
        
        this.customersInStore++;
        
        this.LOGMONITOR.setCustomersInside(this.customersInStore);
        this.LOGMONITOR.reportStatusCustomer();
    }    
    
    /**
     * Operação em que o cliente se coloca na fila de espera e diz quantos produtos quer.
     * 
     * @param id ID do cliente
     * @param numberOfProductsToBuy produtos que quer comprar
     */
    @Override
    public synchronized void iWantThis(int id, int numberOfProductsToBuy) {
        if(this.LOGMONITOR.areCraftmenDeath() && this.numberOfAvailableProducts == 0)
            return;
        
        if((this.numberOfAvailableProducts > numberOfProductsToBuy) ||
                (this.LOGMONITOR.areCraftmenDeath()) )
        {   
            if(numberOfProductsToBuy == 2 && this.numberOfAvailableProducts == 1 && this.LOGMONITOR.areCraftmenDeath())
                numberOfProductsToBuy = this.numberOfAvailableProducts;
            
            if(numberOfProductsToBuy == 1 && this.numberOfAvailableProducts == 0 && this.LOGMONITOR.areCraftmenDeath())
                numberOfProductsToBuy = this.numberOfAvailableProducts;
            
            this.LOGMONITOR.reportStatusCustomer();

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
    }

    /**
     * Operação em que o cliente sinaliza que quer sair da loja.
     * 
     * @param id ID do cliente que quer sair
     */
    @Override
    public synchronized void exitShopCustomer(int id) {
        this.customersInStore--;
        //TODO: verificar isto
        notifyAll();
        
        this.LOGMONITOR.setCustomersInside(this.customersInStore);
        this.LOGMONITOR.reportStatusCustomer();
    }

    /**
     * Sinaliza que são necessárias matérias primas e acorda quem está actualmente no monitor.
     * 
     * @param ID ID do artesão que fez a chamada.
     */
    @Override
    public synchronized void needMorePrimeMaterials(int ID) {
        this.primeMaterialAreNeeded = true;
        notifyAll();
    }

    /**
     * Sinaliza a dona que é preciso vir apanhar produtos.
     * 
     * @param ID ID do artesão que fez a chamada
     */
    @Override
    public synchronized void callEntrepreneurToCollectProducts(int ID) {
        this.callCollectProducts = true;
        notifyAll();
        
        this.LOGMONITOR.setCallTransferProducts(true);
    }    
    
    /**
     * Sinaliza que a operação está concluída.
     */
    private void resetCallCollectProducts() {
        this.callCollectProducts = false;
        
        this.LOGMONITOR.setCallTransferProducts(false);  
    }
    


    /**
     * Operação de servir o primeiro cliente da fila de espera.
     */
    @Override
    public synchronized void serviceCustomer() {
        while(this.customersThatWantToBuyQueue.size() != 0){
            int customerId = this.customersThatWantToBuyQueue.peek();
            int numberOfProductsToBuy = this.amountToBuyCustomer[customerId];
            
            if(this.numberOfAvailableProducts >= numberOfProductsToBuy){
                
                this.numberOfAvailableProducts -= numberOfProductsToBuy;

                customerId = this.customersThatWantToBuyQueue.remove();
                                
                this.accumulatedBoughtProducts[customerId] += numberOfProductsToBuy;
                

                this.LOGMONITOR.setGoodsInDisplay(this.numberOfAvailableProducts);
                
                this.LOGMONITOR.addCustomerAccumulatedBoughtGoods(customerId, this.accumulatedBoughtProducts[customerId]);       
                
                notifyAll();
            }
            else{
                customerId = this.customersThatWantToBuyQueue.remove();
                notifyAll();
            }
        }
    }

    /**
     * Operação que verifica o número de produtos disponíveis na loja.
     * 
     * @return número de produtos disponíveis na loja
     */
    @Override
    public synchronized boolean perusingAround() {
        return this.numberOfAvailableProducts == 0;
    }

    /**
     * Fim de vida da dona.
     * 
     */
    @Override
    public synchronized void endOperationEntrepreneur() {
        this.LOGMONITOR.setStoreState("CLSD");
        this.LOGMONITOR.reportStatusEntrepreneur();
        
    }

    /**
     * Fim do ciclo de vida do cliente.
     * 
     * @param id ID do cliente que finaliza o ciclo de vida
     * @param insideTheStore número de clientes na loja
     */
    @Override
    public synchronized void endOperationCustomer(int id, boolean insideTheStore) {
        notifyAll();    // acordar a dona para ela se aperceber que todos morreram
        this.LOGMONITOR.reportStatusCustomer();
    }    
    
    /**
     *
     * @param ID
     * @param insideTheStore
     * @return
     */
    @Override
    public boolean canEndOperationCustomer(int ID, boolean insideTheStore) {
        boolean isThereProductsInStore = this.numberOfAvailableProducts > 0;
        boolean isTherePrimeMaterialStorage = this.LOGMONITOR.isThereStockInStoreHouse();
        boolean isTherePrimeMaterialInWorkshop = this.LOGMONITOR.isTherePrimeMaterialInWorkshop();
        boolean isThereProductsInWorkshop = this.LOGMONITOR.isThereProductsInWotkshop();
        boolean areCraftsmanDead = this.LOGMONITOR.areCraftmenDeath();
        
        if(isTherePrimeMaterialStorage == false){
            if(isTherePrimeMaterialInWorkshop == false){
                if(isThereProductsInWorkshop == false) {
                    if (isThereProductsInStore == false) {
                        if (areCraftsmanDead) {
                            return insideTheStore == false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;    
    }
}
