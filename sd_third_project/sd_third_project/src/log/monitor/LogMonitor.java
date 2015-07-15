/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log.monitor;

import genclass.GenericIO;
import genclass.TextFile;
import java.rmi.RemoteException;
import interfaces.LogRemoteInterfacesWrapper;
import java.util.ArrayList;
import utils.Evento;

/**
 *  Este tipo de dados implementa o log da simulação.
 *  <p>
 *  Solução concorrente baseada em monitores como elemento de sincronização das threads clientes, artesões e dona.
 * 
 * @author luis
 * @author joelpinheiro
 */
public class LogMonitor implements LogRemoteInterfacesWrapper {
    
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
    
    private ArrayList<Evento> arraylist = new ArrayList<Evento>();

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
        int[] vectorClock = new int[1 + numberOfCraftmen + numberOfCustomers];
        this.reportStatus(vectorClock);
    }

    /**
     * Função para dar aos inteiros o mesmo tamanho em texto (evita 'barrigas' no log)
     * @param i inteiro a converter
     * @return return result  inteiro convertido em texto
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
     * @param ID ID
     * @param state state
     */
    @Override
    public synchronized void reportStatusCustomer(int ID, String state, int[] vectorClock){
        int currentId = ID;
        
        String currentState = state;
        
        this.customersState[currentId] = currentState;
        
        reportStatus(vectorClock);
    }
    
    /**
     * Reporta estado dos artesões.
     * 
     * @param id id
     * @param state state
     */
    @Override
    public synchronized void reportStatusCraftman(int id, String state, int[] vectorClock){
        
        int currentId = id;
        
        String currentState = state;
        
        this.crafsmanState[currentId] = currentState;
        
        reportStatus(vectorClock);
    }
    
    /**
     * Reporta estado da dona.
     * 
     * @param state state
     */
    @Override
    public synchronized void reportStatusEntrepreneur(String state, int[] vectorClock){
        
        this.entrepreneurState = state;
        reportStatus(vectorClock);
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
        
        header2 += "  Stat NCI   NPI   PCR PMR  APMI   NPI   NSPM   TAPM   TNP            V";
        
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
    private synchronized void reportStatus(int[] vectorClock){
        
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
            
            this.arraylist.add(new Evento(lineStatus, vectorClock));

            if(entrepreneurState.equals("DIED"))
            {
                Evento temp;
                if (arraylist.size()>1) // check if the number of orders is larger than 1
                {
                    for (int x=0; x<arraylist.size(); x++) // bubble sort outer loop
                    {
                        for (int i=0; i < arraylist.size() - x - 1; i++)                    
                            if (arraylist.get(i).compareTo(arraylist.get(i+1)) < 0)
                            {
                                temp = arraylist.get(i);
                                arraylist.set(i,arraylist.get(i+1) );
                                arraylist.set(i+1, temp);
                            }
                    }
                }

                for(Evento str: arraylist){
                    System.out.println(str.printMessageAndVectorClock());
                    log.writelnString(str.printMessageAndVectorClock());
                }
                
            }
            
            //System.out.println(lineStatus);
            //log.writelnString(lineStatus);
            
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
     * @return return result  texto de saída (T ou F)
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
    public synchronized void setStoreState(String storeState, int[] vectorClock) {
        this.storeState = storeState;
    }

    /**
     * Altera os clientes que estão dentro da loja.
     * 
     * @param customersInside   número de clientes na loja
     */
    @Override
    public synchronized void setCustomersInside(int customersInside, int[] vectorClock) {
        this.customersInside = customersInside;
    }

    /**
     * Altera os bens que estão disponíveis na loja.
     * 
     * @param goodsInDisplay    bens na loja
     */
    @Override
    public synchronized void setGoodsInDisplay(int goodsInDisplay, int[] vectorClock) {
        this.goodsInDisplay = goodsInDisplay;
    }

    /**
     * Chamada para ir buscar produtos.
     * 
     * @param callTransferProducts  booleano da chamada
     */
    @Override
    public synchronized void setCallTransferProducts(boolean callTransferProducts, int[] vectorClock) {
        this.callTransferProducts = callTransferProducts;
    }

    /**
     * Chamada para ir buscar matérias primas.
     * 
     * @param callTransferPrimeMaterials    booleano da chamada.
     */
    @Override
    public synchronized void setCallTransferPrimeMaterials(boolean callTransferPrimeMaterials, int[] vectorClock) {
        this.callTransferPrimeMaterials = callTransferPrimeMaterials;
    }

    /**
     * Altera o stock das matérias primas.
     * 
     * @param stockPrimeMaterials   stock das matérias primas
     */
    @Override
    public synchronized void setStockPrimeMaterials(int stockPrimeMaterials, int[] vectorClock) {
        this.stockPrimeMaterials = stockPrimeMaterials;
    }

    /**
     * Altera o stock dos produtos acabados.
     * 
     * @param stockFinnishedProducts    stock dos produtos acabados
     */
    @Override
    public synchronized void setStockFinnishedProducts(int stockFinnishedProducts, int[] vectorClock) {
        this.stockFinnishedProducts = stockFinnishedProducts;
    }

    /**
     * Altera o número de fornecimentos de matérias primas.
     * 
     * @param resuplyPrimeMaterials número de fornecimentos de matérias primas
     */
    @Override
    public synchronized void setResuplyPrimeMaterials(int resuplyPrimeMaterials, int[] vectorClock) {
        this.resuplyPrimeMaterials = resuplyPrimeMaterials;
    }

    /**
     * Altera o número de matérias primas fornecidas.
     * 
     * @param totalAmountPrimeMaterialsSupplied número de matérias primas fornecidas
     */
    @Override
    public synchronized void setTotalAmountPrimeMaterialsSupplied(int totalAmountPrimeMaterialsSupplied, int[] vectorClock) {
        this.totalAmountPrimeMaterialsSupplied = totalAmountPrimeMaterialsSupplied;
    }

    /**
     * Altera o total de produtos produzidos. 
     * 
     * @param totalAmountProductsManufactured   total de produtos produzidos
     */
    @Override
    public synchronized void setTotalAmountProductsManufactured(int totalAmountProductsManufactured, int[] vectorClock) {
        this.totalAmountProductsManufactured = totalAmountProductsManufactured;
    }

    /**
     * Altera o stock do armazém.
     * 
     * @param StorageHouseStock stock do armazém
     * @param state
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized void setStorageHouseStock(int stockPrimeMaterials, String state, int[] vectorClock) throws RemoteException{
        this.StorageHouseStock = stockPrimeMaterials;
    }
    
    /**
     * Adiciona productos manufacturados ao artesão.
     * 
     * @param id id        ID do artesão
     * @param number    productos manufacturados do artesão
     */
    @Override
    public synchronized void addCraftsmanAccManProducts(int id, int number, int[] vectorClock) {
        this.craftsmanAccManProducts[id] = number;
    }

    /**
     * Adiciona produtos aos produtos acumulados do cliente.
     * 
     * @param id id        ID do cliente.
     * @param number    produtos acumulados do cliente
     */
    @Override
    public synchronized void addCustomerAccumulatedBoughtGoods(int id, int number, int[] vectorClock) {
        this.customerAccumulatedBoughtGoods[id] = number;
    }

    /**
     * Verifica se existe stock no armazem.
     * 
     * @return return result  se existe stock no armazem
     */
    @Override
    public synchronized boolean isThereStockInStoreHouse(int[] vectorClock) {
        return this.StorageHouseStock > 0;
    }

    /**
     * Verifica se existem matérias primas na oficina.
     * 
     * @return return result  se existem matérias primas na oficina
     */
    @Override
    public synchronized boolean isTherePrimeMaterialInWorkshop(int[] vectorClock) {
        return this.stockPrimeMaterials > 0;
    }

    /**
     * Verifica se há produtos na oficina.
     * 
     * @return return result  se há produtos na oficina
     */
    @Override
    public synchronized boolean isThereProductsInWotkshop(int[] vectorClock) {
        return this.stockFinnishedProducts > 0;
    }
    
    /**
     * Método que número de clientes na loja.
     * 
     * @return return result  número de clientes na loja.
     */
    @Override
    public synchronized int getSizeCustomers(int[] vectorClock) {
        return this.customersState.length;
    }

    /**
     * Verifica se o ciclo de vida dos artesões terminou.
     * 
     * @return return result  se o ciclo de vida dos artesões terminou
     */
    @Override
    public synchronized boolean areCraftmenDeath(int[] vectorClock) {
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
     * @return return result  número de produtos feitos
     */
    @Override
    public synchronized int getStockFinnishedProducts(int[] vectorClock) {
        return this.stockFinnishedProducts;
    }

    /**
     * Verifica se o ciclo de vida dos clientes terminou.
     * 
     * @return return result  se o ciclo de vida dos clientes terminou
     */
    @Override
    public synchronized boolean areCustomersDead(int[] vectorClock) {
        boolean areCustomerDeath = true;
        
        for (String customersState1 : this.customersState) {
            if (!customersState1.equals("DIED")) {
                areCustomerDeath = false;
            }
        }
        
        return areCustomerDeath;
    }

    @Override
    public void setStorageHouseStock(int numberOfAvailableProducts, int[] vectorClock) throws RemoteException {
        this.StorageHouseStock = numberOfAvailableProducts;
    }
}
