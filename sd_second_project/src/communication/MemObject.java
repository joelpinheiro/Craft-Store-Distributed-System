package communication;

/**
 *    Este tipo de dados define uma memória genérica de tipo estático.
 */

public abstract class MemObject
{
  /**
   *  Tamanho da memória em número de posições
   */

   protected int nMax = 0;

    /**
     *
     */
    protected Object [] mem = null;

  /**
   *  Instanciação da memória.
   *
   *    @param nElem tamanho da memória (n. de elementos do array de armazenamento)
   */

   protected MemObject (int nElem)
   {
     mem = new Object [nElem];
     nMax = nElem;
   }

  /**
   *  Escrita de um valor (método virtual).
   *
   *    @param val valor a armazenar
   */

   protected abstract void write (Object val);

  /**
   *  Leitura de um valor (método virtual).
   *
   *    @return return result valor armazenado
   */

   protected abstract Object read ();
}
