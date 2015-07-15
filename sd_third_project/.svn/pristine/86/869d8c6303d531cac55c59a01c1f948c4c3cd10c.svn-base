/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Comparator;

/**
 *
 * @author joelpinheiro
 */
public class Evento implements Comparable<Evento>{

    private String mensagem;
    private int[] vectorClock; 

    public Evento(String mensagem, int[] vectorClock) {
        this.mensagem = mensagem;
        this.vectorClock = vectorClock;
    }

    public String getMensagem() {
        return mensagem;
    }

    public int[] getVectorClock() {
        return vectorClock;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setVectorClock(int[] vectorClock) {
        this.vectorClock = vectorClock;
    }
    
    @Override
    public int compareTo(Evento otherEvento) {
        
        boolean primeiroMaiorSegundo = false;
        boolean segundoMaiorPrimeiro = false;
        
        int[] compareOtherEvento = ((Evento)otherEvento).getVectorClock();
        
        for(int i=0; i < compareOtherEvento.length ; i++)
        {
            if(this.vectorClock[i] > compareOtherEvento[i])
                primeiroMaiorSegundo = true;
            
            if(compareOtherEvento[i] > this.vectorClock[i])
                segundoMaiorPrimeiro = true;
        }
        
        
        
        int x = 0;

        
        if((primeiroMaiorSegundo == true) && segundoMaiorPrimeiro == false)
            return -10; // troca
        else
            return 0;
        
    }
    
    @Override
    public String toString() {
        return mensagem;
    }
    
        public String printMessageAndVectorClock(){
        String line = mensagem + " ";
        
        for(int i = 0; i< vectorClock.length ; i++)
        {
            line += s(vectorClock[i]);
            if(i < vectorClock.length - 1) line += " ";
        }
        
        
        return line;
    }
        
    /**
     * Função para dar aos inteiros o mesmo tamanho em texto (evita 'barrigas' no log)
     * @param i inteiro a converter
     * @return return result  inteiro convertido em texto
     */
    private String s(int i){
        
        String x = String.valueOf(i);
        int result = 3 - x.length();
        
        for(int k = 0 ; k < result ; k++) x += " ";
        
        return x;
         //TODO: Fazer para os headers
    }         
}
