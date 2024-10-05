package p2;

import java.util.Scanner;

public class Principal {
  
    public static void main(String[] args) throws Exception {
        // Se pueden tratar las excepciones en lugar de implementar throws
        Usuario u = new Usuario();
        CA ca = new CA();

        int menu1;
        int menu2;
        Scanner sc = new Scanner(System.in);
        String fichero;
        
        // Para trabajo como usuario
        String ficheroClavePrivada;
        String ficheroClavePublica;
        
        // Para trabajo como CA
        String ficheroCA = null;
        String ficheroCertUsu = null;

        do {
            System.out.println("¿Con qué rol desea trabajar?");
            System.out.println("1. Trabajar como usuario.");
            System.out.println("2. Trabajar como Autoridad de Certificación.");
            System.out.println("3. Salir.");
            menu1 = sc.nextInt();

            switch (menu1) {
                case 1:
                    do {
                        System.out.println("Elija una opción para trabajar como USUARIO:");
                        System.out.println("0. Volver al menú anterior.");
                        System.out.println("1. Generar pareja de claves en formato PEM.");
                        System.out.println("2. Crear petición de certificación.");
                        System.out.println("3. Verificar certificado externo.");
                        menu2 = sc.nextInt();

                        switch (menu2) {
                            case 1: // Generar pareja de claves.
                                System.out.println("OPCIÓN GENERA PAREJA DE CLAVES");
                                System.out.println("Escriba el nombre del fichero que contendrá la clave privada:");
                                ficheroClavePrivada = sc.next();
                                System.out.println("Escriba el nombre del fichero que contendrá la clave pública:");
                                ficheroClavePublica = sc.next();
                                // COMPLETAR POR EL ESTUDIANTE
                                u.generarClavesUsuario(ficheroClavePrivada, ficheroClavePublica);
                                break;

                            case 2: // Crear petición de certificado.
                                System.out.println("Escriba nombre fichero para la petición de certificación:");
                                fichero = sc.next();
                                u.crearPetCertificado(fichero);
                                break;

                            case 3: // Verificar certificado externo.
                                System.out.println("Escriba el nombre del fichero que contiene el certificado del usuario:");
                                fichero = sc.next();
                                System.out.println("Escriba el nombre del fichero que contiene el certificado de la CA:");
                                ficheroCA = sc.next();

                                if (u.verificarCertificadoExterno(ficheroCA, fichero)) {
                                    System.out.println("El certificado ha sido verificado correctamente");
                                } else {
                                    System.out.println("No se ha podido verificar el certificado");
                                }
                                break;
                        }
                    } while (menu2 != 0);
                    break;

                case 2:
                    do {
                        System.out.println("Elija una opción para trabajar como CA:");
                        System.out.println("0. Volver al menú anterior.");
                        System.out.println("1. Generar pareja de claves y certificado autofirmado.");
                        System.out.println("2. Cargar pareja de claves.");
                        System.out.println("3. Generar un certificado a partir de una petición.");
                        menu2 = sc.nextInt();

                        switch (menu2) {
                            case 1: // Generar pareja de claves, el certificado X509 y guardar en ficheros.
                                ca.generarClavesyCertificado();
                                System.out.println("Claves y certificados X509 GENERADOS");
                                System.out.println("Se han guardado en " + CA.NOMBRE_FICHERO_CRT + ", " + CA.NOMBRE_FICHERO_CLAVES + "-*.txt");
                                break;

                            case 2: // Cargar del fichero con la pareja de claves
                                // COMPLETAR POR EL ESTUDIANTE
                                ca.cargarClaves();
                                System.out.println("Claves CARGADAS");
                                System.out.println("Se han cargado de " + CA.NOMBRE_FICHERO_CLAVES + "-*.txt");
                                break;

                            case 3: // Generar certificado a partir de una petición
                                System.out.println("Escriba el nombre del fichero que contiene la petición de certificación del usuario:");
                                fichero = sc.next();
                                System.out.println("Escriba el nombre del fichero que contendrá el certificado emitido por la CA para el usuario:");
                                ficheroCertUsu = sc.next();

                                if (ca.certificarPeticion(fichero, ficheroCertUsu)) {
                                    System.out.println("El certificado ha sido generado correctamente");
                                } else {
                                    System.out.println("No se ha podido generar el certificado correctamente, la petición no cumple los requisitos");
                                }
                                break;
                        }
                    } while (menu2 != 0);
                    break;
            }
        } while (menu1 != 3);
        sc.close();
    }
}
