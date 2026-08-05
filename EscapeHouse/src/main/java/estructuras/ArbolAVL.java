package estructuras;

public class ArbolAVL
{
    private NodoAVL raiz;

    /**
     * Inserta un elemento en la pocision correspondiente retorna falso si el
     * elemento ya existe
     *
     * @param elemento
     * @return
     */
    public boolean insertar(Comparable elemento) {
        boolean exito = false;
        if (this.raiz == null) {
            // en caso de no tener raiz la insertamos y retornamos true
            this.raiz = new NodoAVL(elemento);
            exito = true;
        } else {
            if (this.raiz.getElemento().compareTo(elemento) != 0) {
                if (this.raiz.getElemento().compareTo(elemento) > 0) {
                    // vamos a relizar la insercion por la izquierda de la raiz
                    exito = insertarAux(this.raiz, raiz.getIzquierdo(), 'I', elemento);
                } else {
                    // vamos a realizar la insercion por la derecha de la raiz
                    exito = insertarAux(this.raiz, raiz.getDerecho(), 'D', elemento);
                }
            }
            if (exito) {
                this.raiz.recalcularAltura();
                balancearRaiz(this.raiz);
            }
        }
        return exito;
    }

    /**
     *
     * metodo auxiliar de inserrecibe un subraiz para insertar
     *
     */
    private boolean insertarAux(NodoAVL padre, NodoAVL actual, char ladoHijo, Comparable elem) {
        // comparamos con el elemento actual
        boolean exito = false;

        if (actual != null) {
            if (actual.getElemento().compareTo(elem) != 0) {
                if (actual.getElemento().compareTo(elem) > 0) {
                    // nos movemos a la izquierda de la subRaiz
                    exito = insertarAux(actual, actual.getIzquierdo(), 'I', elem);
                } else {
                    // nos movemos a la derecha de la subRaiz
                    exito = insertarAux(actual, actual.getDerecho(), 'D', elem);
                }
            }
        } else {
            // creamos y enlazamos en nodo
            NodoAVL nuevoHIjo = new NodoAVL(elem);
            actual = nuevoHIjo;
            // enlazamos el padre con el hijo
            if (ladoHijo == 'I') {
                padre.setIzquierdo(nuevoHIjo);
            } else {
                padre.setDerecho(nuevoHIjo);
            }

            exito = true;
        }

        if (exito) {
            actual.recalcularAltura();
            balancear(padre, actual, ladoHijo);
        }

        return exito;
    }

    /**
     * *
     * elimina un elemento del arbol retorna falso si no logra eliminar dicho
     * elemento (arbolVacio o elemento no existe)
     */
    public boolean eliminar(Comparable elem) {
        boolean retorno = false;

        if (!esVacia()) {
            if (this.raiz.getElemento().compareTo(elem) == 0) {
                // eliminamos la raiz por lo cual se refefine dicha raiz
                this.raiz = eliminarRaiz(this.raiz);
                retorno = true;
            } else {
                // en caso de que no sea la raiz recorremos los hijos en preorden
                if (this.raiz.getElemento().compareTo(elem) > 0) {
                    retorno = eliminarAux(this.raiz, this.raiz.getIzquierdo(), elem, 'I');
                } else {
                    retorno = eliminarAux(this.raiz, this.raiz.getDerecho(), elem, 'D');
                }
            }
        }

        if (retorno) {
            // balanceamos
            this.raiz.recalcularAltura();
            balancearRaiz(this.raiz);
        }

        return retorno;
    }

    /**
     * *
     * metodo eliminar para el caso de raiz
     */
    private NodoAVL eliminarRaiz(NodoAVL raiz) {
        NodoAVL retorno = null;
        switch (cantidadHijos(raiz)) {
            case 0:
                retorno = null;
                break;
            case 1:
                retorno = retornarHijo(raiz);
                break;
            case 2:
                retorno = mejorCandidato(raiz);
                break;
            default:
                break;
        }
        balance(raiz);
        return retorno;
    }

