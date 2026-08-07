package estructuras;

public class GrafoEtiquetado
{
        private NodoVertice inicio;
        
        public boolean insertarVertice(Object unElemento)
        {
                boolean exito = true;
                if (this.inicio == null) {
                        this.inicio = new NodoVertice(unElemento, null, null);
                } else if (!existeVertice(unElemento)){
                        insertarVerticeAux(this.inicio, unElemento);
                }
                return (exito);
        }
        
        private void insertarVerticeAux(NodoVertice unNodo, Object unElemento)
        {
                if (unNodo != null) {
                        if (unNodo.getSiguienteVertice() == null) {
                                unNodo.setSiguienteVertice(new NodoVertice(unElemento, null, null));
                        } else {
                                insertarVerticeAux(unNodo.getSiguienteVertice(), unElemento);
                        }
                }
        }
        
        public boolean eliminarVertice(Object unElemento)
        {
                boolean exito = false;
                if (this.inicio != null) {
                        if (this.inicio.getElemento().equals(unElemento)) {
                                // El elemento está en el vértice inicial
                                this.inicio = this.inicio.getSiguienteVertice();
                                exito = true;
                        } else {
                                // Buscar el nodo "padre" del nodo contenedor del elemento
                                NodoVertice nodoPadre = obtenerNodoVerticePadre(this.inicio, unElemento);
                                if (nodoPadre != null) {
                                        // Enlazar el nodo "padre" con el nodo "hijo" del nodo contenedor del elemento
                                        nodoPadre.setSiguienteVertice(nodoPadre.getSiguienteVertice().getSiguienteVertice());
                                        exito = true;
                                }
                        }
                }
                if (exito) {
                        NodoVertice nodo = this.inicio;
                        while (nodo != null) {
                                eliminarArco(nodo.getElemento(), unElemento);
                                nodo = nodo.getSiguienteVertice();
                        }
                }
                return (exito);
        }
        
        private NodoVertice obtenerNodoVertice(NodoVertice unNodo, Object unElemento)
        {
                NodoVertice nodoEncontrado = null;
                if (unNodo != null) {
                        if (unNodo.getElemento().equals(unElemento)) {
                                nodoEncontrado = unNodo;
                        } else {
                                nodoEncontrado = obtenerNodoVertice(unNodo.getSiguienteVertice(), unElemento);
                        }
                }
                return (nodoEncontrado);
        }
        
        private NodoVertice obtenerNodoVerticePadre(NodoVertice unNodo, Object unElemento)
        {
                NodoVertice nodoEncontrado = null;
                if (unNodo != null) {
                        if (unNodo.getSiguienteVertice() != null && unNodo.getSiguienteVertice().getElemento().equals(unElemento)) {
                                nodoEncontrado = unNodo;
                        } else {
                                nodoEncontrado = obtenerNodoVerticePadre(unNodo.getSiguienteVertice(), unElemento);
                        }
                }
                return (nodoEncontrado);
        }
        
        public boolean existeVertice(Object unElemento)
        {
                return (existeVerticeAux(this.inicio, unElemento));
        }
        
        private boolean existeVerticeAux(NodoVertice unNodo, Object unElemento)
        {
                boolean existe = false;
                if (unNodo != null) {
                        if (unNodo.getElemento().equals(unElemento)) {
                                existe = true;
                        } else {
                                existe = existeVerticeAux(unNodo.getSiguienteVertice(), unElemento);
                        }
                }
                return (existe);
        }
        
        public boolean insertarArco(Object unVerticeOrigen, Object unVerticeDestino, Object unaEtiqueta)
        {
                boolean exito = false;
                // Verficar la existencia de los vértices y la no existencia de un arco igual
                if (existeVertice(unVerticeOrigen) && existeVertice(unVerticeDestino) && !existeArco(unVerticeOrigen, unVerticeDestino)) {
                        NodoVertice nodoOrigen = obtenerNodoVertice(this.inicio, unVerticeOrigen);
                        NodoVertice nodoDestino = obtenerNodoVertice(this.inicio, unVerticeDestino);
                        // Insertar el nuevo adyacente al inicio de la lista de adyacentes del nodo origen
                        NodoAdyacente nuevoAdyacente = new NodoAdyacente(nodoDestino, nodoOrigen.getPrimerAdyacente(), unaEtiqueta);
                        nodoOrigen.setPrimerAdyacente(nuevoAdyacente);
                        exito = true;
                }
                return (exito);
        }
        
