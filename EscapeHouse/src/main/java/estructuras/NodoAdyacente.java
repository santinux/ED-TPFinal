package estructuras;

public class NodoAdyacente
{
        private Object etiqueta;
        private NodoVertice vertice;
        private NodoAdyacente siguienteAdyacente;
        
        public NodoAdyacente(NodoVertice unVertice, NodoAdyacente unSiguienteAdyacente, Object unaEtiqueta)
        {
                this.etiqueta = unaEtiqueta;
                this.vertice = unVertice;
                this.siguienteAdyacente = unSiguienteAdyacente;
        }
        
        public Object getEtiqueta()
        {
                return (this.etiqueta);
        }
        
        public NodoVertice getVertice()
        {
                return (this.vertice);
        }
        
        public NodoAdyacente getSiguienteAdyacente()
        {
                return (this.siguienteAdyacente);
        }
        
        public void setEtiqueta(Object unaEtiqueta)
        {
                this.etiqueta = unaEtiqueta;
        }
        
        public void setVertice(NodoVertice unVertice)
        {
                this.vertice = unVertice;
        }
        
        public void setSiguienteAdyacente(NodoAdyacente unSiguienteAdyacente)
        {
                this.siguienteAdyacente = unSiguienteAdyacente;
        }
}