    /**
     * *
     * metodo para poder eliminar un nodo que no es raiz del arbol operaciones
     * especificadas en el algoritmo
     */
    private boolean eliminarAux(NodoAVL padre, NodoAVL subRaiz, Comparable elem, char hijo) {
        boolean retorno = false;
        if (subRaiz.getElemento().compareTo(elem) == 0) {
            NodoAVL remplazo = null;
            // evaluamos la condicion de dicho subarbol
            switch (cantidadHijos(subRaiz)) {
                case 0:
                    // No tiene hijos
                    remplazo = null;
                    break;
                case 1:
                    // Tiene un hijo
                    remplazo = retornarHijo(subRaiz);
                    break;
                case 2:
                    // Tiene dos hijos, y busca el mejor candidato
                    remplazo = mejorCandidato(subRaiz);
                    break;
                default:
                    break;
            }
            // en caso de que la raiz del subarbol sea el elemento se verifica alguna de las
            // condiciones
            subRaiz = remplazo;
            if (hijo == 'I') {
                padre.setIzquierdo(remplazo);
                retorno = true;
            } else {
                // el subarbol es el hd de el padre
                padre.setDerecho(remplazo);
                retorno = true;
            }
        } else {
            // recorremos en los hijos en preorden
            if (subRaiz.getElemento().compareTo(elem) > 0 && subRaiz.getIzquierdo() != null) {
                retorno = eliminarAux(subRaiz, subRaiz.getIzquierdo(), elem, 'I');
            }
            if (subRaiz.getElemento().compareTo(elem) < 0 && subRaiz.getDerecho() != null) {
                retorno = eliminarAux(subRaiz, subRaiz.getDerecho(), elem, 'D');
            }
        }
        // balancemos
        if (retorno && subRaiz != null) {
            subRaiz.recalcularAltura();
            balancear(padre, subRaiz, hijo);
        }
        return retorno;
    }

    /**
     * *
     * retornamos la cantidad de hijos que posee un nodo
     */
    private int cantidadHijos(NodoAVL nodo) {
        int retorno = 0;
        // pasamos la raiz y evaluamos cuales de los metodos usar
        if (nodo.getIzquierdo() != null && nodo.getDerecho() != null) {
            // tenemos los dos hijos por lo que tenemos que usar el metodo 3
            retorno = 2;
        } else {
            if (raiz.getIzquierdo() != null || raiz.getDerecho() != null) {
                // tiene al menos un hijo usamos el metodo 2
                retorno = 1;
            }
        }
        // en caso de no pasar por alguno de los if esto significa que usa el metodo 1
        return retorno;
    }

    /**
     * *
     * es el segundo metodo de eliminacion el cual se aplica cuando el nodo
     * tiene 2 hijos
     */
    private NodoAVL retornarHijo(NodoAVL nodo) {
        NodoAVL retorno = null;
        if (nodo.getIzquierdo() != null) {
            retorno = nodo.getIzquierdo();
        } else {
            retorno = nodo.getDerecho();
        }
        return retorno;
    }

    /**
     * *
     * el tercer metodo de eliminacion el cual intercabia el valor del nodo
     * actual por el mejor candiato eleguido (preferenteme con menos hijos) de
     * esta forma luego eliminamos el candidato eleguido
     */
    private NodoAVL mejorCandidato(NodoAVL nodo) {
        // obtenemos los candidatos de los cuales sacamos a los hijos
        // el que tenga menor cantidad de hijos es el candidato elegido
        NodoAVL retorno = null;
        boolean exito = false;
        // obtenemos los candidatos
        NodoAVL candidatoA = candidatoIzquierdo(nodo.getIzquierdo());
        NodoAVL candidatoB = candidatoDerecho(nodo.getDerecho());
        // evaluamos cual tiene menos hijos
        int hijosCandidatoA = cantidadHijos(candidatoA);
        int hijosCandidatoB = cantidadHijos(candidatoB);
        // comparamos
        if (hijosCandidatoA <= hijosCandidatoB) {
            // obtenemos el valor de este nodo y eliminamos por ese lado
            nodo.setElemento(candidatoA.getElemento());
            // eliminamos el elemento de candidato a por I
            exito = eliminarAux(nodo, nodo.getIzquierdo(), candidatoA.getElemento(), 'I');
        } else {
            // obtenemos el valor de este nodo y eliminamos por ese lado
            nodo.setElemento(candidatoB.getElemento());
            // eliminamos el elemento de candidato a por D
            exito = eliminarAux(nodo, nodo.getDerecho(), candidatoB.getElemento(), 'D');
        }
        if (exito) {
            retorno = nodo;
        }
        return retorno;
    }

    /**
     * *
     * es el candidato A, es el nodo mas a la derecha del subarbol
     *
     * @param raiz del subarbol pasado
     * @return el nodo mas a la derecha
     */
    private NodoAVL candidatoIzquierdo(NodoAVL raiz) {
        NodoAVL aux = raiz;
        while (aux.getDerecho() != null) {
            aux = aux.getDerecho();
        }
        return aux;
    }

