package communication;

import java.io.*;

/**
 * Este tipo de dados define as mensagens que são trocadas entre os clientes e o
 * servidor numa solução do Problema dos Barbeiros Sonolentos que implementa o
 * modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento
 * estático dos threads barbeiro. A comunicação propriamente dita baseia-se na
 * troca de objectos de tipo Message num canal TCP.
 */
public class Message implements Serializable {

    /**
     * Chave de serialização
     *
     * @serialField serialVersionUID
     */

    private static final long serialVersionUID = 1001L;

    /**
     *
     */
    public static final int ACK = 1;

    /* Tipos das mensagens */

    /**
     * Mensagens comuns
     */
    public static int TRUE = 101;

    /**
     *
     */
    public static int FALSE = 100;
    
    /**
     * Storage - Log
     */
    public static final int SETSTORAGEHOUSESTOCK = 2;

    private int StorageHouseStockLog = -1;
    
    private int amountPrimeMaterialsInWorkshop;
    
    private boolean callTransferPrimeMAterialsToWorkshop;
    
    private int primeMaterialStockResuply;
    
    private int totalAmountPrimeMaterialsSupplied;
    
    private int currentStockFinnishedProductsInWorkshop;
    
    private int totalAmountProductsManufacturedInWorkshop;
    
    private int craftsmanAccumulatedManufacturedProduct;
    
    private String storeDoorState;
    
    private int storeAvaliableProducts;
    
    private int numberCustomersInStore;
    
    private int amountAccBoughtProductsByCustomer;
    
    private int numberGoodsInDisplayInStorelog;
    
    private boolean CallTransferProductsLog;
    
    private boolean callTransferPrimeMaterialsLog;

    /**
     * Entrepreneur - Storage
     */
    public static final int REMOVEPRIMEMATERIALSTORAGE = 3;

    /**
     *
     */
    public static final int PRIMEMATERIALSTORAGEREPLENISHAMOUNT = 4;
    
    private int removedPrimeMaterialsFromStorage = -1;
    
    private int amountToRemovePrimeMaterialFromStorage = -1;

    
    /**
     * Craftman - Workshop
     */
    public static final int ISTHEREWORKLEFT_WORKSHOP = 5;
    
    /**
     *
     */
    public static final int CHECKFORMATERIALS_WORKSHOP = 6;
    
    /**
     *
     */
    public static final int COLLECTMATERIALS_WORKSHOP = 7;
    
    /**
     *
     */
    public static final int GOTOSTORE_WORKSHOP = 8;
    
    /**
     *
     */
    public static final int BATCHREADYFORTRANSFER_WORKSHOP = 9;
    
    /**
     *
     */
    public static final int BACKTOWORK_WORKSHOP = 10;
    
    /**
     *
     */
    public static final int PRIMEMATERIALISNEEDED_WORKSHOP = 11;
    
    /**
     *
     */
    public static final int ENDOPERATIONCRAFTMAN_WORKSHOP = 12;
    
    /**
     *
     */
    public static final int PREPARETOPRODUCE_WORKSHOP = 13;
    
    private int amountToCollectWorkshop;
    
    private int productStep;
    
    private int craftmanID = -1;
    
    /**
     * Customer
    */
    public int customerID = -1;
    
    private int amountProductsToBuyByCostumer;
    
    private boolean insideTheStore_customer;
    
    /**
     *
     */
    public static final int GOSHOPING_CUSTOMER = 14;
    
    /**
     *
     */
    public static final int ISDOOROPEN_CUSTOMER = 15;
    
    /**
     *
     */
    public static final int ENTERSHOP_CUSTOMER = 16;
    
    /**
     *
     */
    public static final int IWANTTHIS_CUSTOMER = 17;
 
    /**
     *
     */
    public static final int EXITSHOP_CUSTOMER = 18;
    
    /**
     *
     */
    public static final int ENDOPERATION_CUSTOMER = 19;
    
    /**
     *
     */
    public static final int PERUSINGAROUND_CUSTOMER = 20;
    
