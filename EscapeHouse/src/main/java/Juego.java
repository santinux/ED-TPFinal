import estructuras.ArbolAVL;
import estructuras.GrafoEtiquetado;
import estructuras.Lista;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Juego
{
        private ArbolAVL habitaciones;
        private ArbolAVL desafios;
        private GrafoEtiquetado casona;
        private HashMap<String,Equipo> equipos;
        private HashMap<Desafio,Equipo> desafiosEquipos;
        private static Scanner scan;
        
        public Juego()
        {
                habitaciones = new ArbolAVL();
                desafios = new ArbolAVL();
                casona = new GrafoEtiquetado();
                equipos = new HashMap<>();
                scan = new Scanner(System.in);
        }
        
        private static void log(String unString)
        {
                try (FileWriter fw = new FileWriter("data/logs.dat", true);
                        BufferedWriter writer = new BufferedWriter(fw))
                {
                        LocalDateTime ahora = LocalDateTime.now();
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
                        String fecha = ahora.format(formato);
                        writer.write("[" + fecha + "] - " + unString);
                        writer.newLine();
                } catch (FileNotFoundException ex) {
                        System.out.println("Archivo no encontrado " + ex.getMessage());
                        ex.printStackTrace();
                } catch (IOException ex) {
                        System.out.println("Error al leer el archivo " + ex.getMessage());
                        ex.printStackTrace();
                }
        }
        
        private void cargarDatos()
        {
                try (FileReader archivo = new FileReader("data/carga_inicial.dat");
                        BufferedReader buffer = new BufferedReader(archivo))
                {
                        String linea = null;
                        while ((linea = buffer.readLine()) != null) {
                                StringTokenizer st = new StringTokenizer(linea, ";");
                                switch(st.nextToken()) {
                                case "H":
                                        // Cargar habitaciones
                                        int codigo = Integer.parseInt(st.nextToken());
                                        String nombre = st.nextToken();
                                        int planta = Integer.parseInt(st.nextToken());
                                        double metrosCuadrados = Double.parseDouble(st.nextToken());
                                        boolean tieneSalida = Boolean.parseBoolean(st.nextToken());
                                        Habitacion habitacion = new Habitacion(codigo, nombre, planta, metrosCuadrados, tieneSalida);
                                        habitaciones.insertar(habitacion);
                                        casona.insertarVertice(habitacion);
                                        log("Carga de habitación: " + habitacion.toString());
                                        break;
                                case "E":
                                        // Cargar equipos
                                        String nombreEquipo = st.nextToken();
                                        int puntajeExigido = Integer.parseInt(st.nextToken());
                                        int puntajeAcumulado = Integer.parseInt(st.nextToken());
                                        int habitacionActual = Integer.parseInt(st.nextToken());
                                        int puntajeHabitacion = Integer.parseInt(st.nextToken());
                                        Equipo equipo = new Equipo(nombreEquipo, puntajeExigido, puntajeAcumulado, habitacionActual, puntajeHabitacion);
                                        equipos.put(nombreEquipo, equipo);
                                        log("Carga de equipo: " + equipo);
                                        break;
                                case "D":
                                        // Cargar desafíos
                                        int puntaje = Integer.parseInt(st.nextToken());
                                        String nombreDesafio = st.nextToken();
                                        String tipo = st.nextToken();
                                        Desafio desafio = new Desafio(nombreDesafio, tipo, puntaje);
                                        desafios.insertar(desafio);
                                        log("Carga de desafío: " + desafio);
                                        break;
                                case "P":
                                        // Cargar puertas
                                        int puertaEntrada = Integer.parseInt(st.nextToken());
                                        int puertaSalida = Integer.parseInt(st.nextToken());
                                        int puntajeRequerido = Integer.parseInt(st.nextToken());
                                        //Lista listaHabitaciones = casona.listarVertices();
                                        Lista listaHabitaciones = habitaciones.listar();
                                        Habitacion habitacionEntrada = obtenerHabitacion(puertaEntrada, listaHabitaciones);
                                        Habitacion habitacionSalida = obtenerHabitacion(puertaSalida, listaHabitaciones);
                                        casona.insertarArco(habitacionEntrada, habitacionSalida, puntajeRequerido);
                                        casona.insertarArco(habitacionSalida, habitacionEntrada, puntajeRequerido);
                                        log("Carga de puerta: " + puertaEntrada + " -> " + puertaSalida);
                                        break;
                                }
                        }
                } catch (FileNotFoundException ex) {
                        System.out.println("Archivo no encontrado " + ex.getMessage());
                        ex.printStackTrace();
                } catch (IOException ex) {
                        System.out.println("Error al leer el archivo " + ex.getMessage());
                        ex.printStackTrace();
                }
        }
        
        private Habitacion obtenerHabitacion(int unCodigo, Lista unaLista){
                Lista listaClon = unaLista.clone();
                Habitacion encontrada = null;
                while (!listaClon.esVacia() && encontrada == null) {
                        Habitacion aux = (Habitacion) listaClon.recuperar(1);
                        if (aux.getCodigo() == unCodigo) {
                                encontrada = aux;
                        } else {
                                listaClon.eliminar(1);
                        }
                }
                return (encontrada);
        }
        
        private Desafio obtenerDesafio(String unNombre, Lista unaLista)
        {
                Lista listaClon = unaLista.clone();
                Desafio encontrado = null;
                while (!listaClon.esVacia() && encontrado == null) {
                        Desafio aux = (Desafio) listaClon.recuperar(1);
                        if (aux.getNombre().equals(unNombre)) {
                                encontrado = aux;
                        } else {
                                listaClon.eliminar(1);
                        }
                }
                return (encontrado);
        }
        
        private void mostrarHabitacion()
        {
                System.out.print("Ingrese el código: ");
                int codigo = scan.nextInt();
                if (codigo > 0 && codigo < 100) {
                        Habitacion habitacion = obtenerHabitacion(codigo, habitaciones.listar());
                        if (habitacion != null) {
                                System.out.println(habitacion);
                        } else {
                                System.out.println("Habitación no encontrada");
                        }
                } else {
                        System.out.println("Código inválido");
                }
        }
        
        private void mostrarDesafio()
        {
                System.out.print("Ingrese el nombre: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                if (nombre != null && !nombre.isEmpty()) {
                        Desafio desafio = obtenerDesafio(nombre, desafios.listar());
                        if (desafio != null) {
                                System.out.println(desafio);
                        } else {
                                System.out.println("Desafío no encontrado");
                        }
                } else {
                        System.out.println("Nombre inválido");
                }
        }
        
        private void mostrarEquipo()
        {
                System.out.print("Ingrese el nombre: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                if (nombre != null && !nombre.isEmpty()) {
                        Equipo equipo = equipos.get(nombre);
                        if (equipo != null) {
                                System.out.println(equipo);
                        } else {
                                System.out.println("Equipo no encontrado");
                        }
                } else {
                        System.out.println("Nombre inválido");
                }
        }
        
        private void mostrarMenuPrincipal()
        {
                System.out.println("------------[Menú Principal]------------");
                System.out.println("1) ABM Habitaciones");
                System.out.println("2) ABM Desafíos");
                System.out.println("3) ABM Equipos");
                System.out.println("4) Consultas sobre habitaciones");
                System.out.println("5) Consultas sobre desafíos");
                System.out.println("6) Consultas sobre equipos participantes");
                System.out.println("7) Consulta general");
                System.out.println("0) Salir");
                System.out.println("----------------------------------------");
        }
        
        private void mostrarMenuABMHabitaciones()
        {
                System.out.println("-----------[ABM Habitaciones]-----------");
                System.out.println("1) Crear habitación");
                System.out.println("2) Eliminar habitación");
                System.out.println("3) Modificar habitación");
                System.out.println("0) Volver al menú principal");
                System.out.println("----------------------------------------");
        }
        
        private void mostrarMenuABMDesafios()
        {
                System.out.println("-------------[ABM Desafíos]-------------");
                System.out.println("1) Crear desafío");
                System.out.println("2) Eliminar desafío");
                System.out.println("3) Modificar desafío");
                System.out.println("0) Volver al menú principal");
                System.out.println("----------------------------------------");
        }
        
        private void mostrarMenuABMEquipos()
        {
                System.out.println("-------------[ABM Equipos]--------------");
                System.out.println("1) Crear equipo");
                System.out.println("2) Eliminar equipo");
                System.out.println("3) Modificar equipo");
                System.out.println("0) Volver al menú principal");
                System.out.println("----------------------------------------");
        }
        
        private void mostrarMenuConsultasHabitaciones()
        {
                System.out.println("--------[Consultas Habitaciones]--------");
                System.out.println("1) Ver Info de una habitación");
                System.out.println("2) Ver habitaciones contiguas");
                System.out.println("3) Ver si es posible llegar de una habitación a otra");
                System.out.println("4) Ver mínimo puntaje requerido entre 2 habitaciones");
                System.out.println("5) Ver si se puede pasar de una habitación a otra sin pasar por una habitación con menos de un puntaje");
                System.out.println("0) Volver al menú principal");
                System.out.println("----------------------------------------");
        }
        
        private void mostrarMenuConsultasDesafios()
        {
                System.out.println("----------[Consultas Desafíos]----------");
                System.out.println("1) Ver info de un desafío");
                System.out.println("2) Ver desafíos resueltos por un equipo");
                System.out.println("3) Verificar si un equipo resolvió un desafío");
                System.out.println("4) Ver dasafíos de un tipo con puntaje dentro de un rango");
                System.out.println("0) Volver al menú principal");
                System.out.println("----------------------------------------");
        }
        
        private void mostrarMenuConsultasEquipos()
        {
                System.out.println("----------[Consultas Equipos]-----------");
                System.out.println("1) Ver info de un equipo");
                System.out.println("2) Ver posibles desafíos");
                System.out.println("3) Jugar un desafío");
                System.out.println("4) Pasar a habitación");
                System.out.println("5) Ver si se puede salir de la casona");
                System.out.println("0) Volver al menú principal");
                System.out.println("----------------------------------------");
        }
        
        private void habitacionesABM()
        {
                short opcion;
                do {
                        mostrarMenuABMHabitaciones();
                        System.out.print("Ingrese una opción: ");
                        opcion = scan.nextShort();
                        switch (opcion) {
                        case 1:
                                //crearHabitacion();
                                break;
                        case 2:
                                //eliminarHabitacion();
                                break;
                        case 3:
                                //modificarHabitacion();
                                break;
                        case 0:
                                break;
                        default:
                                System.out.println("Opción inválida");
                                break;
                        }
                } while (opcion != 0);
        }
        
        private void desafiosABM()
        {
                short opcion;
                do {
                        mostrarMenuABMDesafios();
                        System.out.print("Ingrese una opción: ");
                        opcion = scan.nextShort();
                        switch (opcion) {
                        case 1:
                                //crearDesafio();
                                break;
                        case 2:
                                //eliminarDesafio();
                                break;
                        case 3:
                                //modificarDesafio();
                                break;
                        case 0:
                                break;
                        default:
                                System.out.println("Opción inválida");
                                break;
                        }
                } while (opcion != 0);
        }
        
        private void equiposABM()
        {
                short opcion;
                do {
                        mostrarMenuABMEquipos();
                        System.out.print("Ingrese una opción: ");
                        opcion = scan.nextShort();
                        switch (opcion) {
                        case 1:
                                //crearEquipo();
                                break;
                        case 2:
                                //eliminarEquipo();
                                break;
                        case 3:
                                //modificarEquipo();
                                break;
                        case 0:
                                break;
                        default:
                                System.out.println("Opción inválida");
                                break;
                        }
                } while (opcion != 0);
        }
        
        private void consultasHabitaciones()
        {
                short opcion;
                do {
                        mostrarMenuConsultasHabitaciones();
                        System.out.print("Ingrese una opción: ");
                        opcion = scan.nextShort();
                        switch (opcion) {
                        case 1:
                                mostrarHabitacion();
                                break;
                        case 0:
                                break;
                        default:
                                System.out.println("Opción inválida");
                        }
                } while (opcion != 0);
        }
        
        private void consultasDesafios()
        {
                short opcion;
                do {
                        mostrarMenuConsultasDesafios();
                        System.out.print("Ingrese una opción: ");
                        opcion = scan.nextShort();
                        switch (opcion) {
                        case 1:
                                mostrarDesafio();
                                break;
                        case 0:
                                break;
                        default:
                                System.out.println("Opción inválida");
                        }
                } while (opcion != 0);
        }
        
        private void consultasEquipos()
        {
                short opcion;
                do {
                        mostrarMenuConsultasEquipos();
                        System.out.print("Ingrese una opción: ");
                        opcion = scan.nextShort();
                        switch (opcion) {
                        case 1:
                                mostrarEquipo();
                                break;
                        case 0:
                                break;
                        default:
                                System.out.println("Opción inválida");
                        }
                } while (opcion != 0);
        }
        
        private void mostrarSistema()
        {
                System.out.println("-------------[VISTA GENERAL]------------");
                System.out.println("Habitaciones:");
                System.out.println(habitaciones);
                System.out.println("----------------------------------------");
                System.out.println("Desafíos:");
                System.out.println(desafios);
                System.out.println("----------------------------------------");
                System.out.println("Equipos:");
                System.out.println(equipos);
                System.out.println("----------------------------------------");
        }
        
        public void iniciar()
        {
                cargarDatos();
                System.out.println("-------------[ESCAPE HOUSE]-------------");
                short opcion;
                do {
                        mostrarMenuPrincipal();
                        System.out.print("Ingrese una opción: ");
                        opcion = scan.nextShort();
                        switch (opcion) {
                        case 1:
                                habitacionesABM();
                                break;
                        case 2:
                                desafiosABM();
                                break;
                        case 3:
                                equiposABM();
                                break;
                        case 4:
                                consultasHabitaciones();
                                break;
                        case 5:
                                consultasDesafios();
                                break;
                        case 6:
                                consultasEquipos();
                                break;
                        case 7:
                                mostrarSistema();
                                break;
                        case 0:
                                System.out.println("Fin del juego.");
                                break;
                        default:
                                System.out.println("Opción inválida");
                                break;
                        }
                } while (opcion != 0);
        }
}
