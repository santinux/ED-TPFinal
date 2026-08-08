package estructuras;

/**
 * Implementación del TDA Nodo para Árbol AVL.
 *
 * @author <a href="https://www.github.com/santinux">Santino Fuentes</a>
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class NodoAVL
{
        private Comparable elemento;
        private NodoAVL hijoIzquierdo;
        private NodoAVL hijoDerecho;
        private int altura;
        
        public NodoAVL(Comparable unElemento)
        {
                this.elemento = unElemento;
                this.hijoIzquierdo = null;
                this.hijoDerecho = null;
                this.altura = 0;
        }
        
        public NodoAVL(Comparable unElemento, NodoAVL hijoIzq, NodoAVL hijoDer)
        {
                this.elemento = unElemento;
                this.hijoIzquierdo = hijoIzq;
                this.hijoDerecho = hijoDer;
                this.altura = 0;
        }
        
        public Comparable getElemento()
        {
                return (this.elemento);
        }
        
        public NodoAVL getHijoIzquierdo()
        {
                return (this.hijoIzquierdo);
        }
        
        public NodoAVL getHijoDerecho()
        {
                return (this.hijoDerecho);
        }
        
        public int getAltura()
        {
                return (this.altura);
        }
        
        public void setElemento(Comparable unElemento)
        {
                this.elemento = unElemento;
        }
        
        public void setHijoIzquierdo(NodoAVL unNodo)
        {
                this.hijoIzquierdo = unNodo;
        }
        
        public void setHijoDerecho(NodoAVL unNodo)
        {
                this.hijoDerecho = unNodo;
        }
        
        public void recalcularAltura()
        {
                int alturaHI = -1;
                int alturaHD = -1;
                if (this.hijoIzquierdo != null)
                        alturaHI = this.hijoIzquierdo.getAltura();
                if (this.hijoDerecho != null)
                        alturaHD = this.hijoDerecho.getAltura();
                this.altura = Math.max(alturaHI, alturaHD) + 1;
        }
}