        public boolean eliminarArco(Object unVerticeOrigen, Object unVerticeDestino)
        {
                boolean exito = false;
                if (existeArco(unVerticeOrigen, unVerticeDestino)) {
                        NodoVertice nodoVerticeOrigen = obtenerNodoVertice(this.inicio, unVerticeOrigen);
                        if (nodoVerticeOrigen.getPrimerAdyacente().getVertice().getElemento().equals(unVerticeDestino)) {
                                // El arco a eliminar es el primer adyacente
                                nodoVerticeOrigen.setPrimerAdyacente(nodoVerticeOrigen.getPrimerAdyacente().getSiguienteAdyacente());
                        } else {
                                // Buscar el adyacente padre del adyacente a eliminar
                                NodoAdyacente nodoAdyacentePadre = obtenerNodoAdyacentePadre(nodoVerticeOrigen.getPrimerAdyacente(), unVerticeDestino);
                                nodoAdyacentePadre.setSiguienteAdyacente(nodoAdyacentePadre.getSiguienteAdyacente().getSiguienteAdyacente());
                        }
                        exito = true;
                }
                return (exito);
        }
        
        private NodoAdyacente obtenerNodoAdyacentePadre(NodoAdyacente unAdyacente, Object unVerticeDestino)
        {
                NodoAdyacente adyacentePadre = null;
                if (unAdyacente != null) {
                        if (unAdyacente.getSiguienteAdyacente() != null &&
                                unAdyacente.getSiguienteAdyacente().getVertice().getElemento().equals(unVerticeDestino)) {
                                adyacentePadre = unAdyacente;
                        } else {
                                adyacentePadre = obtenerNodoAdyacentePadre(unAdyacente.getSiguienteAdyacente(), unVerticeDestino);
                        }
                }
                return (adyacentePadre);
        }
        
        public Object obtenerEtiqueta(Object origen, Object destino)
        {
                Object etiqueta = null;
                NodoVertice nodoOrigen = obtenerNodoVertice(this.inicio, origen);
                if (nodoOrigen != null)
                        etiqueta = obtenerEtiquetaAux(nodoOrigen.getPrimerAdyacente(), destino);
                return (etiqueta);
        }
        
        private Object obtenerEtiquetaAux(NodoAdyacente unAdyacente, Object destino) {
                Object etiqueta = null;
                if (unAdyacente != null) {
                        if (unAdyacente.getVertice().getElemento().equals(destino)) {
                                etiqueta = unAdyacente.getEtiqueta();
                        } else {
                                etiqueta = obtenerEtiquetaAux(unAdyacente.getSiguienteAdyacente(), destino);
                        }
                }
                return (etiqueta);
        }
        
        public boolean existeArco(Object unVerticeOrigen, Object unVerticeDestino)
        {
                boolean existe = false;
                NodoVertice nodoOrigen = obtenerNodoVertice(this.inicio, unVerticeOrigen);
                if (nodoOrigen != null)
                        existe = existeArcoAux(nodoOrigen.getPrimerAdyacente(), unVerticeDestino);
                return (existe);
        }
        
        private boolean existeArcoAux(NodoAdyacente unAdyacente, Object unVerticeDestino)
        {
                boolean existe = false;
                if (unAdyacente != null) {
                        if (unAdyacente.getVertice().getElemento().equals(unVerticeDestino)) {
                                existe = true;
                        } else {
                                existe = existeArcoAux(unAdyacente.getSiguienteAdyacente(), unVerticeDestino);
                        }
                }
                return (existe);
        }
        