    /**
     *
     */
    public static final int CANENDOPERATION_CUSTOMER = 21;
    
    
    /**
     * Entrepreneur
     */
    public static final int ADDRESSCUSTOMER_ENTREPRENEUR = 22;
    
    /**
     *
     */
    public static final int SAYGOODBYETOCUSTOMER_ENTREPRENEUR = 23;
    
    /**
     *
     */
    public static final int CLOSETHEDOOR_ENTREPRENEUR = 24;
    
    /**
     *
     */
    public static final int CUSTOMERINSHOP_ENTREPRENEUR = 25;
    
    /**
     *
     */
    public static final int PREPARETOLEAVE_ENTREPRENEUR = 26;
    
    /**
     *
     */
    public static final int ADDPRODUCTS_ENTREPRENEUR = 27;
    
    /**
     *
     */
    public static final int VISITSUPPLIERS_ENTREPRENEUR = 28;
    
    /**
     *
     */
    public static final int RETURNTOSHOP_ENTREPRENEUR = 29;
    
    /**
     *
     */
    public static final int APPRAISESIT_ENTREPRENEUR = 30;
    
    /**
     *
     */
    public static final int APPRAISESITRESULT_ENTREPRENEUR = 31;
    
    private char appraiseSitResult;
    
    /**
     *
     */
    public static final int REPLENISHSTOCK_ENTREPRENEUR = 32;
    
    /**
     *
     */
    public static final int ENDOPERATIONENTREPRENEUR_ENTREPRENEUR = 33;
    
    /**
     *
     */
    public static final int SERVICECUSTOMER_ENTREPRENEUR = 34;
    
    /**
     *
     */
    public static final int PREPARETOWORK_ENTREPRENEUR = 35;
    
    /**
     *
     */
    public static final int COLLECTABATCHOFPRODUCTS_ENTREPRENEUR = 36;
    
    /**
     *
     */
    public static final int COLLECTABATCHOFPRODUCTS_RESULT_ENTREPRENEUR = 37;
    
    private int collectABatchOfProductsResult;

    /**
     *
     */
    public static final int ADDPRIMEMATERIAL_ENTREPRENEUR = 38;
    
    private int amountPrimeMaterialToBeAddedByEntrepreneur;
    
    private int numberProductsToAddToStore;

    /**
     * Workshop - Store
     */
    public static final int NEEDMOREPRIMEMATERIALS_WORKSHOP_STORE = 39;

    /**
     *
     */
    public static final int CALLENTREPRENEURTOCOLLECTPRODUCTS_WORKSHOP_STORE = 40;
    
    /**
     * Store - Log
     */
    public static final int REPORTSTATUSENTREPRENEUR_STORE = 41;
    
    /**
     *
     */
    public static final int SETSTORESTATE_STORE = 42;
    
    /**
     *
     */
    public static final int SETSTORAGEHOUSESTOCK_STORE = 43;
    
    /**
     *
     */
    public static final int REPORTSTATUSCUSTOMER_STORE = 44;
    
    /**
     *
     */
    public static final int SETCUSTOMERSINSIDE_STORE = 45;
    
    /**
     *
     */
    public static final int ADDCUSTOMERACCUMULATEDBOUGHTGOODS_STORE = 46;
    
    /**
     *
     */
    public static final int SETGOODSINDISPLAY_STORE = 47;
    
    /**
     *
     */
    public static final int SETCALLTRANSFERPRODUCTS_STORE = 48;
    
    /**
     *
     */
    public static final int SETCALLTRANSFERPRIMEMATERIALS_STORE = 49;
    
    /**
     *
     */
    public static final int ISTHERESTOCKINSTOREHOUSE_STORE = 50;
    
    /**
     *
     */
    public static final int ISTHEREPRIMEMATERIALINWORKSHOP_STORE = 51;
    
    /**
     *
     */
    public static final int ISTHEREPRODUCTSINWORKSHOP_STORE = 52;
    
    /**
     *
     */
    public static final int GETSIZECUSTOMERS_STORE = 53;
    
    /**
     *
     */
    public static final int RETURNGETSIZECUSTOMERS_STORE = 54;
    
    private int sizeCustomersInStore;
    
