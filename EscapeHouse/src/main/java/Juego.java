import estructuras.ArbolAVL;
import estructuras.GrafoEtiquetado;
import estructuras.Lista;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Implementación del juego Escape House, correspondiente al trabajo final de
 * la materia:
 * Estructuras de Datos, Facultad de Informatica,
 * Universidad Nacional del Comahue,
 * 2026.
 *
 * @author <a href="https://www.github.com/santinux">Santino Fuentes</a>
 * @version 1.0
 */
public class Juego
{
        private ArbolAVL habitaciones;
        private ArbolAVL desafios;
        private GrafoEtiquetado casona;
        private HashMap<String, Equipo> equipos;
        private HashMap<String, Lista> desafiosResueltos;
        private static Scanner scan;
        
        public Juego()
        {
                habitaciones = new ArbolAVL();
                desafios = new ArbolAVL();
                casona = new GrafoEtiquetado();
                equipos = new HashMap<>();
                desafiosResueltos = new HashMap<>();
                scan = new Scanner(System.in);
        }
        
        private static void log(String unString)
        {
                try (FileWriter fw = new FileWriter("data/logs.dat", true);
                     BufferedWriter writer = new BufferedWriter(fw)) {
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
                     BufferedReader buffer = new BufferedReader(archivo)) {
                        String linea = null;
                        while ((linea = buffer.readLine()) != null) {
                                StringTokenizer st = new StringTokenizer(linea, ";");
                                switch (st.nextToken()) {
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
                                                desafiosResueltos.put(nombreEquipo, new Lista());
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
                                                Habitacion habitacionEntrada = obtenerHabitacion(puertaEntrada);
                                                Habitacion habitacionSalida = obtenerHabitacion(puertaSalida);
                                                casona.insertarArco(habitacionEntrada, habitacionSalida, puntajeRequerido);
                                                casona.insertarArco(habitacionSalida, habitacionEntrada, puntajeRequerido);
                                                log("Carga de puerta (arco): " + puertaEntrada + " -> " + puertaSalida);
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
        
        /**
         * Busca y retorna la habitación corespondiente al código dado.
         * Se utiliza la búsqueda eficiente de Árbol AVL buscando con la clave
         * de comparación que es el código de habitación.
         *
         * @param unCodigo
         * @return Una habitación o null si no se encuentra (el null lo retorna
         * el metodo de Árbol AVL.
         */
        private Habitacion obtenerHabitacion(int unCodigo)
        {
                Habitacion habitacion = new Habitacion(unCodigo, "", 0, 0, false);
                habitacion = (Habitacion) habitaciones.obtenerElemento(habitacion);
                return (habitacion);
        }
        
        /**
         * Busca y retorna el desafío correspondiente al nombre dado.
         * La búsqueda de esta manera y no como con habitaciones, se justifica
         * porque la clave de comparación de desafío es su puntaje y no su nombre.
         *
         * @param unNombre
         * @param unaLista
         * @return Un desafío o null si no se encuentra.
         */
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
                Habitacion habitacion = obtenerHabitacion(codigo);
                if (habitacion != null) {
                        System.out.println(habitacion);
                } else {
                        System.out.println("Habitación no encontrada");
                }
        }
        
        private void habitacionesContiguas()
        {
                System.out.print("Ingrese código de habitación: ");
                int codigo = scan.nextInt();
                Habitacion habitacion = obtenerHabitacion(codigo);
                if (habitacion != null) {
                        Lista habitacionesAdyacentes = casona.listarAdyacentes(habitacion);
                        System.out.println("Habitaciones contiguas: ");
                        if (!habitacionesAdyacentes.esVacia()) {
                                while (!habitacionesAdyacentes.esVacia()) {
                                        Habitacion habitacionAdyacente = (Habitacion) habitacionesAdyacentes.recuperar(1);
                                        System.out.println("Código: " + habitacionAdyacente.getCodigo()
                                                                   + " Nombre: " + habitacionAdyacente.getNombre()
                                                                   + " Puntaje requerido: " + casona.obtenerEtiqueta(habitacion, habitacionAdyacente));
                                        habitacionesAdyacentes.eliminar(1);
                                }
                        }
                } else {
                        System.out.println("Habitación no encontrada");
                }
        }
        
        private void esPosibleLlegar()
        {
                System.out.print("Ingrese el código de la habitación de origen: ");
                int codOrigen = scan.nextInt();
                System.out.print("Ingrese el código de la habitación de destino: ");
                int codDestino = scan.nextInt();
                System.out.print("Ingrese el puntaje k a acumular: ");
                int k = scan.nextInt();
                
                Habitacion habOrigen = obtenerHabitacion(codOrigen);
                Habitacion habDestino = obtenerHabitacion(codDestino);
                
                if (habOrigen != null && habDestino != null) {
                        if (casona.existeCaminoCosto(habOrigen, habDestino, k)) {
                                System.out.println("Es posible llegar");
                        } else {
                                System.out.println("No es posible llegar");
                        }
                } else {
                        System.out.println("Alguna de las habitaciones ingresadas no existen");
                }
        }
        
        private void minimoPuntaje()
        {
                System.out.print("Ingrese el código de la habitación de origen: ");
                int codOrigen = scan.nextInt();
                System.out.print("Ingrese el código de la habitación de destino: ");
                int codDestino = scan.nextInt();
                
                Habitacion habOrigen = obtenerHabitacion(codOrigen);
                Habitacion habDestino = obtenerHabitacion(codDestino);
                if (habOrigen != null && habDestino != null) {
                        Lista camino = casona.minimoPuntaje(habOrigen, habDestino);
                        if (!camino.esVacia()) {
                                System.out.println("Camino de habitaciones: ");
                                for (int i = 1; i <= camino.longitud(); i++) {
                                        Habitacion h = (Habitacion) camino.recuperar(i);
                                        System.out.print("[ " + h.getCodigo() + " - " + h.getNombre() + " ]");
                                        if (i < camino.longitud()) {
                                                System.out.print("->");
                                        }
                                }
                                System.out.println();
                        }
                } else {
                        System.out.println("Alguno de los códigos ingresados no exite.");
                }
        }
        
        private void sinPasarPor()
        {
                System.out.print("Ingrese el código de la habitación de origen: ");
                int codOri = scan.nextInt();
                System.out.print("Ingrese el código de la habitación de destino: ");
                int codDest = scan.nextInt();
                System.out.print("Ingrese el código de la habitación a evitar: ");
                int codEvitar = scan.nextInt();
                System.out.print("Ingrese el puntaje máximo permitido: ");
                int puntajeMax = scan.nextInt();
                
                Habitacion habOrigen = obtenerHabitacion(codOri);
                Habitacion habDestino = obtenerHabitacion(codDest);
                Habitacion habEvitar = obtenerHabitacion(codEvitar);
                
                if (habOrigen != null && habDestino != null) {
                        Lista caminosValidos = casona.sinPasarPor(habOrigen, habDestino, habEvitar, puntajeMax);
                        if (caminosValidos.esVacia()) {
                                System.out.println("No se encontraron caminos que cumplan las condiciones");
                        } else {
                                System.out.println("Caminos posibles: ");
                                // recorro la lista general de caminos
                                for (int i = 1; i <= caminosValidos.longitud(); i++) {
                                        Lista caminito = (Lista) caminosValidos.recuperar(i);
                                        System.out.print("Opción " + i + " : ");
                                        // recorro las habitaciones de cada camino
                                        for (int j = 1; j <= caminito.longitud(); j++) {
                                                Habitacion h = (Habitacion) caminito.recuperar(j);
                                                System.out.print("[ " + h.getCodigo() + " - " + h.getNombre() + " ]");
                                                if (j < caminito.longitud()) {
                                                        System.out.print("->");
                                                }
                                        }
                                        System.out.println();
                                }
                        }
                } else {
                        System.out.println("Alguno de los códigos ingresados no existen");
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
        
        private void mostrarDesafiosResueltos()
        {
                System.out.print("Ingrese nombre de equipo: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                Equipo equipo = equipos.get(nombre);
                if (equipo != null) {
                        Lista lista = desafiosResueltos.get(nombre);
                        if (lista != null && !lista.esVacia()) {
                                System.out.println("Desafíos resueltos por el equipo " + nombre + ":");
                                Lista listaClon = lista.clone();
                                while (!listaClon.esVacia()) {
                                        Desafio desafio = (Desafio) listaClon.recuperar(1);
                                        System.out.println(desafio);
                                        listaClon.eliminar(1);
                                }
                        } else {
                                System.out.println("El equipo " + nombre + " no ha resuelto desafíos");
                        }
                } else {
                        System.out.println("Equipo no encontrado");
                }
        }
        
        private void verificarDesafioResuelto()
        {
                System.out.print("Ingrese nombre de equipo: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                Equipo equipo = equipos.get(nombre);
                if (equipo != null) {
                        System.out.print("Ingrese nombre de desafío: ");
                        String nombreDesafio = scan.nextLine().trim();
                        Desafio desafio = obtenerDesafio(nombreDesafio, desafios.listar());
                        if (desafio != null) {
                                Lista listaDesafios = desafiosResueltos.get(nombre);
                                if (listaDesafios != null && !listaDesafios.esVacia() && listaDesafios.localizar(desafio) != -1) {
                                        System.out.println("Desafío resuelto");
                                } else {
                                        System.out.println("Desafío no resuelto");
                                }
                        } else {
                                System.out.println("Desafío no encontrado");
                        }
                } else {
                        System.out.println("Equipo no encontrado");
                }
        }
        
        private void mostrarDesafiosTipo()
        {
                System.out.print("Ingrese tipo: ");
                scan.nextLine();
                String tipo = scan.nextLine().trim();
                System.out.print("Ingrese puntaje mínimo: ");
                int puntajeMinimo = scan.nextInt();
                System.out.print("Ingrese puntaje máximo: ");
                int puntajeMaximo = scan.nextInt();
                Desafio desafioMinimo = new Desafio("", "", puntajeMinimo);
                Desafio desafioMaximo = new Desafio("", "", puntajeMaximo);
                Lista lista = desafios.listarRango(desafioMinimo, desafioMaximo);
                if (!lista.esVacia()) {
                        while (!lista.esVacia()) {
                                Desafio desafio = (Desafio) lista.recuperar(1);
                                if (desafio.getTipo().equals(tipo))
                                        System.out.println(desafio);
                                lista.eliminar(1);
                        }
                } else {
                        System.out.println("No se encontraron desafíos");
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
        
        private void posiblesDesafios()
        {
                System.out.print("Ingrese nombre de equipo: ");
                scan.nextLine();
                String nombreEquipo = scan.nextLine().trim();
                System.out.print("Ingrese código de habitación: ");
                int codigoHabitacion = scan.nextInt();
                Equipo equipo = equipos.get(nombreEquipo);
                Habitacion habitacion = obtenerHabitacion(codigoHabitacion);
                if (equipo != null && habitacion != null) {
                        int codHabitacionActual = equipo.getHabitacionActual();
                        Habitacion habitacionActual = obtenerHabitacion(codHabitacionActual);
                        Lista habitacionesAdyacentes = casona.listarAdyacentes(habitacionActual);
                        int posicion = habitacionesAdyacentes.localizar(habitacion);
                        if (posicion != -1) {
                                Habitacion habitacionAdyacente = (Habitacion) habitacionesAdyacentes.recuperar(posicion);
                                int puntajeRequerido = (int) casona.obtenerEtiqueta(habitacionActual, habitacionAdyacente);
                                Lista desafiosTotal = desafios.listar();
                                Lista desafiosResueltosEquipo = desafiosResueltos.get(nombreEquipo);
                                Lista desafiosPendientes = desafiosPendientes(desafiosTotal, desafiosResueltosEquipo);
                                if (!desafiosPendientes.esVacia()) {
                                        System.out.println("Posibles desafíos:");
                                        while (!desafiosPendientes.esVacia()) {
                                                Desafio desafio = (Desafio) desafiosPendientes.recuperar(1);
                                                if ((desafio.getPuntaje() + equipo.getPuntajeAcumulado()) > puntajeRequerido)
                                                        System.out.println(desafio);
                                                desafiosPendientes.eliminar(1);
                                        }
                                }
                        } else {
                                System.out.println("Habitación no adyacente");
                        }
                } else {
                        System.out.println("Equipo o habitación no encontrado");
                }
        }
        
        public Lista desafiosPendientes(Lista desafiosTotal, Lista desafiosResueltos)
        {
                Lista desafiosTot = desafiosTotal.clone();
                Lista pendientes = new Lista();
                while (!desafiosTot.esVacia()) {
                        Object desafio = desafiosTot.recuperar(1);
                        if (desafiosResueltos.localizar(desafio) == -1) {
                                pendientes.insertar(desafio, pendientes.longitud() + 1);
                        }
                        desafiosTot.eliminar(1);
                }
                return pendientes;
        }
        
        private void jugarDesafio()
        {
                System.out.print("Ingrese nombre de equipo: ");
                scan.nextLine();
                String nombreEquipo = scan.nextLine().trim();
                System.out.print("Ingrese nombre de desafío: ");
                String nombreDesafio = scan.nextLine().trim();
                Equipo equipo = equipos.get(nombreEquipo);
                Desafio desafio = obtenerDesafio(nombreDesafio, desafios.listar());
                if (equipo != null && desafio != null) {
                        Lista listaDesafios = desafiosResueltos.get(nombreEquipo);
                        if (listaDesafios != null) {
                                if (listaDesafios.localizar(desafio) == -1) {
                                        int puntajeDesafio = desafio.getPuntaje();
                                        equipo.setPuntajeHabitacion(equipo.getPuntajeHabitacion() + puntajeDesafio);
                                        equipo.setPuntajeAcumulado(equipo.getPuntajeAcumulado() + puntajeDesafio);
                                        registrarDesafioResuelto(nombreEquipo, desafio);
                                        String msj = "Equipo " + nombreEquipo + " jugó " + nombreDesafio + " y obtuvo " + puntajeDesafio + " puntos";
                                        System.out.println(msj);
                                        log(msj);
                                } else {
                                        System.out.println("Desafío ya resuelto por el equipo");
                                }
                        } else {
                                System.out.println("El equipo no resolvió desafíos aún");
                        }
                } else {
                        System.out.println("Equipo o desafío no encontrado");
                }
        }
        
        private void registrarDesafioResuelto(String unNombreEquipo, Desafio unDesafio)
        {
                Lista lista = desafiosResueltos.get(unNombreEquipo);
                if (lista == null) {
                        lista = new Lista();
                        desafiosResueltos.put(unNombreEquipo, lista);
                }
                lista.insertar(unDesafio, lista.longitud() + 1);
        }
        
        private void pasarAHabitacion()
        {
                System.out.print("Ingrese nombre del equipo: ");
                scan.nextLine();
                String nombreEquipo = scan.nextLine().trim();
                System.out.print("Ingrese código de habitación: ");
                int codigoHabitacion = scan.nextInt();
                Equipo equipo = equipos.get(nombreEquipo);
                Habitacion habitacion = obtenerHabitacion(codigoHabitacion);
                if (equipo != null && habitacion != null) {
                        int codHabitacionActual = equipo.getHabitacionActual();
                        Habitacion habitacionActual = obtenerHabitacion(codHabitacionActual);
                        Lista habitacionesAdyacentes = casona.listarAdyacentes(habitacionActual);
                        int posicion = habitacionesAdyacentes.localizar(habitacion);
                        if (posicion != -1) {
                                Habitacion habitacionAdyacente = (Habitacion) habitacionesAdyacentes.recuperar(posicion);
                                int puntajeRequerido = (int) casona.obtenerEtiqueta(habitacionActual, habitacionAdyacente);
                                if (equipo.getPuntajeAcumulado() >= puntajeRequerido) {
                                        equipo.setHabitacionActual(habitacionAdyacente.getCodigo());
                                        equipo.setPuntajeHabitacion(0);
                                        String msj = "Equipo " + equipo + " pasó a la habitación " + habitacionAdyacente.getCodigo();
                                        System.out.println(msj);
                                        log(msj);
                                } else {
                                        System.out.println("Habitación adyacente, pero puntaje insuficiente");
                                }
                        } else {
                                System.out.println("La habitación no es adyacente");
                        }
                } else {
                        System.out.println("Equipo o habitación no encontrado");
                }
        }
        
        private void puedeSalir()
        {
                System.out.print("Ingrese nombre del equipo: ");
                scan.nextLine();
                String nombreEquipo = scan.nextLine().trim();
                Equipo equipo = equipos.get(nombreEquipo);
                if (equipo != null) {
                        Habitacion habitacionActual = obtenerHabitacion(equipo.getHabitacionActual());
                        if (habitacionActual.tieneSalida()) {
                                if (equipo.getPuntajeAcumulado() >= equipo.getPuntajeExigido()) {
                                        System.out.println("¡Felicitaciones! El equipo " + nombreEquipo + " puede salir!");
                                } else {
                                        System.out.println("Puntaje insuficiente para salir :(");
                                }
                        } else {
                                System.out.println("La habitación actual no tiene salida");
                        }
                } else {
                        System.out.println("Equipo no encontrado");
                }
        }
        
        private void crearHabitacion()
        {
                int codigo = ((Habitacion) habitaciones.maximoElemento()).getCodigo() + 1;
                System.out.print("Ingrese nombre: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                System.out.print("Ingrese planta: ");
                int planta = scan.nextInt();
                System.out.print("Ingrese metros cuadrados: ");
                double metrosCuadrados = scan.nextDouble();
                Habitacion habitacion = new Habitacion(codigo, nombre, planta, metrosCuadrados, false);
                habitaciones.insertar(habitacion);
                casona.insertarVertice(habitacion);
                String msj = "Se crea habitación: " + habitacion;
                System.out.println(msj);
                log(msj);
                System.out.println("Si desea conectarla a otras habitaciones, utilice la opción para modificar habitación");
        }
        
        private void eliminarHabitacion()
        {
                System.out.print("Ingrese código: ");
                int codigo = scan.nextInt();
                Habitacion habitacion = obtenerHabitacion(codigo);
                if (habitacion != null && !habitacion.tieneSalida()) {
                        habitaciones.eliminar(habitacion);
                        casona.eliminarVertice(habitacion);
                        String msj = "Se elimina habitación: " + habitacion;
                        System.out.println(msj);
                        log(msj);
                } else {
                        System.out.println("Habitación no encontrada o tiene salida (no se puede eliminar)");
                }
        }
        
        private void modificarHabitacion()
        {
                System.out.print("Ingrese código: ");
                int codigo = scan.nextInt();
                Habitacion habitacion = obtenerHabitacion(codigo);
                if (habitacion != null && !habitacion.tieneSalida()) {
                        short opcion;
                        do {
                                mostrarMenuModificarHabitacion();
                                String msj;
                                System.out.print("Ingrese una opción: ");
                                opcion = scan.nextShort();
                                switch (opcion) {
                                        case 1:
                                                System.out.print("Ingrese nuevo nombre: ");
                                                scan.nextLine();
                                                String nuevoNombre = scan.nextLine().trim();
                                                habitacion.setNombre(nuevoNombre);
                                                msj = "Se modifica el nombre de la habitación " + codigo;
                                                System.out.println(msj);
                                                log(msj);
                                                break;
                                        case 2:
                                                System.out.print("Ingrese nueva planta: ");
                                                int nuevaPlanta = scan.nextInt();
                                                if (nuevaPlanta > -2 && nuevaPlanta < 3) {
                                                        habitacion.setPlanta(nuevaPlanta);
                                                        msj = "Se modifica la planta de la habitación " + codigo;
                                                        System.out.println(msj);
                                                        log(msj);
                                                } else {
                                                        System.out.println("Planta inexistente");
                                                }
                                                break;
                                        case 3:
                                                System.out.print("Ingrese nuevo área en metros cuadrados: ");
                                                double nuevosMetrosCuadrados = scan.nextDouble();
                                                if (nuevosMetrosCuadrados > 0) {
                                                        habitacion.setMetrosCuadrados(nuevosMetrosCuadrados);
                                                        msj = "Se modifica el área de la habitación " + codigo;
                                                        System.out.println(msj);
                                                        log(msj);
                                                } else {
                                                        System.out.println("Ingrese un valor mayor a 0");
                                                }
                                                break;
                                        case 4:
                                                System.out.print("Ingrese código de habitación contigua: ");
                                                int codigoHabitacionContigua = scan.nextInt();
                                                if (codigoHabitacionContigua != codigo) {
                                                        Habitacion habitacionContigua = obtenerHabitacion(codigoHabitacionContigua);
                                                        if (habitacionContigua != null && !habitacionContigua.tieneSalida()) {
                                                                System.out.print("Ingrese puntaje requerido: ");
                                                                int puntajeRequerido = scan.nextInt();
                                                                boolean exito1 = casona.insertarArco(habitacion, habitacionContigua, puntajeRequerido);
                                                                boolean exito2 = casona.insertarArco(habitacionContigua, habitacion, puntajeRequerido);
                                                                if (exito1 && exito2) {
                                                                        msj = "Se crea puerta entre habitaciones " + codigo + " y " + codigoHabitacionContigua + " con puntaje " + puntajeRequerido;
                                                                        System.out.println(msj);
                                                                        log(msj);
                                                                } else {
                                                                        System.out.println("No se pudo crear la puerta");
                                                                }
                                                        } else {
                                                                System.out.println("Habitación no encontrada o tiene salida (no se puede eliminar)");
                                                        }
                                                } else {
                                                        System.out.println("En esta casa respetamos las leyes de la termodinámica");
                                                        System.out.println("No se puede conectar una habitación con si misma >:(");
                                                }
                                                break;
                                        case 5:
                                                System.out.print("Ingrese código de habitación contigua: ");
                                                int codigoHabitContigua = scan.nextInt();
                                                Habitacion habitContigua = obtenerHabitacion(codigoHabitContigua);
                                                if (habitContigua != null && !habitContigua.tieneSalida()) {
                                                        boolean exito1 = casona.eliminarArco(habitacion, habitContigua);
                                                        boolean exito2 = casona.eliminarArco(habitContigua, habitacion);
                                                        if (exito1 && exito2) {
                                                                msj = "Se elimina puerta entre habitaciones " + codigo + " y " + codigoHabitContigua;
                                                                System.out.println(msj);
                                                                log(msj);
                                                        } else {
                                                                System.out.println("No se pudo eliminar la puerta");
                                                        }
                                                } else {
                                                        System.out.println("Habitación no encontrada o tiene salida (no se puede eliminar)");
                                                }
                                                break;
                                        case 0:
                                                break;
                                        default:
                                                System.out.println("Opción inválida");
                                                break;
                                }
                        } while (opcion != 0);
                } else {
                        System.out.println("Habitación no encontrada o tiene salida (no se puede eliminar)");
                }
        }
        
        private void crearDesafio()
        {
                System.out.print("Ingrese nombre: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                System.out.print("Ingrese tipo: ");
                String tipo = scan.nextLine().trim();
                System.out.print("Ingrese puntaje: ");
                int puntaje = scan.nextInt();
                Desafio desafio = new Desafio(nombre, tipo, puntaje);
                if (!desafios.pertenece(desafio)) {
                        desafios.insertar(desafio);
                        log("Se crea el desafío: " + desafio);
                } else {
                        System.out.println("Ya existe un desafío con ese puntaje");
                }
        }
        
        private void eliminarDesafio()
        {
                System.out.print("Ingrese nombre: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                Desafio desafio = obtenerDesafio(nombre, desafios.listar());
                if (desafio != null) {
                        desafios.eliminar(desafio);
                        log("Se elimina el desafío: " + desafio);
                } else {
                        System.out.println("Nombre inválido");
                }
        }
        
        private void modificarDesafio()
        {
                System.out.print("Ingrese nombre: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                Desafio desafio = obtenerDesafio(nombre, desafios.listar());
                if (desafio != null) {
                        short opcion;
                        do {
                                mostrarMenuModificarDesafio();
                                System.out.print("Ingrese una opción: ");
                                opcion = scan.nextShort();
                                switch (opcion) {
                                        case 1:
                                                System.out.print("Ingrese tipo: ");
                                                scan.nextLine();
                                                String tipo = scan.nextLine().trim();
                                                desafio.setTipo(tipo);
                                                log("Se modifica el desafío: " + nombre);
                                                break;
                                        case 0:
                                                break;
                                        default:
                                                System.out.println("Opción inválida");
                                }
                        } while (opcion != 0);
                } else {
                        System.out.println("Nombre inválido");
                }
        }
        
        private void crearEquipo()
        {
                System.out.print("Ingrese nombre: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                if (!equipos.containsKey(nombre)) {
                        Equipo equipo = new Equipo(nombre, 400, 0, 0, 0);
                        equipos.put(nombre, equipo);
                        log("Se crea el equipo: " + equipo);
                } else {
                        System.out.println("Nombre de equipo ya existente");
                }
        }
        
        private void eliminarEquipo()
        {
                System.out.print("Ingrese nombre: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                Equipo equipo = equipos.get(nombre);
                if (equipo != null) {
                        equipos.remove(nombre);
                        log("Se elimina el equipo: " + nombre);
                } else {
                        System.out.println("Nombre inválido");
                }
        }
        
        private void modificarEquipo()
        {
                System.out.print("Ingrese nombre: ");
                scan.nextLine();
                String nombre = scan.nextLine().trim();
                Equipo equipo = equipos.get(nombre);
                if (equipo != null) {
                        short opcion;
                        do {
                                mostrarMenuModificarEquipo();
                                System.out.print("Ingrese una opción: ");
                                opcion = scan.nextShort();
                                switch (opcion) {
                                        case 1:
                                                System.out.print("Ingrese nuevo puntaje acumulado: ");
                                                int nuevoPuntajeAcumulado = scan.nextInt();
                                                if (nuevoPuntajeAcumulado >= 0) {
                                                        equipo.setPuntajeAcumulado(nuevoPuntajeAcumulado);
                                                        log("Se modifica el puntaje acumulado del equipo: " + nombre);
                                                }
                                                break;
                                        case 2:
                                                System.out.print("Ingrese nueva habitación actual: ");
                                                int nuevaHabitacionActual = scan.nextInt();
                                                if (nuevaHabitacionActual >= 0 && nuevaHabitacionActual < 100) {
                                                        equipo.setHabitacionActual(nuevaHabitacionActual);
                                                        log("Se modifica la habitación actual del equipo: " + nombre);
                                                } else {
                                                        System.out.println("Habitación inválida");
                                                }
                                                break;
                                        case 3:
                                                System.out.print("Ingrese nuevo puntaje habitación actual: ");
                                                int nuevoPuntajeHabitacionActual = scan.nextInt();
                                                if (nuevoPuntajeHabitacionActual >= 0) {
                                                        equipo.setPuntajeHabitacion(nuevoPuntajeHabitacionActual);
                                                        log("Se modifica el puntaje de habitación actual del equipo: " + nombre);
                                                } else {
                                                        System.out.println("Puntaje inválido");
                                                }
                                                break;
                                        case 0:
                                                break;
                                        default:
                                                System.out.println("Opción inválida");
                                                break;
                                }
                        } while (opcion != 0);
                } else {
                        System.out.println("Equipo no encontrado");
                }
        }
        
        private void mostrarMenuPrincipal()
        {
                System.out.println("------------[Menú Principal]------------");
                System.out.println("1) Cargar datos de prueba");
                System.out.println("2) ABM Habitaciones");
                System.out.println("3) ABM Desafíos");
                System.out.println("4) ABM Equipos");
                System.out.println("5) Consultas sobre habitaciones");
                System.out.println("6) Consultas sobre desafíos");
                System.out.println("7) Consultas sobre equipos participantes");
                System.out.println("8) Consulta general");
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
        
        private void mostrarMenuModificarHabitacion()
        {
                System.out.println("---------[Modificar Habitación]---------");
                System.out.println("1) Modificar nombre");
                System.out.println("2) Modificar planta");
                System.out.println("3) Modificar metros cuadrados");
                System.out.println("4) Agregar habitación contigua");
                System.out.println("5) Quitar habitación contigua");
                System.out.println("0) Volver");
                System.out.println("----------------------------------------");
        }
        
        private void mostrarMenuModificarDesafio()
        {
                System.out.println("----------[Modificar Desafío]----------");
                System.out.println("1) Modificar tipo");
                System.out.println("0) Volver");
                System.out.println("----------------------------------------");
        }
        
        private void mostrarMenuModificarEquipo()
        {
                System.out.println("-----------[Modificar Equipo]-----------");
                System.out.println("1) Modificar puntaje acumulado");
                System.out.println("2) Modificar habitación actual");
                System.out.println("3) Modificar puntaje habitación actual");
                System.out.println("0) Volver");
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
                System.out.println("4) Ver desafíos de un tipo con puntaje dentro de un rango");
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
                                        crearHabitacion();
                                        break;
                                case 2:
                                        eliminarHabitacion();
                                        break;
                                case 3:
                                        modificarHabitacion();
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
                                        crearDesafio();
                                        break;
                                case 2:
                                        eliminarDesafio();
                                        break;
                                case 3:
                                        modificarDesafio();
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
                                        crearEquipo();
                                        break;
                                case 2:
                                        eliminarEquipo();
                                        break;
                                case 3:
                                        modificarEquipo();
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
                                case 2:
                                        habitacionesContiguas();
                                        break;
                                case 3:
                                        esPosibleLlegar();
                                        break;
                                case 4:
                                        minimoPuntaje();
                                        break;
                                case 5:
                                        sinPasarPor();
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
                                case 2:
                                        mostrarDesafiosResueltos();
                                        break;
                                case 3:
                                        verificarDesafioResuelto();
                                        break;
                                case 4:
                                        mostrarDesafiosTipo();
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
                                case 2:
                                        posiblesDesafios();
                                        break;
                                case 3:
                                        jugarDesafio();
                                        break;
                                case 4:
                                        pasarAHabitacion();
                                        break;
                                case 5:
                                        puedeSalir();
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
                boolean datosCargados = false;
                System.out.println("-------------[ESCAPE HOUSE]-------------");
                short opcion;
                do {
                        mostrarMenuPrincipal();
                        System.out.print("Ingrese una opción: ");
                        opcion = scan.nextShort();
                        switch (opcion) {
                                case 1:
                                        if (datosCargados) {
                                                System.out.println("Ya se han cargado los datos de prueba");
                                        } else {
                                                cargarDatos();
                                                datosCargados = true;
                                        }
                                        break;
                                case 2:
                                        habitacionesABM();
                                        break;
                                case 3:
                                        desafiosABM();
                                        break;
                                case 4:
                                        equiposABM();
                                        break;
                                case 5:
                                        consultasHabitaciones();
                                        break;
                                case 6:
                                        consultasDesafios();
                                        break;
                                case 7:
                                        consultasEquipos();
                                        break;
                                case 8:
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
