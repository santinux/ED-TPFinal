package estructuras;

/**
 * Implementación del TDA Cola Dinámica
 *
 * @author <a href="https://www.github.com/santinux">Santino Fuentes</a>
 * @version 3.0
 */
public class Cola implements Cloneable
{
        /**
         * Consideraciones ligadas a la implementación estática:
         * - Cola vacía: frente == null.
         */
        private Nodo frente;
        private Nodo fin;
        
        /**
         * Constructor por defecto de la clase Cola.
         * Inicializa una cola vacía estableciendo el frente y el final en null.
         */
        public Cola()
        {
                this.frente = null;
                this.fin = null;
        }
        
        /**
         * Coloca un nuevo elemento en el final de la cola.
         *
         * @param unElemento El elemento que se desea colocar en la cola.
         * @return true si la operación fue exitosa, siempre lo es en cola dinámica.
         */
        public boolean poner(Object unElemento)
        {
                Nodo nuevoNodo = new Nodo(unElemento, null);
                // Si el frente es nulo, la cola está vacía
                if (this.frente == null) {
                        this.frente = nuevoNodo;
                } else {
                        this.fin.setEnlace(nuevoNodo);
                }
                // El final de la cola apunta al nuevo nodo
                this.fin = nuevoNodo;
                return (true);
        }
        
        /**
         * Quita el elemento en el frente de la cola, si no está vacía.
         *
         * @return true si la operación fue exitosa, en cola vacía retorna false.
         */
        public boolean sacar()
        {
                boolean exito = false;
                // Si el frente es nulo, la cola está vacía y no puede sacar
                if (this.frente != null) {
                        // Si el nodo no tiene enlaces, la cola quedará vacía,
                        // sino, el frente apuntará al enlace del siguiente nodo
                        if (this.frente.getEnlace() == null) {
                                this.frente = null;
                        } else {
                                this.frente = this.frente.getEnlace();
                        }
                        exito = true;
                }
                return (exito);
        }
        
        /**
         * Retorna el elemento en el frente de la cola.
         *
         * @return El elemento ubicado en el frente, null si está vacía.
         */
        public Object obtenerFrente()
        {
                Object elementoEnFrente = null;
                if (this.frente != null)
                        elementoEnFrente = this.frente.getElemento();
                return (elementoEnFrente);
        }
        
        /**
         * Verifica si la cola está vacía, sin elementos.
         *
         * @return true si la cola no contiene elementos, false en caso contrario.
         */
        public boolean esVacia()
        {
                return (this.frente == null);
        }
        
        /**
         * Vacía completamente la cola, estableciendo su frente y final en null.
         */
        public void vaciar()
        {
                this.frente = null;
                this.fin = null;
        }
        
        /**
         * Retorna una copia exacta de los datos en la estructura original y
         * respetando el orden de los mismos, en otra estructura del mismo tipo.
         *
         * @return Un clon de la cola original (como la oveja Dolly).
         */
        @Override
        @SuppressWarnings("CloneDoesntCallSuperClone")
        public Cola clone()
        {
                Cola dolly = new Cola();
                if (this.frente != null) {
                        cloneAux(this.frente, dolly);
                }
                return (dolly);
        }
        
        /**
         * Helper de clone().
         *
         * @param unNodo  Nodo que recorrerá la estructura.
         * @param unaCola Cola en la que se copiarán los elementos.
         */
        private void cloneAux(Nodo unNodo, Cola unaCola)
        {
                if (unNodo != null) {
                        unaCola.poner(unNodo.getElemento());
                        cloneAux(unNodo.getEnlace(), unaCola);
                }
        }
        
        /**
         * Genera una cadena de caracteres formada por todos los elementos de la
         * cola en formato [1,2,3], siendo '1' el frente y '3' el final.
         *
         * @return Cadena con los elementos de la cola.
         */
        @Override
        public String toString()
        {
                StringBuilder colaString = new StringBuilder("[");
                if (this.frente != null)
                        toStringAux(this.frente, colaString);
                return (colaString.append("]").toString());
        }
        
        /**
         * Helper de toString().
         *
         * @param unNodo   Nodo que recorrerá la estructura.
         * @param unString Cadena en la que se escribirán los elementos encontrados.
         */
        private void toStringAux(Nodo unNodo, StringBuilder unString)
        {
                if (unNodo != null) {
                        unString.append(unNodo.getElemento());
                        if (unNodo.getEnlace() != null)
                                unString.append(",");
                        toStringAux(unNodo.getEnlace(), unString);
                }
        }
}
