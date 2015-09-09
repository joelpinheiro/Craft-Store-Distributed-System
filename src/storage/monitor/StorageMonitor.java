/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage.monitor;

import utils.Combo;
import java.rmi.RemoteException;
import interfaces.EntrepreneurStorageInterface;
import interfaces.StorageLogInterface;

/**
 * StorageMonitor e uma classe monitor que gere recursos partilhados, cujo 
 * acesso e concorrente, tais como:
 * <ul>
 * <li>Quantidade de materias primas em stock, atualmente, no armazém</li>
 * </ul>
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
    private int[] vectorClock;

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
    public StorageMonitor(int stockPrimeMaterials, StorageLogInterface logMonitor, int[] vectorClock) {
        this.LOGMONITOR = logMonitor;
        this.stockPrimeMaterials = stockPrimeMaterials;
        this.vectorClock = vectorClock;
    }

    /**
     * Operação de remover matérias primas do armazém.
     * 
     * @param numberOfReplenish número de matérias primas a remover
     * @param state state
     * @param vectorClock
     * @param vectorClockPosition
     * @return return result 
     * @throws java.rmi.RemoteException 
     */
    @Override
    public synchronized Combo removePrimeMaterial(int numberOfReplenish, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        if(this.stockPrimeMaterials >= numberOfReplenish)
        {
            this.stockPrimeMaterials -= numberOfReplenish;
            this.LOGMONITOR.setStorageHouseStock(this.stockPrimeMaterials, state, this.vectorClock);
            
            combo = new Combo(numberOfReplenish, this.vectorClock);
            return combo;
        }
        else
        {
            int tmp = this.stockPrimeMaterials;
            this.stockPrimeMaterials = 0;
            this.LOGMONITOR.setStorageHouseStock(this.stockPrimeMaterials, state, this.vectorClock);
            
            combo = new Combo(tmp, this.vectorClock);
            return combo;
        }    
    }
    
    // TODO: Método para fazer rebuy das matérias primas

    /**
     * Método que devolve número de matérias primas presentes no armazém.
     * 
     * @param vectorClock
     * @param vectorClockPosition
     * @return return result número de matérias primas presentes no armazém
     */
    @Override
    public synchronized Combo getStock(int[] vectorClock, int vectorClockPosition) {
        
        Combo combo = new Combo();
        this.vectorClock = combo.updateVectorClock(this.vectorClock, vectorClock);
        
        combo = new Combo(this.stockPrimeMaterials, this.vectorClock);
        return combo;
    }
}
