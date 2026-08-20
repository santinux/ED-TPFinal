package estructuras;

/**
 * Implementación del TDA Árbol AVL.
 *
 * @author <a href="https://www.github.com/santinux">Santino Fuentes</a>
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ArbolAVL implements Cloneable
{
        private NodoAVL raiz;
        
        public ArbolAVL()
        {
                this.raiz = null;
        }
        
        public boolean insertar(Comparable unElemento)
        {
                boolean exito = false;
                if (this.raiz == null) {
                        this.raiz = new NodoAVL(unElemento);
                        exito = true;
                } else {
                        exito = insertarAux(this.raiz, unElemento);
                        balancear(balance(this.raiz), this.raiz, null);
                        this.raiz.recalcularAltura();
                }
                return (exito);
        }
        
        private boolean insertarAux(NodoAVL unNodo, Comparable unElemento)
        {
                boolean exito = false;
                if (unNodo != null) {
                        int comparacion = unElemento.compareTo(unNodo.getElemento());
                        if (comparacion < 0) {
                                // El elemento a insertar es menor que el elemento del nodo actual
                                if (unNodo.getHijoIzquierdo() == null) {
                                        // Inserta a su izquierda
                                        unNodo.setHijoIzquierdo(new NodoAVL(unElemento));
                                        exito = true;
                                        unNodo.recalcularAltura();
                                } else {
                                        // Baja por la rama izquierda hasta llegar al último nodo
                                        exito = insertarAux(unNodo.getHijoIzquierdo(), unElemento);
                                        if (exito) {
                                                balancear(balance(unNodo.getHijoIzquierdo()), unNodo.getHijoIzquierdo(), unNodo);
                                                unNodo.recalcularAltura();
                                        }
                                }
                        } else if (comparacion > 0) {
                                // El elemento a insertar es mayor que el elemento del nodo actual
                                if (unNodo.getHijoDerecho() == null) {
                                        // Inserta a su derecha
                                        unNodo.setHijoDerecho(new NodoAVL(unElemento));
                                        exito = true;
                                        unNodo.recalcularAltura();
                                } else {
                                        // Baja por la rama derecha hasta llegar al último nodo
                                        exito = insertarAux(unNodo.getHijoDerecho(), unElemento);
                                        if (exito) {
                                                balancear(balance(unNodo.getHijoDerecho()), unNodo.getHijoDerecho(), unNodo);
                                                unNodo.recalcularAltura();
                                        }
                                }
                        }
                        // Si al comparar retorna 0, es porque son iguales
                        // No se aceptan elementos duplicados, retorna false
                }
                return (exito);
        }
        
        public boolean eliminar(Comparable unElemento)
        {
                boolean exito = false;
                if (this.raiz != null)
                        // Se busca desde la raíz, no tiene padre (null)
                        exito = eliminarAux(unElemento, this.raiz, null);
                return (exito);
        }
        
        private boolean eliminarAux(Comparable unElemento, NodoAVL unNodo, NodoAVL unNodoPadre)
        {
                boolean eliminado = false;
                if (unNodo != null) {
                        int comparacion = unElemento.compareTo(unNodo.getElemento());
                        if (comparacion < 0) {
                                eliminado = eliminarAux(unElemento, unNodo.getHijoIzquierdo(), unNodo);
                                if (eliminado) {
                                        balancear(balance(unNodo.getHijoIzquierdo()), unNodo.getHijoIzquierdo(), unNodo);
                                }
                        } else if (comparacion > 0) {
                                eliminado = eliminarAux(unElemento, unNodo.getHijoDerecho(), unNodo);
                                if (eliminado) {
                                        balancear(balance(unNodo.getHijoDerecho()), unNodo.getHijoDerecho(), unNodo);
                                }
                        } else {
                                if (unNodo.getHijoIzquierdo() == null && unNodo.getHijoDerecho() == null) {
                                        caso1Eliminar(unElemento, unNodoPadre);
                                } else if (unNodo.getHijoIzquierdo() == null || unNodo.getHijoDerecho() == null) {
                                        caso2Eliminar(unElemento, unNodo, unNodoPadre);
                                } else {
                                        caso3Eliminar(unNodo, unNodo.getHijoDerecho(), unNodo.getHijoDerecho());
                                        balancear((balance(unNodo.getHijoDerecho())), unNodo.getHijoDerecho(), unNodo);
                                }
                                eliminado = true;
                        }
                        if (eliminado && unNodoPadre != null) {
                                unNodo.recalcularAltura();
                                unNodoPadre.recalcularAltura();
                        }
                        
                }
                return eliminado;
        }
        
        /**
         * El nodo a eliminar no tiene hijos.
         *
         * @param elemento
         * @param padre
         */
        private void caso1Eliminar(Comparable elemento, NodoAVL padre)
        {
                if (padre == null) {
                        this.raiz = null;
                } else {
                        if (elemento.compareTo(padre.getElemento()) < 0) {
                                padre.setHijoIzquierdo(null);
                        } else {
                                padre.setHijoDerecho(null);
                        }
                }
        }
        
        /**
         * El nodo a eliminar tiene un solo hijo.
         *
         * @param elem
         * @param nodo
         * @param padre
         */
        private void caso2Eliminar(Comparable elem, NodoAVL nodo, NodoAVL padre)
        {
                // Buscar el candidato para reemplazar al nodo
                NodoAVL derecho = nodo.getHijoDerecho();
                NodoAVL izquierdo = nodo.getHijoIzquierdo();
                if (padre == null) {
                        // El nodo es la raíz
                        if (derecho == null) {
                                this.raiz = izquierdo;
                        } else {
                                this.raiz = derecho;
                        }
                } else {
                        // Verificar la rama derecha o izquierda.
                        if (elem.compareTo(padre.getElemento()) < 0) {
                                if (izquierdo == null) {
                                        padre.setHijoIzquierdo(derecho);
                                } else {
                                        padre.setHijoIzquierdo(izquierdo);
                                }
                        } else {
                                if (izquierdo == null) {
                                        padre.setHijoDerecho(derecho);
                                } else {
                                        padre.setHijoDerecho(izquierdo);
                                }
                        }
                }
        }
        
        /**
         * El nodo a eliminar tiene dos hijos.
         *
         * @param raiz
         * @param padre
         * @param nodo
         */
        private void caso3Eliminar(NodoAVL raiz, NodoAVL padre, NodoAVL nodo)
        {
                NodoAVL candidato;
                if (nodo.getHijoIzquierdo() == null) {
                        candidato = nodo;
                } else {
                        candidato = nodo.getHijoIzquierdo();
                }
                if (candidato.getHijoIzquierdo() != null) {
                        caso3Eliminar(raiz, nodo, candidato);
                } else {
                        raiz.setElemento(candidato.getElemento());
                        NodoAVL derecho = candidato.getHijoDerecho();
                        if (raiz.getHijoDerecho() == candidato) {
                                raiz.setHijoDerecho(derecho);
                        } else {
                                padre.setHijoIzquierdo(derecho);
                        }
                }
                raiz.recalcularAltura();
                padre.recalcularAltura();
        }
        
        /**
         * Calcula y retorna el balance de un nodo.
         *
         * @param unNodo
         * @return Balance del nodo
         */
        private int balance(NodoAVL unNodo)
        {
                int alturaHI = -1;
                int alturaHD = -1;
                int balance = 0;
                if (unNodo != null) {
                        if (unNodo.getHijoIzquierdo() != null)
                                alturaHI = unNodo.getHijoIzquierdo().getAltura();
                        if (unNodo.getHijoDerecho() != null)
                                alturaHD = unNodo.getHijoDerecho().getAltura();
                        balance = alturaHI - alturaHD;
                }
                return (balance);
        }
        
        /**
         * Realiza las rotaciones necesarias para lograr el balance de un nodo.
         *
         * @param balancePadre
         * @param unNodo
         * @param unNodoPadre
         */
        private void balancear(int balancePadre, NodoAVL unNodo, NodoAVL unNodoPadre)
        {
                int balanceHijo;
                if (balancePadre == 2) {
                        balanceHijo = balance(unNodo.getHijoIzquierdo());
                        if (balanceHijo == 0 || balanceHijo == 1) {
                                if (unNodoPadre == null) {
                                        this.raiz = rotarDerecha(unNodo);
                                } else {
                                        if (unNodo.getElemento().compareTo(unNodoPadre.getElemento()) < 0) {
                                                unNodoPadre.setHijoIzquierdo(rotarDerecha(unNodo));
                                        } else {
                                                unNodoPadre.setHijoDerecho(rotarDerecha(unNodo));
                                        }
                                }
                        } else if (unNodoPadre == null) {
                                this.raiz = rotarIzquierdaDerecha(unNodo);
                        } else {
                                if (unNodo.getElemento().compareTo(unNodoPadre.getElemento()) < 0) {
                                        unNodoPadre.setHijoIzquierdo(rotarIzquierdaDerecha(unNodo));
                                } else {
                                        unNodoPadre.setHijoDerecho(rotarIzquierdaDerecha(unNodo));
                                }
                        }
                } else if (balancePadre == -2) {
                        balanceHijo = balance(unNodo.getHijoDerecho());
                        if (balanceHijo == 0 || balanceHijo == -1) {
                                if (unNodoPadre == null) {
                                        this.raiz = rotarIzquierda(unNodo);
                                } else {
                                        if (unNodoPadre.getElemento().compareTo(unNodo.getElemento()) < 0) {
                                                unNodoPadre.setHijoDerecho(rotarIzquierda(unNodo));
                                        } else {
                                                unNodoPadre.setHijoIzquierdo(rotarIzquierda(unNodo));
                                        }
                                }
                        } else {
                                if (unNodoPadre == null) {
                                        this.raiz = rotarDerechaIzquierda(unNodo);
                                } else {
                                        if (unNodoPadre.getElemento().compareTo(unNodo.getElemento()) < 0) {
                                                unNodoPadre.setHijoDerecho(rotarDerechaIzquierda(unNodo));
                                        } else {
                                                unNodoPadre.setHijoIzquierdo(rotarDerechaIzquierda(unNodo));
                                        }
                                }
                        }
                }
        }
        
        /**
         * Rotación simple a izquierda.
         *
         * @param unNodo
         * @return
         */
        private NodoAVL rotarIzquierda(NodoAVL unNodo)
        {
                NodoAVL hijo = unNodo.getHijoDerecho();
                NodoAVL temp = hijo.getHijoIzquierdo();
                hijo.setHijoIzquierdo(unNodo);
                unNodo.setHijoDerecho(temp);
                unNodo.recalcularAltura();
                hijo.recalcularAltura();
                return (hijo);
        }
        
        /**
         * Rotación simple a derecha.
         *
         * @param unNodo
         * @return
         */
        private NodoAVL rotarDerecha(NodoAVL unNodo)
        {
                NodoAVL hijo = unNodo.getHijoIzquierdo();
                NodoAVL temp = hijo.getHijoDerecho();
                hijo.setHijoDerecho(unNodo);
                unNodo.setHijoIzquierdo(temp);
                unNodo.recalcularAltura();
                hijo.recalcularAltura();
                return (hijo);
        }
        
        /**
         * Rotación doble izquierda derecha.
         * El nodo padre está caído a la izquierda y el nodo hijo está caído
         * a la derecha.
         *
         * @param unNodo
         * @return Nuevo nodoo raíz del subarbol
         */
        private NodoAVL rotarIzquierdaDerecha(NodoAVL unNodo)
        {
                unNodo.setHijoIzquierdo(rotarIzquierda(unNodo.getHijoIzquierdo()));
                return rotarDerecha(unNodo);
        }
        
        /**
         * Rotación doble derecha izquierda.
         * El nodo padre está caído a la derecha y el nodo hijo está caído
         * a la izquierda.
         *
         * @param unNodo
         * @return Nuevo nodo raíz del subarbol
         */
        private NodoAVL rotarDerechaIzquierda(NodoAVL unNodo)
        {
                unNodo.setHijoDerecho(rotarDerecha(unNodo.getHijoDerecho()));
                return rotarIzquierda(unNodo);
        }
        
        private Comparable padre(Comparable unElemento)
        {
                Comparable elementoPadre = null;
                if (this.raiz != null)
                        elementoPadre = padreAux(this.raiz, unElemento);
                return (elementoPadre);
        }
        
        private Comparable padreAux(NodoAVL unNodo, Comparable unElemento)
        {
                Comparable elementoPadre = null;
                if (unNodo != null) {
                        if (unElemento.compareTo(unNodo.getHijoIzquierdo().getElemento()) == 0
                                || unElemento.compareTo(unNodo.getHijoDerecho().getElemento()) == 0) {
                                // El elemento de alguno de sus hijos coincide con el buscado
                                elementoPadre = unNodo.getElemento();
                        } else if (unElemento.compareTo(unNodo.getElemento()) < 0) {
                                // El elemento buscado es menor que el del nodo actual
                                elementoPadre = padreAux(unNodo.getHijoIzquierdo(), unElemento);
                        } else {
                                // El elemento buscado es mayor que el del nodo actual
                                elementoPadre = padreAux(unNodo.getHijoDerecho(), unElemento);
                        }
                }
                return (elementoPadre);
        }
        
        public boolean pertenece(Comparable unElemento)
        {
                boolean exito = false;
                if (this.raiz != null)
                        exito = perteneceAux(this.raiz, unElemento);
                return (exito);
        }
        
        private boolean perteneceAux(NodoAVL unNodo, Comparable unElemento)
        {
                boolean exito = false;
                if (unNodo != null) {
                        int comparacion = unElemento.compareTo(unNodo.getElemento());
                        if (comparacion == 0) {
                                // El elemento en el nodo actual coincide con el buscado
                                exito = true;
                        } else if (comparacion < 0) {
                                // El elemento a buscar es menor que el elemento del nodo actual
                                exito = perteneceAux(unNodo.getHijoIzquierdo(), unElemento);
                        } else {
                                // El elemento a buscar es mayor que el elemento del nodo actual
                                exito = perteneceAux(unNodo.getHijoDerecho(), unElemento);
                        }
                }
                return (exito);
        }
        
        /**
         * Parece redundante, pero es útil si se busca un elemento solo con
         * la clave para comparar.
         *
         * @param unElemento El elemento a buscar, solo con una clave para comparar.
         * @return El elemento correspondiente al elemento buscado, o null si no se encuentra.
         */
        public Comparable obtenerElemento(Comparable unElemento)
        {
                Comparable elemento = null;
                if (this.raiz != null)
                        elemento = obtenerElementoAux(this.raiz, unElemento);
                return (elemento);
        }
        
        private Comparable obtenerElementoAux(NodoAVL unNodo, Comparable unElemento)
        {
                Comparable elemento = null;
                int comparacion = unElemento.compareTo(unNodo.getElemento());
                if (unNodo != null) {
                        if (comparacion == 0) {
                                // El elemento en el nodo actual coincide con el buscado
                                elemento = unNodo.getElemento();
                        } else if (comparacion < 0) {
                                // El elemento a buscar es menor que el elemento del nodo actual
                                elemento = obtenerElementoAux(unNodo.getHijoIzquierdo(), unElemento);
                        } else {
                                // El elemento a buscar es mayor que el elemento del nodo actual
                                elemento = obtenerElementoAux(unNodo.getHijoDerecho(), unElemento);
                        }
                }
                return (elemento);
        }
        
        public Comparable minimoElemento()
        {
                return (minimoElementoAux(this.raiz));
        }
        
        private Comparable minimoElementoAux(NodoAVL unNodo)
        {
                Comparable minimo = null;
                if (unNodo != null) {
                        if (unNodo.getHijoIzquierdo() == null) {
                                // Si no tiene hijo izquierdo, el nodo tiene el mínimo elemento
                                minimo = unNodo.getElemento();
                        } else {
                                // Si tiene hijo izquierdo, busca en esa rama
                                minimo = minimoElementoAux(unNodo.getHijoIzquierdo());
                        }
                }
                return (minimo);
        }
        
        public Comparable maximoElemento()
        {
                return (maximoElementoAux(this.raiz));
        }
        
        private Comparable maximoElementoAux(NodoAVL unNodo)
        {
                Comparable maximo = null;
                if (unNodo != null) {
                        if (unNodo.getHijoDerecho() == null) {
                                maximo = unNodo.getElemento();
                        } else {
                                maximo = maximoElementoAux(unNodo.getHijoDerecho());
                        }
                }
                return (maximo);
        }
        
        public boolean esVacio()
        {
                return (this.raiz == null);
        }
        
        public void vaciar()
        {
                this.raiz = null;
        }
        
        public Lista listar()
        {
                Lista listaInorden = new Lista();
                if (this.raiz != null)
                        listarAux(this.raiz, listaInorden);
                return (listaInorden);
        }
        
        private void listarAux(NodoAVL unNodo, Lista unaLista)
        {
                if (unNodo != null) {
                        listarAux(unNodo.getHijoIzquierdo(), unaLista);
                        unaLista.insertar(unNodo.getElemento(), unaLista.longitud() + 1);
                        listarAux(unNodo.getHijoDerecho(), unaLista);
                }
        }
        
        public Lista listarRango(Comparable elementoMinimo, Comparable elementoMaximo)
        {
                Lista listaRango = new Lista();
                if (this.raiz != null)
                        listarRangoAux(this.raiz, listaRango, elementoMinimo, elementoMaximo);
                return (listaRango);
        }
        
        private void listarRangoAux(NodoAVL unNodo, Lista unaLista, Comparable unElemMin, Comparable unElemMax)
        {
                if (unNodo != null) {
                        if (unNodo.getElemento().compareTo(unElemMin) > 0)
                                // El elemento del nodo actual es mayor que el mínimo, recorre su HI
                                listarRangoAux(unNodo.getHijoIzquierdo(), unaLista, unElemMin, unElemMax);
                        if (unNodo.getElemento().compareTo(unElemMin) >= 0 && unNodo.getElemento().compareTo(unElemMax) <= 0)
                                // El elemento del nodo actual está dentro del rango a listar, se inserta
                                unaLista.insertar(unNodo.getElemento(), unaLista.longitud() + 1);
                        if (unNodo.getElemento().compareTo(unElemMax) < 0)
                                // El elemento del nodo actual es menor que el máximo, recorre su HD
                                listarRangoAux(unNodo.getHijoDerecho(), unaLista, unElemMin, unElemMax);
                }
        }
        
        @SuppressWarnings("CloneDoesntCallSuperClone")
        @Override
        public ArbolAVL clone()
        {
                ArbolAVL dolly = new ArbolAVL();
                if (this.raiz != null)
                        cloneAux(this.raiz, dolly);
                return (dolly);
        }
        
        private void cloneAux(NodoAVL unNodo, ArbolAVL unArbol)
        {
                if (unNodo != null) {
                        unArbol.insertar(unNodo.getElemento());
                        cloneAux(unNodo.getHijoIzquierdo(), unArbol);
                        cloneAux(unNodo.getHijoDerecho(), unArbol);
                }
        }
        
        /**
         * Genera una cadena de caracteres formada por todos los nodos del árbol,
         * mostrando para cada uno su elemento, hijo izquierdo e hijo derecho.
         *
         * @return Cadena con los nodos del árbol.
         */
        @Override
        public String toString()
        {
                StringBuilder arbolString = new StringBuilder("[\n");
                if (this.raiz != null)
                        toStringAux(this.raiz, arbolString);
                return (arbolString.append("]").toString());
        }
        
        /**
         * Helper de toString().
         *
         * @param unNodo   Nodo que recorrerá la estructura.
         * @param unString Cadena en la que se escribirán los nodos encontrados.
         */
        private void toStringAux(NodoAVL unNodo, StringBuilder unString)
        {
                if (unNodo != null) {
                        unString.append("[ ")
                                .append(unNodo.getElemento())
                                .append(" | HI: *")
                                .append(unNodo.getHijoIzquierdo() != null ? unNodo.getHijoIzquierdo().getElemento() : "null")
                                .append(" | HD: *")
                                .append(unNodo.getHijoDerecho() != null ? unNodo.getHijoDerecho().getElemento() : "null")
                                .append(" ]\n");
                        toStringAux(unNodo.getHijoIzquierdo(), unString);
                        toStringAux(unNodo.getHijoDerecho(), unString);
                }
        }
}
