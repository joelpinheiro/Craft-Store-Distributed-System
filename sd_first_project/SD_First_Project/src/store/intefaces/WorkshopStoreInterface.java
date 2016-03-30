/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store.intefaces;

/**
 *
 * @author luis
 */
public interface WorkshopStoreInterface {

    /**
     *
     * @param ID
     */
    public void needMorePrimeMaterials(int ID);
    
    /**
     *
     * @param ID
     */
    public void callEntrepreneurToCollectProducts(int ID);
    
}