        public boolean existeCamino(Object unVerticeOrigen, Object unVerticeDestino)
        {
                boolean existe = false;
                if (existeVertice(unVerticeOrigen) && existeVertice(unVerticeDestino)) {
                        Lista visitados = new Lista();
                        NodoVertice nodoVerticeInicio = obtenerNodoVertice(this.inicio, unVerticeOrigen);
                        existe = existeCaminoAux(nodoVerticeInicio, unVerticeDestino, visitados);
                }
                return (existe);
        }
        
        private boolean existeCaminoAux(NodoVertice unNodoVertice, Object unVerticeDestino, Lista visitados)
        {
                boolean existe = false;
                if (unNodoVertice != null && (visitados.localizar(unNodoVertice.getElemento()) < 0)) {
                        if (unNodoVertice.getElemento().equals(unVerticeDestino)) {
                                existe = true;
                        } else {
                                visitados.insertar(unNodoVertice.getElemento(), visitados.longitud() + 1); // marcar como visitado
                                NodoAdyacente adyacente = unNodoVertice.getPrimerAdyacente();
                                while (adyacente != null && !existe) {
                                        existe = existeCaminoAux(adyacente.getVertice(), unVerticeDestino, visitados);
                                        adyacente = adyacente.getSiguienteAdyacente();
                                }
                        }
                }
                return (existe);
        }
        
        public Lista minimoPuntaje(Object origen, Object destino) {
                Lista caminoMinimo = new Lista();
                if (existeVertice(origen) && existeVertice(destino)) {
                        Lista caminoActual = new Lista();
                        NodoVertice nodoOrigen = obtenerNodoVertice(this.inicio, origen);
                        int[] costoActual = { 0 };
                        int[] costoMinimo = { Integer.MAX_VALUE };
                        
                        minimoPuntajeAux(nodoOrigen, destino, caminoActual, caminoMinimo, costoActual, costoMinimo);
                        if (!caminoMinimo.esVacia()) {
                                System.out.println("Puntaje minimo acumulado: " + costoMinimo[0]);
                        } else {
                                System.out.println("No existe un camino que conecte las habitaciones");
                        }
                }
                return caminoMinimo;
        }
        
        private void minimoPuntajeAux(NodoVertice actual, Object destino, Lista caminoActual, Lista caminoMinimo,
                                      int[] costoActual, int[] costoMinimo) {
                if(actual!=null){
                        //agrego el vertice visitado al camino actual
                        caminoActual.insertar(actual.getElemento(), caminoActual.longitud() + 1);
                        if(actual.getElemento().equals(destino)){
                                //llegue donde quería, verifico si es el camino mas barato
                                if(costoActual[0] < costoMinimo [0]){
                                        costoMinimo[0] = costoActual[0];
                                        // remplazo los elementos del camino minimo con los del actual
                                        caminoMinimo.vaciar();
                                        for(int i = 1 ; i<= caminoActual.longitud();i++){
                                                caminoMinimo.insertar(caminoActual.recuperar(i), i);
                                        }
                                }
                        }else{
                                // me muevo entre los nodos adyacentes
                                NodoAdyacente ady = actual.getPrimerAdyacente();
                                while (ady!=null) {
                                        Object elemAdy = ady.getVertice().getElemento();
                                        int pesoArco = (int) ady.getEtiqueta(); // casteo a entero la etiqueta de tipo object
                                        //verifico que el nodo no este en el caminoActual
                                        if(caminoActual.localizar(elemAdy)<0){
                                                // solo sigo por esta rama si hay posibilidad de mejorar el minimo
                                                if(costoActual[0] + pesoArco < costoMinimo[0]){
                                                        // sumo el puntaje al actual
                                                        costoActual[0] += pesoArco;
                                                        //llamado recursivo con el adyacente
                                                        minimoPuntajeAux(ady.getVertice(), destino, caminoActual, caminoMinimo, costoActual, costoMinimo);
                                                        costoActual[0] -= pesoArco; // resto el puntaje para ir por otras ramas
                                                }
                                                
                                        }
                                        ady = ady.getSiguienteAdyacente();
                                }
                        }
                        //elimino el nodo actual del final de la lista
                        caminoActual.eliminar(caminoActual.longitud());
                }
        }
        
