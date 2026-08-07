public class Equipo
{
        private int habitacionActual;
        private int puntajeHabitacion;
        private int puntajeAcumulado;
        private int puntajeExigido;
        private String nombre;
        
        public Equipo(String unNombre, int unPuntajeExigido, int unPuntajeAcumulado,
                      int unaHabitacionActual, int unPuntajeHabitacion)
        {
                this.nombre = unNombre;
                this.puntajeExigido = unPuntajeExigido;
                this.puntajeAcumulado = unPuntajeAcumulado;
                this.habitacionActual = unaHabitacionActual;
                this.puntajeHabitacion = unPuntajeHabitacion;
        }
        
        public int getHabitacionActual()
        {
                return (this.habitacionActual);
        }
        
        public void setHabitacionActual(int habitacionActual)
        {
                this.habitacionActual = habitacionActual;
        }
        
        public int getPuntajeHabitacion()
        {
                return (this.puntajeHabitacion);
        }
        
        public void setPuntajeHabitacion(int puntajeHabitacion)
        {
                this.puntajeHabitacion = puntajeHabitacion;
        }
        
        public int getPuntajeAcumulado()
        {
                return (this.puntajeAcumulado);
        }
        
        public void setPuntajeAcumulado(int puntajeAcumulado)
        {
                this.puntajeAcumulado = puntajeAcumulado;
        }
        
        public int getPuntajeExigido()
        {
                return (this.puntajeExigido);
        }
        
        @Override
        public String toString()
        {
                StringBuilder equipoString = new StringBuilder();
                equipoString.append("Nombre Equipo: ")
                            .append(this.nombre)
                            .append(", Habitación Actual: ")
                            .append(this.habitacionActual)
                            .append(", Puntaje Habitación: ")
                            .append(this.puntajeHabitacion)
                            .append(", Puntaje Acumulado: ")
                            .append(this.puntajeAcumulado)
                            .append(", Puntaje Exigido: ")
                            .append(this.puntajeExigido);
                return (equipoString.toString());
        }
}
