package estructuras;

public class NodoAVL {

    private Comparable elemento;
    private NodoAVL hijoDerecho;
    private NodoAVL hijoIzquierdo;
    private int altura = 0;

    // constructor
    public NodoAVL(Comparable elemento) {
        this.elemento = elemento;
    }

    // modificadores
    public void setElemento(Comparable elemento) {
        this.elemento = elemento;
    }

    // observadores
    public Comparable getElemento() {
        return this.elemento;
    }

    public NodoAVL getDerecho() {
        return this.hijoDerecho;
    }

    public NodoAVL getIzquierdo() {
        return this.hijoIzquierdo;
    }

    public void setDerecho(NodoAVL derech) {
        this.hijoDerecho = derech;
    }

    public void setIzquierdo(NodoAVL izq) {
        this.hijoIzquierdo = izq;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getAltura() {
        return altura;
    }

    public void recalcularAltura() {
        int altD = -1, altI = -1;
        if (hijoDerecho != null) {
            altD = hijoDerecho.getAltura();
        }
        if (hijoIzquierdo != null) {
            altI = hijoIzquierdo.getAltura();
        }
        altura = Math.max(altI, altD) + 1;
    }

}
