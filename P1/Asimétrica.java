import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.util.encoders.Hex;

public class Asimétrica {

	public void generarClaves(String ficheroKs, String ficheroKp) {
		
		//Paso 1: Generación parámetros para inicializar el generador de claves 17-Numero primo pequeño 2048-Tamaño de la clave en bits
		RSAKeyGenerationParameters parametros =
				new RSAKeyGenerationParameters(BigInteger.valueOf(17),
				new SecureRandom(), 2048, 10); 
		
		//Paso 2: Instanciar el generador de claves
		RSAKeyPairGenerator generadorClaves = new RSAKeyPairGenerator();
		
		//Paso 3: Inicializarlo
		generadorClaves.init(parametros);
		
		//paso 4: Generar claves
		AsymmetricCipherKeyPair parejaClaves = generadorClaves.generateKeyPair();
		
		//Paso 5: Obtener clave privada y pública
		RSAKeyParameters clavePrivada = (RSAKeyParameters) parejaClaves.getPrivate();
		RSAKeyParameters clavePublica = (RSAKeyParameters) parejaClaves.getPublic();
		
		//Paso 6: Guardar cada clave en un fichero
		try {
			PrintWriter ficheroPrivada = new PrintWriter(new FileWriter(ficheroKs));
			ficheroPrivada.println(new String (Hex.encode(clavePrivada.getModulus().toByteArray())));
			ficheroPrivada.print(new String (Hex.encode(clavePrivada.getExponent().toByteArray())));
			ficheroPrivada.close();
			} catch (FileNotFoundException e) {
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
			}
		
		try {
			PrintWriter ficheroPublica = new PrintWriter(new FileWriter(ficheroKp));
			ficheroPublica.println(new String (Hex.encode(clavePublica.getModulus().toByteArray())));
			ficheroPublica.print(new String (Hex.encode(clavePublica.getExponent().toByteArray())));
			ficheroPublica.close();
			} catch (FileNotFoundException e) {
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
			}
		
}

	public void cifrar(String tipo, String ficheroClave, String fichACifrar, String fichCifrado) throws IOException, InvalidCipherTextException {
		
		//Identificar con qué clave ha venido el fichero.
		
		boolean tipoClave = false;
		
		if(tipo.equals("privada")) { 
            
            tipoClave=true; 
             
        }else{ // else if(tipo.equals("publica") por si acaso el acento
             
            tipoClave=false; 
            
        }
		
		// Paso 1: Leer el modulo y el exponente de la clave
		BufferedReader lectorClave = new BufferedReader(new FileReader(ficheroClave));
		BigInteger modulo = new BigInteger(Hex.decode(lectorClave.readLine()));
		BigInteger exponente = new BigInteger(Hex.decode(lectorClave.readLine()));
				
		// Paso 2: Generación parámetros para inicializar el cifrador
		RSAKeyParameters parametros = new RSAKeyParameters(tipoClave, modulo, exponente);
		
		
		// Paso 3: Instanciar el cifrador
		AsymmetricBlockCipher cifrador = new PKCS1Encoding(new RSAEngine());
        
		// Paso 4: Inicializarlo
		cifrador.init(true, parametros);
		
		
		// Paso 5: Leer bloques del fichero a cifrar e ir cifrando
		BufferedInputStream lectorFichero = new BufferedInputStream(new FileInputStream(fichACifrar));
		BufferedOutputStream escritorFichero = new BufferedOutputStream(new FileOutputStream(fichCifrado));
        byte[] bufferFichero = new byte[cifrador.getInputBlockSize()];
        int bytesLeidos;
		while ((bytesLeidos = lectorFichero.read(bufferFichero, 0,cifrador.getInputBlockSize() )) != -1) {
            byte[] datosCifrados = cifrador.processBlock(bufferFichero, 0, bytesLeidos);
            escritorFichero.write(datosCifrados);
        }
        
        lectorFichero.close();
        lectorClave.close();
        escritorFichero.close();
    }