    /**
     *
     */
    public static final int ARECRAFTMENDEATH_STORE = 55;
    
    /**
     *
     */
    public static final int GETSTOCKFINNISHEDPRODUCTS_STORE = 56;
    
    /**
     *
     */
    public static final int RETURNSTOCKFINNISHEDPRODUCTS_STORE = 57;
    
    private int stockFinnishedProductsInWorkshop;
    
    /**
     *
     */
    public static final int ARECUSTOMERSDEAD_STORE = 58;
    
    
    /**
     * Workshop - Log
     */
    public static final int REPORTSTATUSCRAFTMAN_WORKSHOP = 59;
    
    /**
     *
     */
    public static final int ISTHERESTOCKINSTOREHOUSE_WORKSHOP = 60;
    
    /**
     *
     */
    public static final int REPORTSTATUSENTREPRENEUR_WORKSHOP = 61;
    
    /**
     *
     */
    public static final int SETSTOCKPRIMEMATERIALS_WORKSHOP = 62;
    
    /**
     *
     */
    public static final int SETCALLTRANSFERPRIMEMATERIALS_WORKSHOP = 63;
    
    /**
     *
     */
    public static final int SETRESUPLYPRIMEMATERIALS_WORKSHOP = 64;
    
    /**
     *
     */
    public static final int SETTOTALAMOUNTPRIMEMATERIALSSUPPLIED_WORKSHOP = 65;
    
    /**
     *
     */
    public static final int SETSTOCKFINNISHEDPRODUCTS_WORKSHOP = 66;
    
    /**
     *
     */
    public static final int SETTOTALAMOUNTPRODUCTSMANUFACTURED_WORKSHOP = 67;
    
    /**
     *
     */
    public static final int ADDCRAFTMANACCMANPRODUCTS_WORKSHOP = 68;
    
    /**
     * Configuration logic
     */
    public static final int GETCONFIGURATIONPARAMETERS_ENTREPRENEUR = 69;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_ENTREPRENEUR_RESULT = 70;
    
    private int amountProductsToPickUp_conf_entrepreneur;
    
    private int amountOfReplenishWorkshop_conf_entrepreneur;
    
    private int portNumbStorage;
    
    private String serverUrlStorage;
    
    private int portNumbStore;
    
    private String serverUrlStore;
    
    private int portNumbWorkshop;
    
    private String serverUrlWorkshop;
    
    private int portNumbLog;
        
    private String serverUrlLog;
    
    private int customerSize;
    
    private int craftsmanSize;
    
    private int craftsmanProductStep_conf;
    
    private int craftsmanAmountToCollectPrimeMaterials_conf;
    
    private int craftsmanElapsedTimeconf;
    
    private String logFileName;
    
    private String logFilePath;
    
    private int characterSpacing_conf;
    
    private int storagePrimeMaterialsStock_conf;
    
    private int productWorkshopThreshold_conf;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_NOTREADY = 71;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_CUSTOMER = 72;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_CUSTOMER_RESULT = 73;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_CRAFTSMAN = 74;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_CRAFTSMAN_RESULT = 75;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_LOG = 76;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_LOG_RESULT = 77;
    
    /**
     *
     */
    public static final int SUBMITCONFIGURATION_LOG = 78;
    
    /**
     *
     */
    public static final int SUBMITCONFIGURATION_STORAGE = 79;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_STORAGE = 80;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_STORAGE_RESULT = 81;
    
    /**
     *
     */
    public static final int SUBMITCONFIGURATION_STORE = 82;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_STORE = 83;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_STORE_RESULT = 84;
    
    /**
     *
     */
    public static final int SUBMITCONFIGURATION_WORKSHOP = 85;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_WORKSHOP= 86;
    
    /**
     *
     */
    public static final int GETCONFIGURATIONPARAMETERS_WORKSHOP_RESULT = 87;
    
    /**
     */
    private int msgType = -1;
    
    private String state = null;
    
    /**
     * Mensagem tipo 1
     *
     * @param type type
     */
    public Message(int type) {
        msgType = type;
    }
    
