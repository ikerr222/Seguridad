package p2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcRSAContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.operator.OperatorCreationException;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcContentSignerBuilder;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.bc.BcPKCS10CertificationRequestBuilder;


/**
* Esta clase implementa el comportamiento de un usuario en una Infraestructura de Certificaci�n
* @author Iker Bermejo Lurueña
* @version 1.0
*/
public class Usuario {
	
	private RSAKeyParameters clavePrivada = null;
	private RSAKeyParameters clavePublica = null;

	/**
	 * Metodo que genera las claves del usuario.
	 * @param fichClavePrivada: String con el nombre del fichero donde se guardar� la clave privada en formato PEM
	 * @param fichClavePublica: String con el nombre del fichero donde se guardar� la clave publica en formato PEM
     * @throws IOException 	
	
	 */
	public void generarClavesUsuario(String fichClavePrivada, String fichClavePublica) throws IOException { 
		
			// 1. Generamos pareja de claves
		    GestionClaves gc = new GestionClaves(); 
			AsymmetricCipherKeyPair claves = gc.generarClaves(BigInteger.valueOf(17), 2048); //Clave de tamaño 2048 bits 
			clavePublica = (RSAKeyParameters) claves.getPublic(); 
			clavePrivada = (RSAKeyParameters) claves.getPrivate(); 
			
			// 2. Damos el formato adecuado y guardamos las claves en formato PEM 
			SubjectPublicKeyInfo clavePublica = gc.getClavePublicaSPKI(this.clavePublica); 
			GestionObjetosPEM.escribirObjetoPEM(GestionObjetosPEM.PUBLICKEY_PEM_HEADER, clavePublica.getEncoded(), fichClavePublica); 
			PrivateKeyInfo clavePrivada = gc.getClavePrivadaPKCS8(this.clavePrivada); 
			GestionObjetosPEM.escribirObjetoPEM(GestionObjetosPEM.PKCS8KEY_PEM_HEADER, clavePrivada.getEncoded(), fichClavePrivada); 
			
			 
	            }
			
			 /** 
			  * Metodo que genera una peticion de certificado en 
			formato PEM, almacenando 
			  * esta peticion en un fichero. 
			  *  
			  * @param fichPeticion: String con el nombre del 
			fichero donde se guardara la 
			  *                      peticion de certificado 
			  * @throws IOException 
			  * @throws OperatorCreationException 
			  */
	public void crearPetCertificado(String fichPeticion) throws OperatorCreationException, IOException {
		// IMPLEMENTAR POR EL ESTUDIANTE
 
	   	// Configurar hash para resumen y algoritmo firma 
		// La solicitud se firma con la clave privada del usuario y se escribe en fichPeticion en formato PEM
		


		// IMPLEMENTAR POR EL ESTUDIANTE
		
				  // 1. Verificar que el usuario tiene generadas las claves pública y privada. 
		
				  if (!(clavePublica == null && clavePrivada == null)) { 
					  
				   // 2. Generamos el nombre que irá en la peticion de certificado (x500 del propietario)
					  
				   X500Name subject = new X500Name("C=ES, O=DTE, CN=IKER"); 
				   GestionClaves gc = new GestionClaves();
				   
				   // 3. El usuario instancia un Objeto de la clasePKCS10CertificationRequestBuilder 
				   // que contiene la información de la petición(argumentos nombre y clave pública solicitante).  
				   
				   PKCS10CertificationRequestBuilder requestBuilder = new PKCS10CertificationRequestBuilder(subject,  gc.getClavePublicaSPKI(clavePublica)); 
				   
				   // 4. Configura el resumen y la firma (builder). Instancia un objeto de la clase BcContentSignerBuilder. 
				  
				   DefaultSignatureAlgorithmIdentifierFinder sigAlgFinder = new DefaultSignatureAlgorithmIdentifierFinder(); 
				   DefaultDigestAlgorithmIdentifierFinder digAlgFinder = new DefaultDigestAlgorithmIdentifierFinder(); 
				   AlgorithmIdentifier sigAlgId = sigAlgFinder.find("SHA256withRSA"); 
				   AlgorithmIdentifier digAlgId = digAlgFinder.find(sigAlgId); 
				   BcContentSignerBuilder csBuilder = new BcRSAContentSignerBuilder(sigAlgId, digAlgId); 
				   
				   // Generar la petición y la firma con su clave privada 
				   
				   PKCS10CertificationRequest pet = requestBuilder.build(csBuilder.build(this.clavePrivada)); 
				   
				   // Guardar pet en fichero en formato PEM
				   
				  GestionObjetosPEM.escribirObjetoPEM(GestionObjetosPEM.PKCS10_PEM_HEADER, pet.getEncoded(), fichPeticion); 
				  } 
				  
				  else { 
				   System.out.println("No hay clave generada");  
				   } 
				 }
	
	/**
	 * Método que verifica un certificado de una entidad.
	 * @param fichCertificadoCA: String con el nombre del fichero donde se encuentra el certificado de la CA
	 * @param fichCertificadoUsu: String con el nombre del fichero donde se encuentra el certificado de la entidad
     	 * @throws CertException 
	 * @throws OperatorCreationException 
	 * @throws IOException 
	 * @throws FileNotFoundException 	
	 * @return boolean: true si verificación OK, false en caso contrario.
	 */
	
	public boolean verificarCertificadoExterno(String fichCertificadoCA, String fichCertificadoUsu)  throws OperatorCreationException, 
			CertException, FileNotFoundException, IOException { 
			  
		boolean resultado = false; 
			  
		// 1. Leer certificado del usuario 
			  
		X509CertificateHolder certUsuario = (X509CertificateHolder) GestionObjetosPEM.leerObjetoPEM(fichCertificadoUsu); 
		
		// 2. Comprobar fecha validez del certificado, si la fecha es válida se comprueba la firma
		
		Date fechaActual = new Date(System.currentTimeMillis());
		
		if (fechaActual.before(certUsuario.getNotAfter()) && fechaActual.after(certUsuario.getNotBefore())) { 
			
			// 3. Leer fichCertificadoCA (certificado CA)
			
			X509CertificateHolder certificadoCA = (X509CertificateHolder) GestionObjetosPEM .leerObjetoPEM(fichCertificadoCA); 
			GestionClaves gc = new GestionClaves();    
			
			// Obtener clave pública de la CA en formato  RSAKeyParameters			
			// Clase SubjectPublicKeyInfo y clase GestionClaves.
			
			SubjectPublicKeyInfo clavePubliCA = certificadoCA.getSubjectPublicKeyInfo(); 
		    RSAKeyParameters clavePublicaCA = gc.getClavePublicaMotor(clavePubliCA); 
			   
		    // 4. Generar un contenedor para la verificación 
		    
			DefaultDigestAlgorithmIdentifierFinder signer = new DefaultDigestAlgorithmIdentifierFinder(); 
			ContentVerifierProvider contentVerifierProvider = new BcRSAContentVerifierProviderBuilder(signer).build(clavePublicaCA); 
			   
			// 5. Verificar firma  
			
			if (certUsuario.isSignatureValid(contentVerifierProvider)) { 
	
				resultado = true; // Firma válida 
			   } 
			  } 
			  return resultado; 
	}}
	

	// EL ESTUDIANTE PODR� CODIFICAR TANTOS M�TODOS PRIVADOS COMO CONSIDERE INTERESANTE PARA UNA MEJOR ORGANIZACI�N DEL C�DIGO