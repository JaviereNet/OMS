package es.educastur.oms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Utilidades {
	
    public static Date leerFecha() {
    	Scanner sc = new Scanner(System.in, "ISO-8859-1");
        Date ret = null;
        boolean correcto = false;

        while (!correcto) {
            try {
                System.out.println("Introduzca un valor para el día (1...31):");
                int dia = sc.nextInt();
                
                System.out.println("Introduzca un valor para el mes (1...12):");
                int mes = sc.nextInt();
                
                System.out.println("Introduzca un valor para el año:");
                int anio = sc.nextInt();
                
                ret = Date.valueOf(LocalDate.of(anio, mes, dia));
                correcto = true;
            } catch (Exception e) {
                System.out.println("Fecha introducida incorrecta. Intente nuevamente.");
                sc.nextLine();
            }
        }

        return ret;
    }
    
    public static String encriptarSHA256(String contrasena) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contrasena.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
    
    public static int obtenerOpcionUsuario(int maxOpcion) {
    	Scanner sc = new Scanner(System.in);
        int opcion = -1;
        boolean entradaValida = false;
        while (!entradaValida) {
            try {
                if (sc.hasNextInt()) {
                    opcion = sc.nextInt();
                    sc.nextLine();
                    if (opcion >= 1 && opcion <= maxOpcion) {
                        entradaValida = true;
                    } else {
                        System.err.println("Por favor, ingresa un número entre 1 y " + maxOpcion + ".");
                    }
                } else {
                    System.err.println("Entrada no válida. Por favor, ingresa un número entre 1 y " + maxOpcion + ".");
                    sc.next();
                }
            } catch (InputMismatchException e) {
                System.err.println("Error de entrada. Inténtalo de nuevo.");
                sc.next();
            }
        }
        return opcion;
    }
    
    public static void salirdelprograma() {
        try {
            // Cerrar la conexión antes de salir del programa
            System.out.println("SALIENDO DEL PROGRAMA...");
        } catch (Exception e) {
            System.err.println("Error al cerrar la conexión antes de salir.");
            e.printStackTrace();
        }
        System.exit(0);
    }

}