    /**
     *
     * @param type type
     * @param arg arg
     */
    public Message(int type, int arg){
        msgType = type;
        if(msgType == RETURNGETSIZECUSTOMERS_STORE){
            sizeCustomersInStore = arg;
        }
        else if(msgType == GETSTOCKFINNISHEDPRODUCTS_STORE){
            storeAvaliableProducts = arg;
        }
        else if(msgType == RETURNSTOCKFINNISHEDPRODUCTS_STORE){
            stockFinnishedProductsInWorkshop = arg;
        }
    }
    
    /**
     *
     * @param type type
     * @param arg arg
     */
    public Message(int type, String arg) {
        msgType = type;
        if(arg != null)
            this.state = arg;
    }
    
    /**
     *
     * @param type type
     * @param state state
     * @param id id
     */
    public Message(int type, String state, int id) {
        msgType = type;
        if (msgType == REPORTSTATUSCUSTOMER_STORE) {
            this.state = state;
            this.customerID = id;
        }
        else if (msgType == REPORTSTATUSCRAFTMAN_WORKSHOP) {
            this.state = state;
            this.craftmanID = id;
        }
        else if (msgType == REPORTSTATUSENTREPRENEUR_WORKSHOP) {
            this.state = state;
        }
    }

    /**
     * Instanciação de uma mensagem (forma 1).
     * @param type type
     * @param arg arg
     * @param state state
     */
    public Message(int type, int arg, String state) {
        msgType = type;
        if (msgType == SETSTORAGEHOUSESTOCK) {
            StorageHouseStockLog = arg;
            this.state = state;
        }
        else if (msgType == REMOVEPRIMEMATERIALSTORAGE) {
            amountToRemovePrimeMaterialFromStorage = arg;
            this.state = state;
        }
        else if (msgType == PRIMEMATERIALSTORAGEREPLENISHAMOUNT) {
            removedPrimeMaterialsFromStorage = arg;
            this.state = state;
        }
        else if (msgType == ISTHEREWORKLEFT_WORKSHOP) {
            craftmanID = arg;
            this.state = state;
        }
        else if (msgType == ENDOPERATIONCRAFTMAN_WORKSHOP) {
            craftmanID = arg;
            this.state = state;
        }
        else if (msgType == PRIMEMATERIALISNEEDED_WORKSHOP) {
            craftmanID = arg;
            this.state = state;
        }
        else if (msgType == BACKTOWORK_WORKSHOP) {
            craftmanID = arg;
            this.state = state;
        }
        else if (msgType == BATCHREADYFORTRANSFER_WORKSHOP) {
            craftmanID = arg;
            this.state = state;
        }
        else if(msgType == GOSHOPING_CUSTOMER){
            customerID = arg;
            this.state = state;
        }
        else if(msgType == NEEDMOREPRIMEMATERIALS_WORKSHOP_STORE){
            craftmanID = arg;
        }        
        else if(msgType == CALLENTREPRENEURTOCOLLECTPRODUCTS_WORKSHOP_STORE){
            craftmanID = arg;
        }
        else if(msgType == COLLECTABATCHOFPRODUCTS_RESULT_ENTREPRENEUR){
            collectABatchOfProductsResult = arg;
            this.state = state;
        }
        else if(msgType == SETSTOCKPRIMEMATERIALS_WORKSHOP){
            amountPrimeMaterialsInWorkshop = arg;
        }
        else if(msgType == SETRESUPLYPRIMEMATERIALS_WORKSHOP){
            primeMaterialStockResuply = arg;
        }
        else if(msgType == SETTOTALAMOUNTPRIMEMATERIALSSUPPLIED_WORKSHOP){
            totalAmountPrimeMaterialsSupplied = arg;
        }
        else if(msgType == SETSTOCKFINNISHEDPRODUCTS_WORKSHOP){
            currentStockFinnishedProductsInWorkshop = arg;
        }
        else if(msgType == SETTOTALAMOUNTPRODUCTSMANUFACTURED_WORKSHOP){
            totalAmountProductsManufacturedInWorkshop = arg;
        }
        else if(msgType == SETSTORAGEHOUSESTOCK_STORE){
            storeAvaliableProducts = arg;
        }
        else if(msgType == SETCUSTOMERSINSIDE_STORE){
            numberCustomersInStore = arg;
        }
        else if(msgType == SETGOODSINDISPLAY_STORE){
            numberGoodsInDisplayInStorelog = arg;
        }
        else if(msgType == ADDPRODUCTS_ENTREPRENEUR){
            numberProductsToAddToStore = arg;
            this.state = state;
        }
        else if(msgType == ISDOOROPEN_CUSTOMER){
            customerID = arg;
            this.state = state;
        }
        else if(msgType == PERUSINGAROUND_CUSTOMER){
            customerID = arg;
            this.state = state;
        }
        else if(msgType == ENTERSHOP_CUSTOMER){
            customerID = arg;
            this.state = state;
        }
        else if(msgType == EXITSHOP_CUSTOMER){
            customerID = arg;
            this.state = state;
        }
        else if(msgType == ADDPRIMEMATERIAL_ENTREPRENEUR){
            this.state = state;
            amountPrimeMaterialToBeAddedByEntrepreneur = arg;
        }
        else if(msgType == COLLECTABATCHOFPRODUCTS_ENTREPRENEUR){
            amountToCollectWorkshop = arg;
            this.state = state;
        }
        else if(msgType == SUBMITCONFIGURATION_LOG){
            portNumbLog = arg;
            serverUrlLog = state;
        }
        else if(msgType == SUBMITCONFIGURATION_STORAGE){
            portNumbStorage = arg;
            serverUrlStorage = state;
        }
        else if(msgType == SUBMITCONFIGURATION_STORE){
            portNumbStore = arg;
            serverUrlStore = state;
        }
        else if(msgType == SUBMITCONFIGURATION_WORKSHOP){
            portNumbWorkshop = arg;
            serverUrlWorkshop = state;
        }
    }
    
