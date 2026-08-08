package estructuras;

/**
 * Implementación del TDA Nodo Vértice para Grafo.
 *
 * @author <a href="https://www.github.com/santinux">Santino Fuentes</a>
 * @version 1.0
 */
public class NodoVertice
{
        private Object elemento;
        private NodoVertice siguienteVertice;
        private NodoAdyacente primerAdyacente;
        
        public NodoVertice(Object unElemento, NodoVertice unSiguienteVertice, NodoAdyacente unPrimerAdyacente)
        {
                this.elemento = unElemento;
                this.siguienteVertice = unSiguienteVertice;
                this.primerAdyacente = unPrimerAdyacente;
        }
        
        public Object getElemento()
        {
                return (this.elemento);
        }
        
        public NodoVertice getSiguienteVertice()
        {
                return (this.siguienteVertice);
        }
        
        public NodoAdyacente getPrimerAdyacente()
        {
                return (this.primerAdyacente);
        }
        
        public void setElemento(Object unElemento)
        {
                this.elemento = unElemento;
        }
        
        public void setSiguienteVertice(NodoVertice unSiguienteVertice)
        {
                this.siguienteVertice = unSiguienteVertice;
        }
        
        public void setPrimerAdyacente(NodoAdyacente unPrimerAdyacente)
        {
                this.primerAdyacente = unPrimerAdyacente;
        }
}
