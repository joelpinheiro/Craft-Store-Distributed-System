/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage.interfaces;

/**
 *
 * @author luis
 */
public interface EntrepreneurStorageInterface {
    
    /**
     *
     * @return
     */
    public int getStock();

    /**
     *
     * @param numberOfReplenish
     * @return
     */
    public int removePrimeMaterial(int numberOfReplenish);
    
}
