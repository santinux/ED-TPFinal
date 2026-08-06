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
        
        public Lista listarAdyacentes(NodoVertice unVertice)
        {
                Lista listaAdyacentes = new Lista();
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