    /**
     *
     * @param type type
     * @param arg1 arg1
     * @param arg2 arg2
     * @param state state
     */
    public Message(int type, int arg1, int arg2, String state) {
        msgType = type;
        if (msgType == CHECKFORMATERIALS_WORKSHOP) {
            craftmanID = arg1;
            amountToCollectWorkshop = arg2;
            this.state = state;
        }
        else if (msgType == PREPARETOPRODUCE_WORKSHOP) {
            craftmanID = arg1;
            productStep = arg2;
            this.state = state;
        }
        else if (msgType == GOTOSTORE_WORKSHOP) {
            craftmanID = arg1;
            productStep = arg2;
            this.state = state;
        }
        else if(msgType == ADDCRAFTMANACCMANPRODUCTS_WORKSHOP){
            craftmanID = arg1;
            craftsmanAccumulatedManufacturedProduct = arg2;
            this.state = state;
        }
        else if(msgType == ADDCUSTOMERACCUMULATEDBOUGHTGOODS_STORE){
            customerID = arg1;
            amountAccBoughtProductsByCustomer = arg2;
        }
        else if(msgType == IWANTTHIS_CUSTOMER){
            customerID = arg1;
            amountProductsToBuyByCostumer = arg2;
            this.state = state;
        }
        else if(msgType == COLLECTMATERIALS_WORKSHOP){
            craftmanID = arg1;
            amountToCollectWorkshop = arg2;
            this.state = state;
        }
        else if(msgType == GETCONFIGURATIONPARAMETERS_CUSTOMER_RESULT){
            portNumbStore = arg1;
            customerSize = arg2;
            serverUrlStore = state;
        }
        else if(msgType == GETCONFIGURATIONPARAMETERS_STORAGE_RESULT){
            storagePrimeMaterialsStock_conf = arg1;
            portNumbLog = arg2;
            serverUrlLog = state;
        }
        else if(msgType == GETCONFIGURATIONPARAMETERS_STORE_RESULT){
            customerSize = arg1;
            portNumbLog = arg2;
            serverUrlLog = state;
        }
    }
    