        public Lista sinPasarPor (Object origen , Object destino , Object evitar , int puntajeMax){
                Lista caminos = new Lista();
                if(existeVertice(origen) && existeVertice(destino)){
                        NodoVertice nodoOrigen = obtenerNodoVertice(this.inicio, origen);
                        // si el origen es la habitacion que quiero evitar no hago nada
                        if(evitar == null || !nodoOrigen.getElemento().equals(evitar)){
                                Lista caminoActual = new Lista();
                                sinPasarPorAux(nodoOrigen, destino , evitar , puntajeMax , 0 , caminoActual , caminos);
                        }
                }
                return caminos;
        }
        
        private void sinPasarPorAux(NodoVertice nActual , Object destino , Object evitar , int puntajeMax , int costoAct, Lista caminoActual, Lista caminos){
                if(nActual != null){
                        //agrego el vertice al camino actual
                        caminoActual.insertar(nActual.getElemento(), caminoActual.longitud()+1);
                        
                        if(nActual.getElemento().equals(destino)){
                                //guardo el clon del camino actual en la lista general
                                caminos.insertar(caminoActual.clone(), caminos.longitud() +1 );
                        }else{
                                NodoAdyacente ady = nActual.getPrimerAdyacente();
                                while (ady != null) {
                                        Object elmAdy = ady.getVertice().getElemento();
                                        int pesoArco = (int) ady.getEtiqueta();
                                        boolean esEvitado = (evitar != null && elmAdy.equals(evitar));
                                        // sigo solo si no es nodo que quiero evitar
                                        if(!esEvitado && caminoActual.localizar(elmAdy) < 0 ){
                                                // verifico no superar el puntaje maximo
                                                int nuevoCostAct = costoAct + pesoArco;
                                                if( nuevoCostAct<= puntajeMax){
                                                        sinPasarPorAux(ady.getVertice(), destino, evitar, puntajeMax, nuevoCostAct, caminoActual, caminos);
                                                }
                                        }
                                        ady= ady.getSiguienteAdyacente();
                                }
                        }
                        // elimino para buscar otros caminos
                        caminoActual.eliminar(caminoActual.longitud());
                }
        }
        
        public boolean esVacio()
        {
                return (this.inicio == null);
        }
        
        public void vaciar()
        {
                this.inicio = null;
        }
        
        public Lista listarVertices()
        {
                Lista listaVertices = new Lista();
                if (this.inicio != null)
                        listarVerticesAux(this.inicio, listaVertices);
                return (listaVertices);
        }
        
        private void listarVerticesAux(NodoVertice unVertice, Lista unaLista)
        {
                if (unVertice != null) {
                        unaLista.insertar(unVertice.getElemento(), unaLista.longitud() + 1);
                        listarVerticesAux(unVertice.getSiguienteVertice(), unaLista);
                }
        }
        
        public Lista listarAdyacentes(Object unElemento)
        {
                Lista listaAdyacentes = new Lista();
                NodoVertice unVertice = obtenerNodoVertice(this.inicio, unElemento);
                NodoAdyacente adyacente = unVertice.getPrimerAdyacente();
                if (adyacente != null)
                        listarAdyacentesAux(adyacente, listaAdyacentes);
                return (listaAdyacentes);
        }
        
        private void listarAdyacentesAux(NodoAdyacente unAdyacente, Lista unaLista)
        {
                if (unAdyacente != null) {
                        unaLista.insertar(unAdyacente.getVertice().getElemento(), unaLista.longitud() + 1);
                        listarAdyacentesAux(unAdyacente.getSiguienteAdyacente(), unaLista);
                }
        }
        
