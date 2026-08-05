package estructuras;

/**
 * Implementación del TDA Lista.
 *
 * @author <a href="https://www.github.com/santinux">Santino Fuentes</a>
 * @version 3.0
 */
public class Lista implements Cloneable
{
        /**
         * Consideraciones ligadas a la implementación:
         * - Lista vacía: cabecera == null.
         */
        private Nodo cabecera;
        
        /**
         * Constructor por defecto de la clase Lista.
         * Inicializa una lista vacía estableciendo la cabecera en null.
         */
        public Lista()
        {
                this.cabecera = null;
        }
        
        /**
         * Inserta un elemento en la posición indicada de la lista.
         * Los elementos que se encontraban en esa posición y en las siguientes
         * son desplazados una posición hacia adelante.
         *
         * @param unElemento  El elemento a insertar.
         * @param unaPosicion La posición en la que se insertará el elemento (debe ser >= 1 y <= longitud + 1).
         * @return true si la operación fue exitosa, false en caso contrario.
         */
        public boolean insertar(Object unElemento, int unaPosicion)
        {
                boolean exito = false;
                // La posición a insertar no debe exceder los límites de la estructura
                if (unaPosicion >= 1 && unaPosicion <= this.longitud() + 1) {
                        if (unaPosicion == 1) {
                                // Primera posición en la lista
                                this.cabecera = new Nodo(unElemento, this.cabecera);
                                exito = true;
                        } else {
                                // Cualquier otra posición de la lista
                                exito = insertarAux(this.cabecera, unElemento, unaPosicion);
                                
                                /* Enfoque iterativo
                                Nodo nodoAux = this.cabecera;
                                int posicionActual = 1;
                                // Navegar la lista hasta llegar a la posición anterior a la deseada
                                while (nodoAux != null) {
                                        if (posicionActual == unaPosicion - 1) {
                                                nodoAux.setEnlace(new Nodo(unElemento, nodoAux.getEnlace()));
                                                exito = true;
                                        }
                                        nodoAux = nodoAux.getEnlace();
                                }
                                 */
                        }
                }
                return (exito);
        }
        
        /**
         * Helper de insertar().
         *
         * @param unNodo      Nodo desde el cual iniciar la navegación.
         * @param unElemento  El elemento a insertar.
         * @param unaPosicion La posición relativa al nodo actual en la que se insertará.
         * @return true si la operación fue exitosa, false en caso contrario.
         */
        private boolean insertarAux(Nodo unNodo, Object unElemento, int unaPosicion)
        {
                boolean exito = false;
                if (unNodo != null) {
                        if (unaPosicion == 2) {
                                // Posición anterior a la deseada
                                unNodo.setEnlace(new Nodo(unElemento, unNodo.getEnlace()));
                                exito = true;
                        } else {
                                // Navegar la lista decrementando la posición hasta llegar a la posición anterior a la deseada
                                // También se recupera el valor retornado para exito
                                exito = insertarAux(unNodo.getEnlace(), unElemento, unaPosicion - 1);
                        }
                }
                return (exito);
        }
        
        /**
         * Elimina el elemento que se encuentra en la posición indicada de la lista.
         * Los elementos que se encontraban en posiciones posteriores son desplazados
         * una posición hacia atrás.
         *
         * @param unaPosicion La posición del elemento a eliminar (debe ser >= 1 y <= longitud).
         * @return true si la operación fue exitosa, false en caso contrario.
         */
        public boolean eliminar(int unaPosicion)
        {
                boolean exito = false;
                // La posición a eliminar no debe exceder los límites de la estructura
                if (unaPosicion >= 1 && unaPosicion <= this.longitud()) {
                        if (unaPosicion == 1) {
                                this.cabecera = this.cabecera.getEnlace();
                                exito = true;
                        } else {
                                exito = eliminarAux(this.cabecera, unaPosicion);
                        }
                }
                return (exito);
        }
        
        /**
         * Helper de eliminar().
         *
         * @param unNodo      Nodo desde el cual iniciar la navegación.
         * @param unaPosicion La posición relativa al nodo actual del elemento a eliminar.
         * @return true si la operación fue exitosa, false en caso contrario.
         */
        private boolean eliminarAux(Nodo unNodo, int unaPosicion)
        {
                boolean exito = false;
                if (unNodo != null) {
                        if (unaPosicion == 2) {
                                // Posición anterior a la deseada
                                unNodo.setEnlace(unNodo.getEnlace().getEnlace());
                                exito = true;
                        } else {
                                // Navegar la lista decrementando la posición hasta llegar a la posición anterior a la deseada
                                // También se recupera el valor retornado para exito
                                exito = eliminarAux(unNodo.getEnlace(), unaPosicion - 1);
                        }
                }
                return (exito);
        }
        
