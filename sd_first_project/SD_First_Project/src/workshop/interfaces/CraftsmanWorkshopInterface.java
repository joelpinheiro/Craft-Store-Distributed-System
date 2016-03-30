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
public interface CraftsmanWorkshopInterface {

    /**
     *
     * @param ID
     * @return
     */
    public boolean isThereWorkLeft(int ID);

    /**
     *
     * @param ID
     * @param amountToCollect
     * @return
     */
    public boolean checkForMaterials(int ID, int amountToCollect);

    /**
     *
     * @param ID
     * @param amountToCollect
     */
    public boolean collectMaterials(int ID, int amountToCollect);

    /**
     *
     * @param ID
     * @param productStep
     * @return
     */
    public boolean goToStore(int ID, int productStep);

    /**
     *
     * @param ID
     */
    public void batchReadyForTransfer(int ID);

    /**
     *
     * @param ID
     */
    public void backToWork(int ID);

    /**
     *
     * @param ID
     */
    public void primeMaterialsNeeded(int ID);

    /**
     *
     * @param ID
     * @param productStep
     */
    public void prepareToProduce(int ID, int productStep);

    /**
     *
     * @param ID
     */
    public void endOperationCraftsman(int ID);
}
