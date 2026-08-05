package estructuras;

/**
 * Implementación del TDA Nodo.
 *
 * @author <a href="https://www.github.com/santinux">Santino Fuentes</a>
 * @version 3.0
 */
public class Nodo
{
        private Object elemento;
        private Nodo enlace;
        
        /**
         * Constructor con parámetros de la clase Nodo.
         * Inicializa un nodo a partir del elemento y enlace indicados.
         *
         * @param unElemento El elemento a almacenar en el nodo.
         * @param unEnlace   El nodo al que enlaza.
         */
        public Nodo(Object unElemento, Nodo unEnlace)
        {
                this.elemento = unElemento;
                this.enlace = unEnlace;
        }
        
        /**
         * Establece el elemento del nodo.
         *
         * @param unElemento El nuevo elemento a almacenar.
         */
        public void setElemento(Object unElemento)
        {
                this.elemento = unElemento;
        }
        
        /**
         * Establece el nodo enlace del nodo.
         *
         * @param unEnlace El nuevo nodo enlace.
         */
        public void setEnlace(Nodo unEnlace)
        {
                this.enlace = unEnlace;
        }
        
        /**
         * Retorna el elemento almacenado en el nodo.
         *
         * @return El elemento del nodo.
         */
        public Object getElemento()
        {
                return (this.elemento);
        }
        
        /**
         * Retorna el nodo enlace del nodo.
         *
         * @return El nodo enlace.
         */
        public Nodo getEnlace()
        {
                return (this.enlace);
        }
}
