/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage;

import log.interfaces.StorageLogInterface;
import storage.interfaces.EntrepreneurStorageInterface;

/**
 * StorageMonitor e uma classe monitor que gere recursos partilhados, cujo 
 * acesso e concorrente, tais como:
 * <ul>
 * <li>Quantidade de materias primas em stock, atualmente, no armazém</li>
 * </ul>
 * <p></p>
 * Esta classe esta preparada para ambientes multi-threaded.
 * Os objetos thread que interagem com esta classe sao:
 * <ul>
 * <li><code>EntrepreneurThread</code></li>
 * </ul>
 * 
 * @author Luis Assuncao
 * @author Joel Pinheiro
 * @version %I%, %G%
 * @since 1.0
 */
public class StorageMonitor implements EntrepreneurStorageInterface{
    private int stockPrimeMaterials;
    private final StorageLogInterface LOGMONITOR;

    /**
     * Construtor do monitor do armazém.
     * 
     * @param stockPrimeMaterials   número de matérias primas
     * @param logMonitor            instância do monitor de log
     */
    /**
     * Construtor da classe monitor <code>StorageMonitor</code>.
     * Constroi um novo StorageMonitor capaz de gerir e manter o sincronismo entre varias instancias do tipo
     * <code>EntrepreneurThread</code>.
     * 
     * @param stockPrimeMaterials   número de matérias primas
     * @param logMonitor            instância do monitor de log     * @see <code>WorkshopLogInterface</code> 
     * @since 1.0
     */
    public StorageMonitor(int stockPrimeMaterials, StorageLogInterface logMonitor) {
        this.LOGMONITOR = logMonitor;
        this.stockPrimeMaterials = stockPrimeMaterials;
    }

    /**
     * Operação de remover matérias primas do armazém.
     * 
     * @param numberOfReplenish número de matérias primas a remover
     * @return 
     */
    @Override
    public synchronized int removePrimeMaterial(int numberOfReplenish) {
        
        if(this.stockPrimeMaterials >= numberOfReplenish)
        {
            this.stockPrimeMaterials -= numberOfReplenish;
            this.LOGMONITOR.setStorageHouseStock(this.stockPrimeMaterials);
            return numberOfReplenish;
        }
        else
        {
            int tmp = this.stockPrimeMaterials;
            this.stockPrimeMaterials = 0;
            this.LOGMONITOR.setStorageHouseStock(this.stockPrimeMaterials);
            return tmp;
        }    
    }
    
    // TODO: Método para fazer rebuy das matérias primas

    /**
     * Método que devolve número de matérias primas presentes no armazém.
     * 
     * @return número de matérias primas presentes no armazém
     */
    @Override
    public synchronized int getStock() {
        return this.stockPrimeMaterials;
    }
}