    /**
     * *
     * es el candidato B, nodo mas a a la iquierda del subarbol actual
     *
     * @param raiz del subarbol pasado
     * @return el nodo mas a la izquierda del subarbol
     */
    private NodoAVL candidatoDerecho(NodoAVL raiz) {
        NodoAVL aux = raiz;
        while (aux.getIzquierdo() != null) {
            aux = aux.getIzquierdo();
        }
        return aux;
    }

    // metodos de AVL

    private boolean balancear(NodoAVL padre, NodoAVL nodo, char hijo) {
        // evaluamos el balande de nodo
        int balance = balance(nodo);

        if (balance < -1 || balance > 1) {
            // hay que balancear el nodo
            NodoAVL balanceado = aplicarRotaciones(nodo);
            if (hijo == 'I') {
                padre.setIzquierdo(balanceado);
            } else {
                padre.setDerecho(balanceado);
            }
            padre.recalcularAltura();
        }

        return true;
    }

    /**
     * caso especial de balanceo del nodo raiz
     */
    private boolean balancearRaiz(NodoAVL nodo) {
        // evaluamos el balande de nodo
        int balance = balance(nodo);
        NodoAVL balanceado = null;
        if (balance < -1 || balance > 1) {
            // hay que balancear el nodo
            this.raiz = aplicarRotaciones(nodo);
        }
        this.raiz.recalcularAltura();
        return true;
    }

    /**
     * *
     * retornamos el balance de un nodo
     */
    private int balance(NodoAVL nodo) {
        int alturaIzquierdo = -1;
        int alturaDerecho = -1;

        if (nodo.getIzquierdo() != null) {
            alturaIzquierdo = nodo.getIzquierdo().getAltura();
        }

        if (nodo.getDerecho() != null) {
            alturaDerecho = nodo.getDerecho().getAltura();
        }

        return alturaIzquierdo - alturaDerecho;
    }

    /**
     * *
     * aplicamos las rotacion necesaria para ese nodo en caso de tener que
     * aplicarla
     *
     * nodo al cual hay que aplicar una rotacion
     */
    private NodoAVL aplicarRotaciones(NodoAVL nodo) {
        NodoAVL balanceado = null;
        int balanceNodo = balance(nodo);
        if (balanceNodo == -2 && nodo.getDerecho() != null) {
            // esta caido hacia la derecha
            int balanceHijoDerecho = balance(nodo.getDerecho());
            if (balanceHijoDerecho == -1) {
                // giro simple a la izquierda
                balanceado = giroIzquierda(nodo);
            } else {
                // giro doble izquierda-derecha
                balanceado = dobleDerechaIzquierda(nodo);
            }
        } else {
            if (balanceNodo == 2 && nodo.getIzquierdo() != null) {
                // esta caido hacia la izquierda
                int balanceHijoIzquierda = balance(nodo.getIzquierdo());
                if (balanceHijoIzquierda == 1) {
                    // giro a la derecha simple
                    balanceado = giroDerecha(nodo);
                } else {
                    // giro doble derecha-izquierda
                    balanceado = dobleIzquierdaDerecha(nodo);
                }
            }
        }
        return balanceado;
    }

    // rotaciones
    /**
     * *
     * tenemos que el subarbol esta caido hacia la derecha (-2), y su hijo
     * derecho esta caido hacia la izquierda (1)
     *
     * padre es nodo raiz del subarbol desbalanceado hacia la derecha
     */
    private NodoAVL dobleDerechaIzquierda(NodoAVL padre) {
        // hacemos girar hacia la derecha el hijo
        NodoAVL hijoDerecha = giroDerecha(padre.getDerecho());
        padre.setDerecho(hijoDerecha);
        // hacemos girar padre a la izquierda
        NodoAVL nuevoPadre = giroIzquierda(padre);
        // recalculamos alturas
        padre.recalcularAltura();
        hijoDerecha.recalcularAltura();
        return nuevoPadre;
    }

    /**
     * *
     * tenemos que el subarbol esta caido hacia la izquierda (2), y su hijo
     * izquierdo esta caido hacia la derecha (-1)
     *
     * padre es nodo raiz del subarbol desbalanceado hacia la izquierda
     */
    private NodoAVL dobleIzquierdaDerecha(NodoAVL padre) {
        // hacemos rotar hacia la izquierda al hijo izquierdo de padre
        NodoAVL hijoIzqueirdo = giroIzquierda(padre.getIzquierdo());
        padre.setIzquierdo(hijoIzqueirdo);
        // hacemos rotar hacia la derecha a el padre
        NodoAVL nuevoPadre = giroDerecha(padre);
        // recalculasmos alturas
        padre.recalcularAltura();
        hijoIzqueirdo.recalcularAltura();
        return nuevoPadre;
    }

