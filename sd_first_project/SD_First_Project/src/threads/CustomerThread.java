/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import store.intefaces.CustomerStoreInterface;

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

    /**
     * Instanciação do thread cliente.
     * 
     * @param ID
     * @param storeMonitor
     */
    public CustomerThread(int ID, CustomerStoreInterface storeMonitor) {
        this.ID = ID;
        this.STOREMONITOR = storeMonitor;
        this.insideTheStore = false;
        this.exitShop = false;
        this.state = "CODC";
    }
    
    /**
    *  Ciclo de vida do thread cliente.
    */  
    @Override
    public void run() {
        boolean doorOpen;
        
        do
        {
            this.state = "CODC";
            livingNormalLife();
            
            this.state = "CSDO";
            this.STOREMONITOR.goShopping(this.ID);
            
            this.state = "CSDO";
            doorOpen = this.STOREMONITOR.isDoorOpen();
            while(doorOpen)                                                   
            {
                this.state = "AOID";
                this.STOREMONITOR.enterShopCustomer(this.ID);
                this.insideTheStore = true;
                
                this.state = "AOID";
                this.exitShop = this.STOREMONITOR.perusingAround();
                lookAround();
                
                if(this.exitShop){
                    this.state = "CODC";
                    this.STOREMONITOR.exitShopCustomer(this.ID);
                    this.insideTheStore = false;
                    break;
                }

                int numberOfProductsToBuy = NumberOfGoodsToBuy();
                
                this.state = "BSG ";
                this.STOREMONITOR.iWantThis(this.ID, numberOfProductsToBuy); 
                
                this.state = "CODC";
                this.STOREMONITOR.exitShopCustomer(this.ID);
                this.insideTheStore = false;
                
                break;
                        
            }
            
            //exitStore(); 
            
            
        } while(!this.endOperationCustomer(this.ID, this.insideTheStore));   
    } 
    
    /**
     * Operação que retorna o número de produtos a comprar pelo cliente.
     * 70% - 1 produtos.
     * 30% - 2 produtos.
     * 
     * @return 
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
     * @return 
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
     * @return
     */
    public synchronized int getCurrentId() {
        return this.ID;
    }

    /**
     *
     * @return
     */
    public synchronized String getCurrentState() {
        return this.state;
    }

    private boolean endOperationCustomer(int ID, boolean insideTheStore) {
        if(this.STOREMONITOR.canEndOperationCustomer(this.ID, this.insideTheStore)){
            this.state = "DIED";
            this.STOREMONITOR.endOperationCustomer(this.ID, this.insideTheStore);
            return true;
        }
        else {
            return false;
        }
    }
}