    /**
     *
     * @param type type
     * @param id id
     * @param arg arg
     * @param state state
     */
    public Message(int type, int id, boolean arg, String state){
        msgType = type;
        if(msgType == ENDOPERATION_CUSTOMER){
            customerID = id;
            insideTheStore_customer = arg;
            this.state = state;
        }
        else if(msgType == CANENDOPERATION_CUSTOMER){
            customerID = id;
            insideTheStore_customer = arg;
            this.state = state;
        }
    }

    /**
     *
     * @param type type
     * @param arg arg
     * @param state state
     */
    public Message(int type, boolean arg, String state) {
        msgType = type;
        if (msgType == SETCALLTRANSFERPRIMEMATERIALS_WORKSHOP) {
            callTransferPrimeMAterialsToWorkshop = arg;
        } else if (msgType == SETCALLTRANSFERPRODUCTS_STORE) {
            CallTransferProductsLog = arg;
        } else if (msgType == SETCALLTRANSFERPRIMEMATERIALS_STORE) {
            callTransferPrimeMaterialsLog = arg;
        }
    }
    
    /**
     *
     * @param appraiseResult appraiseResult
     * @param type type
     */
    public Message(char appraiseResult, int type){
        msgType = type;
        this.appraiseSitResult = appraiseResult;
    }

    /**
     *
     * @param type type
     * @param arg arg
     * @param state state
     */
    public Message(int type, String arg, String state){
        msgType = type;
        if(msgType == SETSTORESTATE_STORE){
            storeDoorState = arg;
            this.state = state;
        }
    }
    
    /**
     *
     * @param type type
     * @param amountProductsToPickUp amountProductsToPickUp
     * @param amountOfReplenishWorkshop amountOfReplenishWorkshop
     * @param portNumbStorage portNumbStorage
     * @param serverUrlStorage serverUrlStorage
     * @param portNumbStore portNumbStore
     * @param serverUrlStore serverUrlStore
     * @param portNumbWorkshop portNumbWorkshop
     * @param serverUrlWorkshop serverUrlWorkshop
     */
    public Message(int type, int amountProductsToPickUp, int amountOfReplenishWorkshop,
                        int portNumbStorage, String serverUrlStorage, int portNumbStore, 
                        String serverUrlStore, int portNumbWorkshop, String serverUrlWorkshop){       // used for the configuration of entrepreneur
        msgType = type;
        if(msgType == GETCONFIGURATIONPARAMETERS_ENTREPRENEUR_RESULT){
            this.amountProductsToPickUp_conf_entrepreneur = amountProductsToPickUp;
            this.amountOfReplenishWorkshop_conf_entrepreneur = amountOfReplenishWorkshop;
            this.portNumbStorage = portNumbStorage;
            this.serverUrlStorage = serverUrlStorage;
            this.portNumbStore = portNumbStore;
            this.serverUrlStore = serverUrlStore;
            this.portNumbWorkshop = portNumbWorkshop;
            this.serverUrlWorkshop = serverUrlWorkshop;
        }
    }
 
    /**
     *
     * @param type type
     * @param portNumbWorkshop portNumbWorkshop
     * @param howManyCraftsman howManyCraftsman
     * @param productStep productStep
     * @param ammountToCollectPrimeMaterials ammountToCollectPrimeMaterials
     * @param elapsedTime elapsedTime
     * @param serverUrlWorkshop serverUrlWorkshop
     */
    public Message(int type, int portNumbWorkshop, int howManyCraftsman,
            int productStep, int ammountToCollectPrimeMaterials, int elapsedTime,
            String serverUrlWorkshop) {
        msgType = type;
        if(msgType == GETCONFIGURATIONPARAMETERS_CRAFTSMAN_RESULT){
            this.portNumbWorkshop = portNumbWorkshop;
            this.craftsmanSize = howManyCraftsman;
            this.craftsmanProductStep_conf = productStep;
            this.craftsmanAmountToCollectPrimeMaterials_conf = ammountToCollectPrimeMaterials;
            this.craftsmanElapsedTimeconf = elapsedTime;
            this.serverUrlWorkshop = serverUrlWorkshop;
        }
    }

