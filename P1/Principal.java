/**Fichero: Principal.java
 * Clase para comprobar el funcionamiento de las otras clases del paquete.
 * Asignatura: SEG
 * @author Profesores de la asignatura
 * @version 1.0
 */

import java.io.IOException;
import java.util.Scanner;

import org.bouncycastle.crypto.InvalidCipherTextException;

public class Principal {

	public static void main (String [ ] args) throws IOException, InvalidCipherTextException {
		int menu1;
		int menu2;
		Scanner sc = new Scanner(System.in);
		/* completar declaracion de variables e instanciación de objetos */
		 Simétrica s = new Simétrica();
		 Asimétrica a = new Asimétrica();
		 
		do {
			System.out.println("¿Qué tipo de criptografía desea utilizar?");
			System.out.println("1. Simétrico.");
			System.out.println("2. Asimétrico.");
			System.out.println("3. Salir.");
			menu1 = sc.nextInt();
		
			switch(menu1){
				case 1:
					do{
						System.out.println("Elija una opción para CRIPTOGRAFIA SIMÉTRICA:");
						System.out.println("0. Volver al menú anterior.");
						System.out.println("1. Generar clave.");
						System.out.println("2. Cifrado.");
						System.out.println("3. Descifrado.");
						menu2 = sc.nextInt();
				
						switch(menu2){
							case 1: //case generar clave:leer nombre fichero y generar clave
								/*completar acciones*/
								Scanner sc2 = new Scanner(System.in);  
                                System.out.print("Nombre del fichero: "); 
                                String nomFicheroKey=sc2.next(); 
                                s.generarClave(nomFicheroKey);
								
								
							break;
							case 2: //leer nombre fichero clave, nombre fichero a cifrar y nombre donde dejar cifrado y cifrar
								/*completar acciones*/

                                Scanner sc3 = new Scanner(System.in);                        
                                System.out.print("Nombre de fichero con clave: "); 
                                String ficheroClave=sc3.next(); 
                                 
                                Scanner sc4 = new Scanner(System.in); 
                                System.out.println("\nFichero a cifrar:"); 
                                String fichACifrar= sc4.next(); 
                                 
                                Scanner sc5 = new Scanner(System.in); 
                                System.out.println("\nFichero donde desea almacenar el resultado del cifrado: "); 
                                String fichCifrado = sc5.next(); 
                                 
                                s.cifrar(ficheroClave, fichACifrar, fichCifrado);
								
							break;
							case 3: //leer nombre fichero clave, nombre fichero a descifrar y nombre donde dejar descifrado y descifrar
								/*completar acciones*/
								System.out.print("Nombre del fichero con clave: ");
                                String ficheroClaveDescifrar = sc.next();
                                
                                System.out.print("\nFichero a descifrar: ");
                                String ficheroADescifrar = sc.next();
                                
                                System.out.print("\nFichero donde desea almacenar el resultado del descifrado: ");
                                String ficheroDescifrado = sc.next();
                                
                                s.descifrar(ficheroClaveDescifrar, ficheroADescifrar, ficheroDescifrado);
								
								
							break;
						}
					} while(menu2 != 0);
				break;
				case 2:
					do{
						System.out.println("Elija una opción para CRIPTOGRAFIA ASIMÉTRICA:");
						System.out.println("0. Volver al menú anterior.");
						System.out.println("1. Generar clave.");
						System.out.println("2. Cifrado.");
						System.out.println("3. Descifrado.");
						System.out.println("4. Firmar digitalmente.");
						System.out.println("5. Verificar firma digital.");
						menu2 = sc.nextInt();
				
						switch(menu2){
							case 1://Generar claves
								/*completar acciones*/
								//leer nombre fichero Ks y nombre fichero Kp y generar claves
								Scanner sc2 = new Scanner(System.in);  
                                System.out.println("Nombre del fichero Clave Privada: "); 
                                String ficheroKs=sc2.next(); 
                                Scanner sc3 = new Scanner(System.in);
                                System.out.println("Nombre del fichero Clave Pública: ");
                                String ficheroKp=sc3.next();
                                a.generarClaves(ficheroKs, ficheroKp);
                                
                                
							break;
							case 2://Cifrar
								/*completar acciones*/
								//Leer ¿privada o publica?
								//leer nombre fichero clave, nombre fichero a
								//cifrar y nombre fichero donde dejar cifrado y cifrar
								
								/*
								 Caso 1: Confidencialidad
								 A->B
								 Fichero clave: Clave pública de B
								 FicheroACifrar: Mensaje de A
								 FicheroCifrado: Mensaje cifrado de A
								 
								 Caso 2: Autenticación
								 A->B
								 Fichero clave: Clave privada de A
								 FicheroACifrar: Mensaje de A
								 FicheroCifrado: Mensaje cifrado de A
								 
								 */
								Scanner sc7 = new Scanner(System.in);                        
                                System.out.print("Clave Pública o Privada: "); 
                                String tipo=sc7.next();
								
								Scanner sc4 = new Scanner(System.in);                        
                                System.out.print("Nombre de fichero con clave Pública o Privada: ");
                                String ficheroClave=sc4.next(); 
                                 
                                Scanner sc5 = new Scanner(System.in); 
                                System.out.println("\nFichero a cifrar:"); 
                                String fichACifrar= sc5.next(); 
                                 
                                Scanner sc6 = new Scanner(System.in); 
                                System.out.println("\nFichero donde desea almacenar el resultado del cifrado: "); 
                                String fichCifrado = sc6.next(); 
                                 
                                a.cifrar(tipo, ficheroClave, fichACifrar, fichCifrado);
								
								
								
							break;
							case 3://Descifrar
								/*
								 A-<B
								 Fichero clave: Clave pública de B (Confidencialidad)
								 FicheroACifrar: Mensaje de A
								 FicheroCifrado: Mensaje cifrado de A
								 Tipo a usar ahora: Clave privada de B
								 
								 A-<B
								 Fichero clave: Clave privada de A (Autenticación)
								 FicheroACifrar: Mensaje de A
								 FicheroCifrado: Mensaje cifrado de A
								 Tipo a usar ahora: Clave pública de A
								 */
								/*completar acciones*/
								
								Scanner sc9 = new Scanner(System.in);                        
							    System.out.print("Clave Pública o Privada: "); 
							    String tipo1  = sc.next();
						
							    System.out.print("Nombre del fichero con clave: ");
							    String ficheroClaveDescifrar = sc9.next();
							    
							    System.out.print("\nFichero a descifrar: ");
							    String ficheroADescifrar = sc9.next();
							    
							    System.out.print("\nFichero donde desea almacenar el resultado del descifrado: ");
							    String ficheroDescifrado = sc9.next();
							  
							    
							    a.descifrar(tipo1, ficheroClaveDescifrar, ficheroADescifrar, ficheroDescifrado);
							break;
								
							
							case 4: //Firmar
								/*completar acciones*/
								Scanner sc10 = new Scanner(System.in);                        
							    System.out.print("Fichero con clave privada: "); 
							    String ficheroClave2 = sc10.next();
							                         
							    System.out.print("Fichero donde desea almacenar la firma: "); 
							    String ficheroFirmado  = sc10.next();
							    
							    System.out.print("Fichero mensaje original: "); 
							    String ficheroMensaje  = sc10.next();                        
							    
							    
							    a.firmar(ficheroClave2, ficheroMensaje, ficheroFirmado);
							    
								
								
							break;
							case 5: //Verificar Firma
								/*completar acciones*/
								Scanner sc11 = new Scanner(System.in);                        
							    System.out.print("Fichero con clave publica: "); 
							    String ficheroClave1  = sc11.next();
							                         
							    System.out.print("Fichero firmado: "); 
							    String ficheroAVerificar  = sc11.next();
							                           
							    System.out.print("Fichero mensaje original: "); 
							    String ficheroMensajeClaro  = sc11.next();
							    
							    a.verificar(ficheroClave1, ficheroMensajeClaro, ficheroAVerificar);
							    
							break;
						}
					} while(menu2 != 0);
				break;
			}			
		} while(menu1 != 3);
		sc.close();
	}
}