        /**
         * Retorna el elemento almacenado en la posición indicada de la lista.
         *
         * @param unaPosicion La posición del elemento a recuperar (debe ser >= 1 y <= longitud).
         * @return El elemento en la posición indicada, null si la posición es inválida.
         */
        public Object recuperar(int unaPosicion)
        {
                Object elementoEnPosicion = null;
                // La posición a recuperar no debe exceder los límites de la estructura
                if (unaPosicion >= 1 && unaPosicion <= this.longitud()) {
                        if (unaPosicion == 1) {
                                elementoEnPosicion = this.cabecera.getElemento();
                        } else {
                                elementoEnPosicion = recuperarAux(this.cabecera, unaPosicion);
                        }
                }
                return (elementoEnPosicion);
        }
        
        /**
         * Helper de recuperar().
         *
         * @param unNodo      Nodo desde el cual iniciar la navegación.
         * @param unaPosicion La posición relativa al nodo actual del elemento a recuperar.
         * @return El elemento en la posición indicada, null si no se encuentra.
         */
        private Object recuperarAux(Nodo unNodo, int unaPosicion)
        {
                Object elemento = null;
                if (unNodo != null) {
                        if (unaPosicion == 1) {
                                elemento = unNodo.getElemento();
                        } else {
                                elemento = recuperarAux(unNodo.getEnlace(), unaPosicion - 1);
                        }
                }
                return (elemento);
        }
        
        /**
         * Retorna la posición de la primera ocurrencia del elemento indicado en la lista.
         *
         * @param unElemento El elemento a buscar.
         * @return La posición del elemento (>= 1) si se encontró, -1 en caso contrario.
         */
        public int localizar(Object unElemento)
        {
                return (localizarAux(this.cabecera, unElemento));
        }
        
        /**
         * Helper de localizar().
         *
         * @param unNodo     Nodo desde el cual iniciar la búsqueda.
         * @param unElemento El elemento a buscar.
         * @return La posición relativa del elemento si se encontró, -1 en caso contrario.
         */
        private int localizarAux(Nodo unNodo, Object unElemento)
        {
                int posicion = -1;
                if (unNodo != null) {
                        if (unElemento == null && unNodo.getElemento() == null) {
                                // Si el elemento buscado es null se asigna 1 porque se encontró
                                posicion = 1;
                        } else if (unNodo.getElemento() != null && unNodo.getElemento().equals(unElemento)) {
                                // Si el elemento buscado se encontró
                                posicion = 1;
                        } else {
                                // Navegar la lista hasta encontrar el elemento
                                posicion = localizarAux(unNodo.getEnlace(), unElemento);
                                // Si en el desapilado de la recursión se devolvió la posición encontrada, se va sumando
                                if (posicion != -1) posicion++;
                        }
                }
                return (posicion);
        }
        
        /**
         * Retorna la cantidad de elementos almacenados en la lista.
         *
         * @return La longitud de la lista.
         */
        public int longitud()
        {
                int longitud = 0;
                if (this.cabecera != null)
                        longitud = longitudAux(this.cabecera);
                return (longitud);
        }
        
        /**
         * Helper de longitud().
         *
         * @param unNodo Nodo desde el cual contar los elementos.
         * @return La cantidad de elementos a partir del nodo indicado.
         */
        private int longitudAux(Nodo unNodo)
        {
                int longitud = 0;
                if (unNodo != null) {
                        longitud = longitudAux(unNodo.getEnlace()) + 1;
                }
                return (longitud);
        }
        
        /**
         * Verifica si la lista está vacía, sin elementos.
         *
         * @return true si la lista no contiene elementos, false en caso contrario.
         */
        public boolean esVacia()
        {
                return (this.cabecera == null);
        }
        
        /**
         * Vacía completamente la lista, estableciendo su cabecera en null.
         */
        public void vaciar()
        {
                this.cabecera = null;
        }
        
        /**
         * Retorna una copia exacta de los datos en la estructura original,
         * en otra estructura del mismo tipo.
         *
         * @return Un clon de la lista original (como la oveja Dolly).
         */
        @Override
        @SuppressWarnings("CloneDoesntCallSuperClone")
        public Lista clone()
        {
                Lista dolly = new Lista();
                if (this.cabecera != null)
                        cloneAux(this.cabecera, 1, dolly);
                return (dolly);
        }
        
        /**
         * Helper de clone().
         *
         * @param unNodo      Nodo que recorrerá la estructura original.
         * @param unaPosicion Posición en la que se insertará el elemento en la lista clonada.
         * @param unaLista    La lista clonada en construcción.
         */
        private void cloneAux(Nodo unNodo, int unaPosicion, Lista unaLista)
        {
                if (unNodo != null) {
                        unaLista.insertar(unNodo.getElemento(), unaPosicion);
                        cloneAux(unNodo.getEnlace(), unaPosicion + 1, unaLista);
                }
        }
        
        /**
         * Genera una cadena de caracteres formada por todos los elementos de la
         * lista en formato [1,2,3], siendo '1' la cabecera.
         *
         * @return Cadena con los elementos de la lista.
         */
        @Override
        public String toString()
        {
                StringBuilder listaString = new StringBuilder("[");
                if (this.cabecera != null)
                        toStringAux(this.cabecera, listaString);
                return (listaString.append("]").toString());
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
