/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log.interfaces;

/**
 *
 * @author luis
 */
public interface WorkshopLogInterface {

    /**
     *
     */
    public void reportStatusCraftman();

    /**
     *
     * @return
     */
    public boolean isThereStockInStoreHouse();

    /**
     *
     */
    public void reportStatusEntrepreneur();

    /**
     *
     * @param stockPrimeMaterials
     */
    public void setStockPrimeMaterials(int stockPrimeMaterials);

    /**
     *
     * @param entrepreneurCalledReplenishPrimeMaterials
     */
    public void setCallTransferPrimeMaterials(boolean entrepreneurCalledReplenishPrimeMaterials);

    /**
     *
     * @param primeMaterialStockResuply
     */
    public void setResuplyPrimeMaterials(int primeMaterialStockResuply);

    /**
     *
     * @param accumulatedSupliedPrimeMaterials
     */
    public void setTotalAmountPrimeMaterialsSupplied(int accumulatedSupliedPrimeMaterials);

    /**
     *
     * @param stockManufacturedMaterials
     */
    public void setStockFinnishedProducts(int stockManufacturedMaterials);

    /**
     *
     * @param accumulatedProducedProducts
     */
    public void setTotalAmountProductsManufactured(int accumulatedProducedProducts);

    /**
     *
     * @param ID
     * @param craftsmanAccumulatedManufacturedProduct
     */
    public void addCraftsmanAccManProducts(int ID, int craftsmanAccumulatedManufacturedProduct);

}
