/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop.interfaces;

/**
 *
 * @author luis
 */
public interface EntrepreneurWorkshopInterface {

    /**
     *
     * @param amountProductsToPickUp
     * @return
     */
    public int collectABatchOfProducts(int amountProductsToPickUp);

    /**
     *
     * @param numberOfReplenish
     */
    public void addPrimeMaterial(int numberOfReplenish);
    
}
