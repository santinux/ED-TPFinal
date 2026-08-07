public class Desafio implements Comparable
{
        private int puntaje;
        private String nombre;
        private String tipo;
        
        public Desafio(String unNombre, String unTipo, int unPuntaje)
        {
                this.puntaje = unPuntaje;
                this.nombre = unNombre;
                this.tipo = unTipo;
        }
        
        public String getNombre()
        {
                return (this.nombre);
        }
        
        public void setTipo(String unTipo)
        {
                this.tipo = unTipo;
        }
        
        public String getTipo()
        {
                return (this.tipo);
        }
        
        public int getPuntaje()
        {
                return (this.puntaje);
        }
        
        @Override
        public int compareTo(Object unDesafio)
        {
                int comparacion = 0;
                if (this.puntaje < ((Desafio) unDesafio).puntaje) {
                        comparacion = -1;
                } else if (this.puntaje > ((Desafio) unDesafio).puntaje) {
                        comparacion = 1;
                }
                return (comparacion);
        }
        
       @Override
        public String toString()
       {
               StringBuilder desafioString = new StringBuilder();
               desafioString.append("Nombre Desafio: ")
                            .append(this.nombre)
                            .append(", Puntaje: ")
                            .append(this.puntaje)
                            .append(", Tipo: ")
                            .append(this.tipo);
               return (desafioString.toString());
       }
}
