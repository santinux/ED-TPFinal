public class Habitacion implements Comparable
{
        private boolean salidaExterior;
        private int codigo;
        private int planta;
        private double metrosCuadrados;
        private String nombre;
        
        public Habitacion(int unCodigo, String unNombre, int unaPlanta,
                          double metrosCuadrados, boolean salidaExterior)
        {
                this.codigo = unCodigo;
                this.nombre = unNombre;
                this.planta = unaPlanta;
                this.metrosCuadrados = metrosCuadrados;
                this.salidaExterior = salidaExterior;
        }
        
        public int getCodigo()
        {
                return (this.codigo);
        }
        
        public void setNombre(String unNombre)
        {
                this.nombre = unNombre;
        }
        
        public void setPlanta(int unaPlanta)
        {
                this.planta = unaPlanta;
        }
        
        public void setMetrosCuadrados(double metrosCuadrados)
        {
                this.metrosCuadrados = metrosCuadrados;
        }
        
        public boolean tieneSalida()
        {
                return (this.salidaExterior);
        }
        
        @Override
        public int compareTo(Object unaHabitacion)
        {
                int comparacion = 0;
                if (this.codigo < ((Habitacion)unaHabitacion).codigo) {
                        comparacion = -1;
                } else if (this.codigo > ((Habitacion)unaHabitacion).codigo) {
                        comparacion = 1;
                }
                return (comparacion);
        }
        
        @Override
        public String toString()
        {
                StringBuilder habitacionString = new StringBuilder();
                habitacionString.append("Código: ")
                                .append(this.codigo)
                                .append(", Nombre: ")
                                .append(this.nombre)
                                .append(", Planta: ")
                                .append(this.planta)
                                .append(", Metros Cuadrados: ")
                                .append(this.metrosCuadrados)
                                .append(", Salida Exterior: ")
                                .append(this.salidaExterior);
                return (habitacionString.toString());
        }
}