        public Lista listarEnProfundidad()
        {
                Lista visitados = new Lista();
                NodoVertice vertice = this.inicio;
                while (vertice != null) {
                        if (visitados.localizar(vertice.getElemento()) < 0)
                                listarEnProfundidadAux(vertice, visitados);
                        vertice = vertice.getSiguienteVertice();
                }
                return (visitados);
        }
        
        private void listarEnProfundidadAux(NodoVertice unNodoVertice, Lista visitados)
        {
                if (unNodoVertice != null) {
                        visitados.insertar(unNodoVertice.getElemento(), visitados.longitud() + 1);
                        NodoAdyacente adyacente = unNodoVertice.getPrimerAdyacente();
                        while (adyacente != null) {
                                if (visitados.localizar(adyacente.getVertice().getElemento()) < 0)
                                        listarEnProfundidadAux(adyacente.getVertice(), visitados);
                                adyacente = adyacente.getSiguienteAdyacente();
                        }
                }
        }
        
        public Lista listarEnAnchura()
        {
                Lista visitados = new Lista();
                NodoVertice vertice = this.inicio;
                while (vertice != null) {
                        if (visitados.localizar(vertice.getElemento()) < 0)
                                listarEnAnchuraAux(vertice, visitados);
                        vertice = vertice.getSiguienteVertice();
                }
                return (visitados);
        }
        
        private void listarEnAnchuraAux(NodoVertice unNodoVertice, Lista visitados)
        {
                Cola cola = new Cola();
                visitados.insertar(unNodoVertice.getElemento(), visitados.longitud() + 1);
                cola.poner(unNodoVertice);
                while (!cola.esVacia()) {
                        NodoVertice nodoVerticeTemp = (NodoVertice) cola.obtenerFrente();
                        NodoAdyacente adyacente = nodoVerticeTemp.getPrimerAdyacente();
                        cola.sacar();
                        while (adyacente != null) {
                                if (visitados.localizar(adyacente.getVertice().getElemento()) < 0) {
                                        visitados.insertar(adyacente.getVertice().getElemento(), visitados.longitud() + 1);
                                        cola.poner(adyacente.getVertice());
                                }
                                adyacente = adyacente.getSiguienteAdyacente();
                        }
                }
        }
        
        @Override
        public GrafoEtiquetado clone()
        {
                GrafoEtiquetado dolly = new GrafoEtiquetado();
                // Insertar todos los vértices en el clon
                NodoVertice vertice = this.inicio;
                while (vertice != null) {
                        dolly.insertarVertice(vertice.getElemento());
                        vertice = vertice.getSiguienteVertice();
                }
                // Insertar todos los arcos en el clon
                vertice = this.inicio;
                while (vertice != null) {
                        NodoAdyacente adyacente = vertice.getPrimerAdyacente();
                        while (adyacente != null) {
                                dolly.insertarArco(vertice.getElemento(),
                                                   adyacente.getVertice().getElemento(),
                                                   adyacente.getEtiqueta());
                                adyacente = adyacente.getSiguienteAdyacente();
                        }
                        vertice = vertice.getSiguienteVertice();
                }
                return (dolly);
        }
        
        @Override
        public String toString()
        {
                StringBuilder grafoString = new StringBuilder();
                NodoVertice vertice = this.inicio;
                while (vertice != null) {
                        grafoString.append(vertice.getElemento()).append(" ->");
                        NodoAdyacente adyacente = vertice.getPrimerAdyacente();
                        while (adyacente != null) {
                                grafoString.append(" ")
                                           .append(adyacente.getVertice().getElemento())
                                           .append(" (")
                                           .append(adyacente.getEtiqueta())
                                           .append(")");
                                if (adyacente.getSiguienteAdyacente() != null) {
                                        grafoString.append(",");
                                }
                                adyacente = adyacente.getSiguienteAdyacente();
                        }
                        grafoString.append("\n");
                        vertice = vertice.getSiguienteVertice();
                }
                return (grafoString.toString());
        }
}