    /**
     *
     * @param type type
     * @param arg1 arg1
     * @param arg2 arg2
     * @param arg3 arg3
     * @param arg4 arg4
     * @param arg5 arg5
     * @param arg6 arg6
     */
    public Message(int type, int arg1, int arg2, int arg3, int arg4, String arg5, String arg6) {
        msgType = type;
        
        if(msgType == GETCONFIGURATIONPARAMETERS_LOG_RESULT){
            this.customerSize = arg1;
            this.storagePrimeMaterialsStock_conf = arg2;
            this.craftsmanSize = arg3;
            this.characterSpacing_conf = arg4;
            this.logFileName = arg5;
            this.logFilePath = arg6;
        }
        else if(msgType == GETCONFIGURATIONPARAMETERS_WORKSHOP_RESULT){
            portNumbLog = arg1;
            portNumbStore = arg2;
            productWorkshopThreshold_conf = arg3;
            craftsmanSize = arg4;
            serverUrlLog = arg5;
            serverUrlStore = arg6;
        }
    }
    
    /**
     *
     * @return return result
     */
    public int getType() {
        return (msgType);
    }

    /**
     *
     * @return return result
     */
    public int getRemovedPrimeMaterialsFromStorage() {
        return removedPrimeMaterialsFromStorage;
    }

    /**
     *
     * @return return result
     */
    public int getAmountPrimeMaterialsToRemoveFromStock() {
        return amountToRemovePrimeMaterialFromStorage;
    }

    /**
     *
     * @return return result
     */
    public int getStorageHouseStockLog() {
        return StorageHouseStockLog;
        //return storagePrimeMaterialsStock_conf;
    }

    /**
     *
     * @return return result
     */
    public int getCraftmanID() {
        return craftmanID;
    }

    /**
     *
     * @return return result
     */
    public int getAmountToCollect() {
        return amountToCollectWorkshop;
    }

    /**
     *
     * @return return result
     */
    public int getProductStep() {
        return productStep;
    }

    /**
     *
     * @return return result
     */
    public char getAppraiseSitResult() {
        return appraiseSitResult;
    }

    /**
     *
     * @return return result
     */
    public int getCollectABatchOfProductsResultEntrepreneur() {
        return collectABatchOfProductsResult;
    }

    /**
     *
     * @return return result
     */
    public int getSizeCustomers_store() {
        return this.sizeCustomersInStore;
    }

    /**
     *
     * @return return result
     */
    public int getStockFinnishedProducts_store() {
        return stockFinnishedProductsInWorkshop;
    }

    /**
     *
     * @return return result
     */
    public int getAmountOfProductsToRemoveByEntrepreneur() {
        return amountToCollectWorkshop;
    }

    /**
     *
     * @return return result
     */
    public int getAmountPrimeMaterialsToBeAddedByEntrepreneur() {
        return this.amountPrimeMaterialToBeAddedByEntrepreneur;
    }

    /**
     *
     * @return return result
     */
    public int getAmountPrimeMaterialsInWorkshop() {
        return amountPrimeMaterialsInWorkshop;
    }

    /**
     *
     * @return return result
     */
    public boolean getCallTransferPrimeMaterialsToWorkshop() {
        return callTransferPrimeMAterialsToWorkshop;
    }

    /**
     *
     * @return return result
     */
    public int getPrimeMaterialStockResuply() {
        return primeMaterialStockResuply;
    }

    /**
     *
     * @return return result
     */
    public int getTotalAmountPrimeMaterialsSupplied() {
        return totalAmountPrimeMaterialsSupplied;
    }

    /**
     *
     * @return return result
     */
    public int getCurrentStockFinnishedProductsInWorkshop() {
        return currentStockFinnishedProductsInWorkshop;
    }

    /**
     *
     * @return return result
     */
    public int getTotalAmountProductsManufacturedInWorkshop() {
        return totalAmountProductsManufacturedInWorkshop;
    }

    /**
     *
     * @return return result
     */
    public int getCraftsmanAccumulatedManufacturedProduct() {
        return craftsmanAccumulatedManufacturedProduct;
    }

    /**
     *
     * @return return result
     */
    public String getStoreDoorState() {
        return storeDoorState;
    }

