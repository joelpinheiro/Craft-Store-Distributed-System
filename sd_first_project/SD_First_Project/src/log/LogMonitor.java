/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log;

import genclass.GenericIO;
import genclass.TextFile;

import log.interfaces.StoreLogInterface;
import log.interfaces.StorageLogInterface;
import log.interfaces.WorkshopLogInterface;
import threads.CraftsmanThread;
import threads.CustomerThread;
import threads.EntrepreneurThread;

/**
 *  Este tipo de dados implementa o log da simulação.
 *  <p>
 *  Solução concorrente baseada em monitores como elemento de sincronização das threads clientes, artesões e dona.
 * 
 * @author luis
 * @author joelpinheiro
 */
public class LogMonitor implements StoreLogInterface, StorageLogInterface,
        WorkshopLogInterface{
    
    // Craftsman
    private String[] crafsmanState;
    private int[] craftsmanAccManProducts;
    
    // Store
    private String storeState;
    private int customersInside;
    private int goodsInDisplay;
    private boolean callTransferProducts;
    private boolean callTransferPrimeMaterials;
    
    // Customer
    private String[] customersState;
    private int[] customerAccumulatedBoughtGoods;
    
    // Workshop
    private int stockPrimeMaterials;
    private int stockFinnishedProducts;
    private int resuplyPrimeMaterials;
    private int totalAmountPrimeMaterialsSupplied;
    private int totalAmountProductsManufactured;
    
    // Entrepreneur
    private String entrepreneurState;
    
    // Storage
    private int StorageHouseStock;
    
    private String fileName;
    private String filePath;
    
    private String previousLine;
    private int maxSpaceToIntegers;

    /**
     * Construtor do monitor de Log.
     * 
     * @param numberOfCustomers     número de clientes máximo da loja
     * @param storageStock          número de matérias primas máximas
     * @param numberOfCraftmen      número de atersões
     * @param fileName              nome do ficheiro a escrever
     * @param filePath              caminho do ficheiro
     * @param maxSpaceToIntegers    máximo de caracteres por inteiro
     */
    public LogMonitor(int numberOfCustomers, int storageStock, int numberOfCraftmen, String fileName, String filePath, int maxSpaceToIntegers) {
        
        this.crafsmanState = new String[numberOfCraftmen];
        this.craftsmanAccManProducts = new int[numberOfCraftmen];
        this.customersState = new String[numberOfCustomers];
        this.customerAccumulatedBoughtGoods = new int[numberOfCustomers];
        
        for(int i = 0; i < numberOfCraftmen ; i++){
            this.crafsmanState[i] = "FPNS";
            this.craftsmanAccManProducts[i] = 0; 
        }
        
        for(int i = 0; i < numberOfCustomers ; i++){
            this.customersState[i] = "CODC";
            this.customerAccumulatedBoughtGoods[i] = 0; 
        }

        this.storeState = "OPEN";
        this.customersInside = 0;
        this.goodsInDisplay = 0;
        this.callTransferProducts = false;
        this.callTransferPrimeMaterials = false;
        this.stockPrimeMaterials = 0;
        this.stockFinnishedProducts = 0;
        this.resuplyPrimeMaterials = 0;
        this.totalAmountPrimeMaterialsSupplied = 0;
        this.totalAmountProductsManufactured = 0;
        this.entrepreneurState = "OTS ";
        this.StorageHouseStock = storageStock;
        this.fileName = fileName;
        this.filePath = filePath;
        this.previousLine = "";
        this.maxSpaceToIntegers = maxSpaceToIntegers;
        
        this.reportInitialStatus();
        this.reportStatus();
    }

    /**
     * Função para dar aos inteiros o mesmo tamanho em texto (evita 'barrigas' no log)
     * @param i inteiro a converter
     * @return  inteiro convertido em texto
     */
    private String s(int i){
        
        String x = String.valueOf(i);
        int result = this.maxSpaceToIntegers - x.length();
        
        for(int k = 0 ; k < result ; k++) x += " ";
        
        return x;
         //TODO: Fazer para os headers
    } 

    /**
     * Reporta estado dos clientes.
     * 
     */
    @Override
    public synchronized void reportStatusCustomer(){
        CustomerThread customer = (CustomerThread)Thread.currentThread();
        
        int currentId = customer.getCurrentId();
        
        String currentState = customer.getCurrentState();
        
        this.customersState[currentId] = currentState;
        
        reportStatus();
    }
    
    /**
     * Reporta estado dos artesões.
     * 
     */
    @Override
    public synchronized void reportStatusCraftman(){
        CraftsmanThread craftsman = (CraftsmanThread)Thread.currentThread();
        
        int currentId = craftsman.getCurrentId();
        
        String currentState = craftsman.getCurrentState();
        
        this.crafsmanState[currentId] = currentState;
        
        reportStatus();
    }
    
    /**
     * Reporta estado da dona.
     * 
     */
    @Override
    public synchronized void reportStatusEntrepreneur(){
        
        EntrepreneurThread entrepreneur = (EntrepreneurThread)Thread.currentThread();
        
        String currentState = entrepreneur.getCurrentState();
        
        this.entrepreneurState = currentState;
        reportStatus();
    }
    
    /**
     * Escreve no ficheiro os headers do log.
     */
    private void reportInitialStatus(){
        TextFile log = new TextFile();
        String header1 = "ENTREPRE ";
        String header2 = "Stat     ";
        
        if(!log.openForWriting(this.filePath, this.fileName)){
            GenericIO.writelnString("A operação de criação do ficheiro " + this.fileName + " falhou!");
            System.exit(1);
        }
        
        for(int i = 0; i < this.customersState.length; i++){
            header1 += " CUST_" + String.valueOf(i) + "   ";
        }
        
        for(int i = 0; i < this.crafsmanState.length; i++){
            header1 += " CRAFT_" + String.valueOf(i) + "  ";
        }
        
        header1 += "               SHOP                    WORKSHOP";
        
        for (String customersState1 : this.customersState) {
            header2 += "Stat  BP  ";
        }
        
        for (String crafsmanState1 : this.crafsmanState) {
            header2 += " Stat PP  ";
        }
        
        header2 += "  Stat NCI   NPI   PCR PMR  APMI   NPI   NSPM   TAPM   TNP";
        
        System.out.println("                 Aveiro Handicraft SARL - Description of the internal state");
        System.out.println(header1);
        System.out.println(header2);
        
        log.writelnString("                 Aveiro Handicraft SARL - Description of the internal state");
        log.writelnString(header1);
        log.writelnString(header2);
        
        if(!log.close()){
            GenericIO.writelnString("A operação de fecho do ficheiro " + this.fileName + " falhou!");
            System.exit(1);
        }
    }
    
    /**
     * Reporta um novo evento no log (escreve no ficheiro).
     */
    private synchronized void reportStatus(){
        
        TextFile log = new TextFile();
        
        String lineStatus = "";
        
        if(!log.openForAppending(this.filePath, this.fileName)){
            GenericIO.writelnString("A operação de criação do ficheiro " + this.fileName + " falhou!");
            System.exit(1);
        }
        
        lineStatus += this.entrepreneurState + "    ";
        
        for(int i = 0; i < this.customersState.length; i++){
            lineStatus += " " + this.customersState[i] + "  " + s(this.customerAccumulatedBoughtGoods[i]);
        }

        lineStatus += " ";
        
        for(int i = 0; i < this.crafsmanState.length; i++){
            lineStatus += " " + this.crafsmanState[i] + "  " + s(this.craftsmanAccManProducts[i]);
        }
        lineStatus += "  " + this.storeState + "   " + s(this.customersInside) + "   " + 
                s(this.goodsInDisplay) + "   " + getC(this.callTransferProducts) + 
                "   " + getC(this.callTransferPrimeMaterials);

        lineStatus += "     " + s(this.stockPrimeMaterials) + "   " + s(this.stockFinnishedProducts) + 
                "    " + s(this.resuplyPrimeMaterials) + 
                "    " + s(this.totalAmountPrimeMaterialsSupplied) + 
                "   " + s(this.totalAmountProductsManufactured);
        
        if(!lineStatus.equals(this.previousLine)) {
            System.out.println(lineStatus);
            log.writelnString(lineStatus);
            
            if(!log.close()){
                GenericIO.writelnString("A operação de fecho do ficheiro " + this.fileName + " falhou!");
                System.exit(1);
            }
        } else
        {
            if(!log.close()){
                GenericIO.writelnString("A operação de fecho do ficheiro " + this.fileName + " falhou!");
                System.exit(1);
            } 
        }
        this.previousLine = lineStatus;
        
    }
    
    /**
     * Retorna valor do booleano (T ou F).
     * 
     * @param x booleano de entrada
     * @return  texto de saída (T ou F)
     */
    private String getC(boolean x){
        if(x) return "T";
        else return "F";
    }
    
    /**
     * Altera o estado da loja.
     * 
     * @param storeState    estado da loja
     */
    @Override
    public synchronized void setStoreState(String storeState) {
        this.storeState = storeState;
    }

    /**
     * Altera os clientes que estão dentro da loja.
     * 
     * @param customersInside   número de clientes na loja
     */
    @Override
    public synchronized void setCustomersInside(int customersInside) {
        this.customersInside = customersInside;
    }

    /**
     * Altera os bens que estão disponíveis na loja.
     * 
     * @param goodsInDisplay    bens na loja
     */
    @Override
    public synchronized void setGoodsInDisplay(int goodsInDisplay) {
        this.goodsInDisplay = goodsInDisplay;
    }

    /**
     * Chamada para ir buscar produtos.
     * 
     * @param callTransferProducts  booleano da chamada
     */
    @Override
    public synchronized void setCallTransferProducts(boolean callTransferProducts) {
        this.callTransferProducts = callTransferProducts;
    }

    /**
     * Chamada para ir buscar matérias primas.
     * 
     * @param callTransferPrimeMaterials    booleano da chamada.
     */
    @Override
    public synchronized void setCallTransferPrimeMaterials(boolean callTransferPrimeMaterials) {
        this.callTransferPrimeMaterials = callTransferPrimeMaterials;
    }

    /**
     * Altera o stock das matérias primas.
     * 
     * @param stockPrimeMaterials   stock das matérias primas
     */
    @Override
    public synchronized void setStockPrimeMaterials(int stockPrimeMaterials) {
        this.stockPrimeMaterials = stockPrimeMaterials;
    }

    /**
     * Altera o stock dos produtos acabados.
     * 
     * @param stockFinnishedProducts    stock dos produtos acabados
     */
    @Override
    public synchronized void setStockFinnishedProducts(int stockFinnishedProducts) {
        this.stockFinnishedProducts = stockFinnishedProducts;
    }

    /**
     * Altera o número de fornecimentos de matérias primas.
     * 
     * @param resuplyPrimeMaterials número de fornecimentos de matérias primas
     */
    @Override
    public synchronized void setResuplyPrimeMaterials(int resuplyPrimeMaterials) {
        this.resuplyPrimeMaterials = resuplyPrimeMaterials;
    }

    /**
     * Altera o número de matérias primas fornecidas.
     * 
     * @param totalAmountPrimeMaterialsSupplied número de matérias primas fornecidas
     */
    @Override
    public synchronized void setTotalAmountPrimeMaterialsSupplied(int totalAmountPrimeMaterialsSupplied) {
        this.totalAmountPrimeMaterialsSupplied = totalAmountPrimeMaterialsSupplied;
    }

    /**
     * Altera o total de produtos produzidos. 
     * 
     * @param totalAmountProductsManufactured   total de produtos produzidos
     */
    @Override
    public synchronized void setTotalAmountProductsManufactured(int totalAmountProductsManufactured) {
        this.totalAmountProductsManufactured = totalAmountProductsManufactured;
    }

    /**
     * Altera o stock do armazém.
     * 
     * @param StorageHouseStock stock do armazém
     */
    @Override
    public synchronized void setStorageHouseStock(int StorageHouseStock) {
        this.StorageHouseStock = StorageHouseStock;
    }
    
    /**
     * Adiciona productos manufacturados ao artesão.
     * 
     * @param id        ID do artesão
     * @param number    productos manufacturados do artesão
     */
    @Override
    public synchronized void addCraftsmanAccManProducts(int id, int number) {
        this.craftsmanAccManProducts[id] = number;
    }

    /**
     * Adiciona produtos aos produtos acumulados do cliente.
     * 
     * @param id        ID do cliente.
     * @param number    produtos acumulados do cliente
     */
    @Override
    public synchronized void addCustomerAccumulatedBoughtGoods(int id, int number) {
        this.customerAccumulatedBoughtGoods[id] = number;
    }

    /**
     * Verifica se existe stock no armazem.
     * 
     * @return  se existe stock no armazem
     */
    @Override
    public synchronized boolean isThereStockInStoreHouse() {
        return this.StorageHouseStock > 0;
    }

    /**
     * Verifica se existem matérias primas na oficina.
     * 
     * @return  se existem matérias primas na oficina
     */
    @Override
    public synchronized boolean isTherePrimeMaterialInWorkshop() {
        return this.stockPrimeMaterials > 0;
    }

    /**
     * Verifica se há produtos na oficina.
     * 
     * @return  se há produtos na oficina
     */
    @Override
    public synchronized boolean isThereProductsInWotkshop() {
        return this.stockFinnishedProducts > 0;
    }
    
    /**
     * Método que número de clientes na loja.
     * 
     * @return  número de clientes na loja.
     */
    @Override
    public synchronized int getSizeCustomers() {
        return this.customersState.length;
    }

    /**
     * Verifica se o ciclo de vida dos artesões terminou.
     * 
     * @return  se o ciclo de vida dos artesões terminou
     */
    @Override
    public synchronized boolean areCraftmenDeath() {
        boolean areCraftmenDeath = true;
        
        for (String crafsmanState1 : this.crafsmanState) {
            if (!crafsmanState1.equals("DIED")) {
                areCraftmenDeath = false;
            }
        }
        
        return areCraftmenDeath;
    }

    /**
     * Devolve o número de produtos feitos.
     * 
     * @return  número de produtos feitos
     */
    @Override
    public synchronized int getStockFinnishedProducts() {
        return this.stockFinnishedProducts;
    }

    /**
     * Verifica se o ciclo de vida dos clientes terminou.
     * 
     * @return  se o ciclo de vida dos clientes terminou
     */
    @Override
    public synchronized boolean areCustomersDead() {
        boolean areCustomerDeath = true;
        
        for (String customersState1 : this.customersState) {
            if (!customersState1.equals("DIED")) {
                areCustomerDeath = false;
            }
        }
        
        return areCustomerDeath;
    }
}
