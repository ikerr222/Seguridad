import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.engines.ThreefishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.X923Padding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

public class Simétrica {

	public void generarClave(String ficheroClave) throws IOException {
		
		   //Paso 1: Crear objeto generador
		   CipherKeyGenerator genClave = new CipherKeyGenerator(); 
		   
		   //Paso 2: Inicializar objeto generador, número aleatorio de 512 bits
		   genClave.init(new KeyGenerationParameters(new SecureRandom(),512));
		   
		   //Paso 3: Generar clave (generateKey()) devuelve un array de bytes
		   byte[] claveBytes = genClave.generateKey();
		   
		   //Paso 4: Convertir clave a Hexadecimal (Hex.encode())
           //Paso 5: Almacenar clave en fichero
           FileOutputStream ficheroACifrar = new FileOutputStream (ficheroClave);
           ficheroACifrar.write(Hex.encode(claveBytes));
           ficheroACifrar.close();
	}

	public void cifrar(String ficheroClave, String fichACifrar, String fichCifrado) throws IOException {
		
		// Paso 1: Leer clave y decodificar de Hex a bin
		FileInputStream fisClave = new FileInputStream(ficheroClave);
		byte[] claveHex = new byte[(int) fisClave.getChannel().size()];
        fisClave.read(claveHex);
        fisClave.close();
        byte[] claveBytes = Hex.decode(claveHex);
		
		// Paso 2: Generar parámetros y cargar clave
		KeyParameter params = new KeyParameter(claveBytes);
		
		//Paso 3:Crear motor de cifrado 
		// Algoritmo de cifrado: Threefish de 512 bits con CBC
		PaddedBufferedBlockCipher cifrador =new PaddedBufferedBlockCipher(new CBCBlockCipher(new ThreefishEngine(512)));
		// Esquema de relleno: ANSI X923
		new X923Padding();

        // Paso 4: Inicializar motor de cifrado con parámetros y clave 
		//El parámetro booleano true indica que se va a realizar un cifrado.
        cifrador.init(true, params);
        
        // Paso 5: Crear flujos de entrada/salida de archivos
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fichACifrar));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fichCifrado))) {
             
            // Paso 6: Crear arrays de bytes para E/S
            byte[] datosLeidos = new byte[cifrador.getBlockSize()];
            byte[] datosCifrados = new byte[cifrador.getOutputSize(datosLeidos.length)];

            // Paso 7: Bucle de lectura, cifrado y escritura de bloques de datos
            int bytesLeidos;
            int bytesCifrados;
            while ((bytesLeidos = bis.read(datosLeidos)) != -1) {
                bytesCifrados = cifrador.processBytes(datosLeidos, 0, bytesLeidos, datosCifrados, 0);
                //Este método cifra el bloque de datos y lo almacena en el array datosCifrados. 
                //cifrador.processBytes() devuelve la cantidad de bytes cifrados, 
                //que se almacena en bytesCifrados.
                bos.write(datosCifrados, 0, bytesCifrados);
            }

            // Paso 8: Cifrar el último bloque y escribirlo
            bytesCifrados = cifrador.doFinal(datosCifrados, 0);
            //Esto cifra el último bloque de datos (que puede ser menos que el tamaño del bloque completo)
            //y almacena el resultado en datosCifrados.
            bos.write(datosCifrados, 0, bytesCifrados);

        } catch (IOException e) {
            System.err.println("Error al crear los flujos de entrada/salida de archivos: " + e.getMessage());
        } catch (Exception ex) {
            System.err.println("Error al cifrar: " + ex.getMessage());
        }
    
	} 
	
	public void descifrar(String ficheroClave, String fichADescifrar, String fichDescifrado) throws IOException {
        // Paso 1: Leer clave y decodificar de Hex a bin
        FileInputStream fisClave = new FileInputStream(ficheroClave);
        byte[] claveHex = new byte[(int) fisClave.getChannel().size()];
        fisClave.read(claveHex);
        fisClave.close();
        byte[] claveBytes = Hex.decode(claveHex);

        // Paso 2: Generar parámetros y cargar clave
        KeyParameter params = new KeyParameter(claveBytes);

        // Paso 3: Crear motor de descifrado
        PaddedBufferedBlockCipher descifrador = new PaddedBufferedBlockCipher(
            new CBCBlockCipher(new ThreefishEngine(512)), // Algoritmo de cifrado: Threefish de 512 bits con CBC
            new X923Padding() // Esquema de relleno: ANSI X923
        );

        // Paso 4: Inicializar motor de descifrado con parámetros y clave
        descifrador.init(false, params); // false porque descifra

        // Paso 5: Crear flujos de entrada/salida de archivos
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fichADescifrar));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fichDescifrado))) {
             
            // Paso 6: Crear arrays de bytes para E/S
            byte[] datosLeidos = new byte[descifrador.getBlockSize()];
            byte[] datosDescifrados = new byte[descifrador.getOutputSize(datosLeidos.length)];

            // Paso 7: Bucle de lectura, descifrado y escritura de bloques de datos
            int bytesLeidos;
            int bytesDescifrados;
            while ((bytesLeidos = bis.read(datosLeidos)) != -1) {
                bytesDescifrados = descifrador.processBytes(datosLeidos, 0, bytesLeidos, datosDescifrados, 0);
                bos.write(datosDescifrados, 0, bytesDescifrados);
            }

            // Paso 8: Descifrar el último bloque y escribirlo
            bytesDescifrados = descifrador.doFinal(datosDescifrados, 0);
            bos.write(datosDescifrados, 0, bytesDescifrados);

        } catch (IOException e) {
            System.err.println("Error al crear los flujos de entrada/salida de archivos: " + e.getMessage());
        } catch (Exception ex) {
            System.err.println("Error al descifrar: " + ex.getMessage());
        }}
}