	    public void descifrar(String tipo, String ficheroClave, String ficheroCifrado, String ficheroDescifrado)
	            throws IOException, InvalidCipherTextException {

	    	boolean tipoClave = false;
			
			if(tipo.equals("privada")) { 
	            
	            tipoClave=true; 
	             
	        }else{ // else if(tipo.equals("publica") por si acaso el acento
	             
	            tipoClave=false; 
	            
	        }
	    	
	        // Paso 1: Leer el modulo y el exponente de la clave
	        BufferedReader lectorClave = new BufferedReader(new FileReader(ficheroClave));
	        BigInteger modulo = new BigInteger(Hex.decode(lectorClave.readLine()));
	        BigInteger exponente = new BigInteger(Hex.decode(lectorClave.readLine()));

	        // Paso 2: Generación parámetros para inicializar el cifrador
	        RSAKeyParameters parametros = new RSAKeyParameters(tipoClave, modulo, exponente);

	        // Paso 3: Instanciar el cifrador
	        AsymmetricBlockCipher cifrador = new PKCS1Encoding(new RSAEngine());

	        // Paso 4: Inicializarlo
	        cifrador.init(false, parametros);

	        // Paso 5: Leer bloques del fichero cifrado e ir descifrando
	        BufferedInputStream lectorFichero = new BufferedInputStream(new FileInputStream(ficheroCifrado));
	        BufferedOutputStream escritorFichero = new BufferedOutputStream(new FileOutputStream(ficheroDescifrado));
	        byte[] bufferFichero = new byte[cifrador.getInputBlockSize()];
	        int bytesLeidos;
	        while ((bytesLeidos = lectorFichero.read(bufferFichero, 0, cifrador.getInputBlockSize())) != -1) {
	            byte[] datosDescifrados = cifrador.processBlock(bufferFichero, 0, bytesLeidos);
	            escritorFichero.write(datosDescifrados);
	        }

	        // Cerrar flujos de entrada y salida
	        lectorFichero.close();
	        escritorFichero.close();
	        lectorClave.close();
	    }

	    public void firmar(String ficheroClave, String ficheroMensaje, String ficheroFirmado) throws IOException, InvalidCipherTextException {
	        // Paso 1: Instanciar la clase para generar el resumen
	        Digest resumen = new SHA1Digest();
	        
	        // Paso 2: Generar el resumen: los bloques de lectura son del mismo tamaño que el resumen
	        byte[] buffer = new byte[resumen.getDigestSize()];
	        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(ficheroMensaje));
	        int bytesLeidos;
	        
	        // Se lee el archivo de mensaje y se actualiza el resumen con sus bytes
	        while ((bytesLeidos = fis.read(buffer)) > 0) {
	            resumen.update(buffer, 0, bytesLeidos);
	        }
	        fis.close();
	        
	        // Se finaliza el resumen y se guarda en un arreglo
	        byte[] resumenFinal = new byte[resumen.getDigestSize()];
	        resumen.doFinal(resumenFinal, 0);
	        
	        // Paso 3: Guardar el resumen en un fichero intermedio
	        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream("Resumen.txt")); 
	        fos.write(resumenFinal);
	        fos.close();
	        
	        // Paso 4: Cifrar el fichero que contiene el resumen
	        this.cifrar("privada", ficheroClave, "Resumen.txt", ficheroFirmado);
	    }

	    public boolean verificar(String ficheroClave1, String ficheroMensajeClaro, String ficheroAVerificar) throws IOException, InvalidCipherTextException {
	        // Paso 1: Descifrar el fichero que contiene la firma
	        this.descifrar("publica", ficheroClave1, ficheroAVerificar, "resumenV.txt");

	        // Paso 2: Generar el resumen del texto en claro
	        Digest resumen = new SHA1Digest();
	        byte[] buffer = new byte[resumen.getDigestSize()];
	        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(ficheroMensajeClaro));
	        int bytesLeidos;

	        // Se lee el archivo de mensaje claro y se actualiza el resumen con sus bytes
	        while ((bytesLeidos = fis.read(buffer)) > 0) {
	            resumen.update(buffer, 0, bytesLeidos);
	        }
	        fis.close();
	        
	        // Se finaliza el resumen y se guarda en un arreglo
	        byte[] resumenMensaje = new byte[resumen.getDigestSize()];
	        resumen.doFinal(resumenMensaje, 0);

	        // Paso 3: Leer el contenido del fichero de la firma descifrada y verificar si es igual al resumen
	        BufferedInputStream fisFirma = new BufferedInputStream(new FileInputStream("resumenV.txt"));
	        byte[] firma = new byte[resumenMensaje.length];
	        fisFirma.read(firma);
	        fisFirma.close();

	        // Comparar los resúmenes para verificar la firma
	        boolean verificado = Arrays.equals(resumenMensaje, firma);

	        // Se imprime el resultado de la verificación
	        if (verificado) {
	            System.out.println("La firma es válida.");
	        } else {
	            System.out.println("La firma NO es válida.");
	        }
	        return verificado;
	    }
	
}
