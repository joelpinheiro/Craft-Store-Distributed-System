/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_craftman.interfaces;

/**
 * CraftsmanWorkshopInterface.
 * @author luis
 */
public interface CraftsmanWorkshopInterface {

    /**
     *
     * @param ID ID
     * @return return result
     */
    public boolean isThereWorkLeft(int ID);

    /**
     *
     * @param ID ID
     * @param amountToCollect amountToCollect
     * @return return result
     */
    public boolean checkForMaterials(int ID, int amountToCollect);

    /**
     *
     * @param ID ID
     * @param amountToCollect amountToCollect
     * @return return result 
     */
    public boolean collectMaterials(int ID, int amountToCollect);

    /**
     *
     * @param ID ID
     * @param productStep productStep
     * @return return result
     */
    public boolean goToStore(int ID, int productStep);

    /**
     *
     * @param ID ID
     */
    public void batchReadyForTransfer(int ID);

    /**
     *
     * @param ID ID
     */
    public void backToWork(int ID);

    /**
     *
     * @param ID ID
     */
    public void primeMaterialsNeeded(int ID);

    /**
     *
     * @param ID ID
     * @param productStep productStep
     */
    public void prepareToProduce(int ID, int productStep);

    /**
     *
     * @param ID ID
     */
    public void endOperationCraftsman(int ID);
}
