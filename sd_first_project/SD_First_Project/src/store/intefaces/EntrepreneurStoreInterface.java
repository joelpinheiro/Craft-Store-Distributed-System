/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store.intefaces;

/**
 *
 * @author luis, joelpinheiro
 */
public interface EntrepreneurStoreInterface {

    /**
     *
     */
    public void addressCustomer();

    /**
     *
     */
    public void sayGoodByeToCustomer();

    /**
     *
     */
    public void closeTheDoor();

    /**
     *
     * @return
     */
    public boolean customerInShop();

    /**
     *
     */
    public void prepareToLeave();

    /**
     *
     * @param amountProductsToPickUpFromWorkshop
     */
    public void addProducts(int amountProductsToPickUpFromWorkshop);

    /**
     *
     */
    public void visitSuppliers();

    /**
     *
     */
    public void returnToShop();

    /**
     *
     * @return
     */
    public char appraiseSit();

    /**
     *
     */
    public void replenishStock();

    /**
     *
     */
    public void endOperationEntrepreneur();

    /**
     *
     */
    public void serviceCustomer();

    /**
     *
     */
    public void prepareToWork();


}