    /**
     * *
     * En esta rotacion lo que pasa es que se intercambian los enlaces de padre
     * e hijo derecho Esto es el HI del HD de padre pasa a ser el HD de padre y
     * el HI de el HD de padre pasa a aser padre
     */
    private NodoAVL giroDerecha(NodoAVL padre) {
        NodoAVL hijoIzquierdo = padre.getIzquierdo();
        // guardamos el hijo derecho de hijoIzquierdo en un tmp
        NodoAVL temp = null;
        if (hijoIzquierdo.getDerecho() != null) {
            temp = hijoIzquierdo.getDerecho();
        }
        // hacemos que hijo tenga como derecho a su padre
        hijoIzquierdo.setDerecho(padre);
        // hacemos que tmp sea hijo izquierdo de padre
        padre.setIzquierdo(temp);

        padre.recalcularAltura();
        hijoIzquierdo.recalcularAltura();
        return hijoIzquierdo;
    }

    /**
     * *
     * Este metodo retorna rota el subarbol hacia la derecha, esto para mantener
     * el balance del arbol, esto se aplica cuando el arbol esta caido hacia la
     * izquierda (balance 2)
     */
    private NodoAVL giroIzquierda(NodoAVL padre) {
        // tomamos al hijo derecho de padre
        NodoAVL hijoDerecho = padre.getDerecho();
        // tomamos el hijo izquiedo de nuetro hijo derecho
        NodoAVL temp = null;
        if (hijoDerecho.getIzquierdo() != null) {
            temp = hijoDerecho.getIzquierdo();
        }

        // hacemos que padre sea hijo derecho de su hijo derecho
        hijoDerecho.setIzquierdo(padre);
        // hacemos que el hijo derecho de padre sea el HI de su hijo derecho
        padre.setDerecho(temp);

        // recalculamos las alturas
        padre.recalcularAltura();
        hijoDerecho.recalcularAltura();
        return hijoDerecho;
    }

    /*
     * Pertenece: Devuelve verdadero si el elemento recibido por parámetro está en
     * el árbol y falso en caso contrario.
     * esVacio (): boolean
     */
    public boolean pertenece(Comparable elem) {
        boolean pertenece = false;
        if (!esVacia()) {
            pertenece = perteneceAux(elem, this.raiz);
        }
        return pertenece;
    }

    private boolean perteneceAux(Comparable elem, NodoAVL nodoActual) {
        boolean pertenece = false;
        int aux = nodoActual.getElemento().compareTo(elem);
        if (aux != 0) {
            if (aux < 0) {
                if (nodoActual.getDerecho() != null) {
                    pertenece = perteneceAux(elem, nodoActual.getDerecho());
                }
            } else {
                if (nodoActual.getIzquierdo() != null) {
                    pertenece = perteneceAux(elem, nodoActual.getIzquierdo());
                }
            }
        } else {
            pertenece = true;
        }
        return pertenece;
    }

    public Comparable getElemento(Comparable elem) {
        Comparable equipo = null;
        if (!esVacia()) {
            equipo = obtenerAux(elem, this.raiz);
        }
        return equipo;
    }

    private Comparable obtenerAux(Comparable elem, NodoAVL nodoActual) {
        Comparable equipo = null;
        int aux = nodoActual.getElemento().compareTo(elem);
        if (aux != 0) {
            if (aux < 0) {
                if (nodoActual.getDerecho() != null) {
                    equipo = obtenerAux(elem, nodoActual.getDerecho());
                }
            } else {
                if (nodoActual.getIzquierdo() != null) {
                    equipo = obtenerAux(elem, nodoActual.getIzquierdo());
                }
            }
        } else {
            equipo = nodoActual.getElemento();
        }
        return equipo;
    }
    // minimoElem (): elemento Recorre la rama correspondiente y devuelve el
    // elemento más pequeño almacenado en el árbol.

    public Comparable minimoElem() {

        Comparable resultado;
        if (this.raiz != null) {
            resultado = minimoElemAux(this.raiz);
        } else {
            resultado = "ArbolVacio";
        }
        return resultado;
    }