    /**
     *
     * @return return result
     */
    public int getStoreAvaliableProducts() {
        return storeAvaliableProducts;
    }

    /**
     *
     * @return return result
     */
    public int getNumberCustomersInStore() {
        return numberCustomersInStore;
    }

    /**
     *
     * @return return result
     */
    public int getAmountAccBoughtProductsByCustomer() {
        return amountAccBoughtProductsByCustomer;
    }

    /**
     *
     * @return return result
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     *
     * @return return result
     */
    public int getNumberGoodsInDisplayInStorelog() {
        return numberGoodsInDisplayInStorelog;
    }

    /**
     *
     * @return return result
     */
    public boolean getCallTransferProductsLog() {
        return CallTransferProductsLog;
    }

    /**
     *
     * @return return result
     */
    public boolean getCallTransferPrimeMaterialsLog() {
        return callTransferPrimeMaterialsLog;
    }

    /**
     *
     * @return return result
     */
    public int getNumberProductsToAddToStore() {
        return this.numberProductsToAddToStore;
    }

    /**
     *
     * @return return result
     */
    public int getAmountProductsToBuyByCostumer() {
        return amountProductsToBuyByCostumer;
    }

    /**
     *
     * @return return result
     */
    public boolean getInsideTheStore_customer() {
        return insideTheStore_customer;
    }

    /**
     *
     * @return return result
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @return return result
     */
    public int getAmoutProductsToPickUp_conf_entrepreneur() {
        return amountProductsToPickUp_conf_entrepreneur;
    }

    /**
     *
     * @return return result
     */
    public int getAmountOfReplenishWorkshop_conf_entrepreneur() {
        return amountOfReplenishWorkshop_conf_entrepreneur;
    }

    /**
     *
     * @return return result
     */
    public int getPortNumbStorage() {
        return portNumbStorage;
    }

    /**
     *
     * @return return result
     */
    public String getServerUrlStorage() {
        return serverUrlStorage;
    }

    /**
     *
     * @return return result
     */
    public int getPortNumbStore() {
        return portNumbStore;
    }

    /**
     *
     * @return return result
     */
    public String getServerUrlStore() {
        return serverUrlStore;
    }

    /**
     *
     * @return return result
     */
    public int getPortNumbWorkshop() {
        return portNumbWorkshop;
    }

    /**
     *
     * @return return result
     */
    public String getServerUrlWorkshop() {
        return serverUrlWorkshop;
    }

    /**
     *
     * @return return result
     */
    public int getCustomerSize() {
        return customerSize;
    }

    /**
     *
     * @return return result
     */
    public int getCraftsmanSize() {
        return craftsmanSize;
    }

    /**
     *
     * @return return result
     */
    public int getCraftsmanProductStep_conf() {
        return craftsmanProductStep_conf;
    }

    /**
     *
     * @return return result
     */
    public int getCraftsmanAmountToCollectPrimeMaterials_conf() {
        return craftsmanAmountToCollectPrimeMaterials_conf;
    }

    /**
     *
     * @return return result
     */
    public int getCraftsmanElapsedTime_conf() {
        return craftsmanElapsedTimeconf;
    }

    /**
     *
     * @return return result
     */
    public String getLogFileName() {
        return this.logFileName;
    }

    /**
     *
     * @return return result
     */
    public String getLofFilePath() {
        return this.logFilePath;
    }

    /**
     *
     * @return return result
     */
    public int getCharacterSpacing() {
        return this.characterSpacing_conf;
    }

    /**
     *
     * @return return result
     */
    public int getPortNumbLog() {
        return portNumbLog;
    }

    /**
     *
     * @return return result
     */
    public String getServerUrlLog() {
        return serverUrlLog;
    }

    /**
     *
     * @return return result
     */
    public int getStoragePrimeMaterialsStock_conf() {
        return storagePrimeMaterialsStock_conf;
        //return StorageHouseStockLog;
    }

    /**
     *
     * @return return result
     */
    public int getProductThreshold_conf() {
        return productWorkshopThreshold_conf;
    }
}