    private Comparable minimoElemAux(NodoAVL actual) {
        Comparable resultado;
        if (actual.getIzquierdo() == null) {
            resultado = actual.getElemento();
        } else {
            resultado = minimoElemAux(actual.getIzquierdo());
        }
        return resultado;

    }
    // minimoElem (): elemento Recorre la rama correspondiente y devuelve el
    // elemento más pequeño almacenado en el árbol.

    public Comparable maximoElem() {

        Comparable resultado;
        if (this.raiz != null) {
            resultado = maximoElemAux(this.raiz);
        } else {
            resultado = "ArbolVacio";
        }
        return resultado;
    }

    private Comparable maximoElemAux(NodoAVL actual) {
        Comparable resultado;
        if (actual.getDerecho() == null) {
            resultado = actual.getElemento();
        } else {
            resultado = maximoElemAux(actual.getDerecho());
        }
        return resultado;

    }

    // --------------------------------------------------------------------------
    public Comparable buscarPadre(Comparable elem) {
        Comparable respuesta = null;
        if (!esVacia()) {
            if (buscarPadreAux(this.raiz, elem) != null) {
                respuesta = buscarPadreAux(this.raiz, elem).getElemento();
            }

        }

        return respuesta;
    }

    private NodoAVL buscarPadreAux(NodoAVL actual, Comparable elem) {
        NodoAVL resultado = null;
        int aux = actual.getElemento().compareTo(elem);
        if (actual != null) {
            if (actual.getIzquierdo() != null && actual.getIzquierdo().getElemento().equals(elem)) {
                resultado = actual;
            } else {
                if (actual.getDerecho() != null && actual.getDerecho().getElemento().equals(elem)) {
                    resultado = actual;
                } else {
                    if (aux < 0) {
                        if (actual.getDerecho() != null) {
                            resultado = buscarPadreAux(actual.getDerecho(), elem);
                        }
                    } else {
                        if (actual.getIzquierdo() != null) {
                            resultado = buscarPadreAux(actual.getIzquierdo(), elem);
                        }
                    }
                }
            }
        }
        return resultado;
    }

    public boolean esVacia() {

        return (this.raiz == null);
    }

    public String toString() {
        String respuesta;
        if (this.raiz != null) {
            respuesta = toStringAux(this.raiz, "");
        } else {
            respuesta = "Arbol Vacio";

        }
        return respuesta;
    }

    private String toStringAux(NodoAVL nodo, String s) {
        if (nodo != null) {
            s += "\n" + nodo.getElemento().toString() + "\t";
            NodoAVL izq = nodo.getIzquierdo();
            NodoAVL der = nodo.getDerecho();
            s += "HI: " + ((izq != null) ? izq.getElemento().toString() : "-") + "\t"
                    + "HD: " + ((der != null) ? der.getElemento().toString() : "-");
            s = toStringAux(nodo.getIzquierdo(), s);
            s = toStringAux(nodo.getDerecho(), s);
        }
        return s;
    }

    public String recorridoInorden() {
        String s;
        if (!esVacia()) {
            s = recorridoInordenAux(this.raiz, "");
        } else {
            s = "Arbol vacio";
        }

        return s;
    }

    private String recorridoInordenAux(NodoAVL actual, String s) {
        if (actual != null) {
            s = recorridoInordenAux(actual.getIzquierdo(), s);
            s += "\n" + actual.getElemento().toString() + "\t";
            s = recorridoInordenAux(actual.getDerecho(), s);
        }
        return s;
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
            listarAux(unNodo.getIzquierdo(), unaLista);
            unaLista.insertar(unNodo.getElemento(), unaLista.longitud() + 1);
            listarAux(unNodo.getDerecho(), unaLista);
        }
    }
    
    public String listarRango(Comparable min, Comparable max) {
        String s = "Listado";
        if (!esVacia()) {
            s = listarRangoAux(min, max, this.raiz, s);

        }
        return s;

    }

    public String listarRangoAux(Comparable min, Comparable max, NodoAVL actual, String s) {

        if (actual != null) {
            NodoAVL izq = actual.getIzquierdo();
            NodoAVL drch = actual.getDerecho();
            if (actual.getElemento().compareTo(min) > 0 && izq != null) {
                s = listarRangoAux(min, max, izq, s);
            }
            if (actual.getElemento().compareTo(min) >= 0 && actual.getElemento().compareTo(max) <= 0) {
                s += "\n" + actual.getElemento().toString() + "\t";
            }
            if (actual.getElemento().compareTo(max) < 0 && drch != null) {
                s = listarRangoAux(min, max, drch, s);
            }
        }
        return s;
    }
}
