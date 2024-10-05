package p2;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import java.util.Date;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcContentSignerBuilder;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.bc.BcRSAContentVerifierProviderBuilder;


/**
* Esta clase implementa el comportamiento de una CA
* @author Seg Red Ser
* @version 1.0
*/
public class CA {
	
	private final X500Name nombreEmisor;
	private BigInteger numSerie;
	private final int añosValidez; 
	
	public final static String NOMBRE_FICHERO_CRT = "CertificadoCA.crt";
	public final static String NOMBRE_FICHERO_CLAVES = "CA-claves";
	
	private RSAKeyParameters clavePrivadaCA = null;
	private RSAKeyParameters clavePublicaCA = null;
	
	/**
	 * Constructor de la CA. 
	 * Inicializa atributos de la CA a valores por defecto
	 */
	public CA () {
		// Distinguished Name DN. C Country, O Organization name, CN Common Name. 
		this.nombreEmisor = new X500Name ("C=ES, O=DTE, CN=CA");
		this.numSerie = BigInteger.valueOf(1);
		this.añosValidez = 4; // Son los años de validez del certificado de usuario, para la CA el valor es 4
	}
	
	/**
	* M�todo que genera la parejas de claves y el certificado autofirmado de la CA.
	* @throws OperatorCreationException
	* @throws IOException 
	*/
	public void generarClavesyCertificado() throws OperatorCreationException, IOException {
		
		// 1. Generar una pareja de claves (clase GestionClaves) y se guardan en formato PEM
        // (añadiendo al nombre las cadenas "_pri.txt" y "_pu.txt")
		
		GestionClaves gc = new GestionClaves(); 
		AsymmetricCipherKeyPair claves = gc.generarClaves(BigInteger.valueOf(17), 2048); 
		
		this.clavePublicaCA = (RSAKeyParameters) claves.getPublic(); 
		this.clavePrivadaCA = (RSAKeyParameters) claves.getPrivate(); 
		
		PrivateKeyInfo clavePrivada = gc.getClavePrivadaPKCS8(clavePrivadaCA); 
		GestionObjetosPEM.escribirObjetoPEM(GestionObjetosPEM.PKCS8KEY_PEM_HEADER, clavePrivada.getEncoded(), NOMBRE_FICHERO_CLAVES+"_pri.txt"); 
		SubjectPublicKeyInfo clavePublica = gc.getClavePublicaSPKI(clavePublicaCA); 
		GestionObjetosPEM.escribirObjetoPEM(GestionObjetosPEM.PUBLICKEY_PEM_HEADER, clavePublica.getEncoded(), NOMBRE_FICHERO_CLAVES+"_pu.txt");
		
		// Generar un certificado autofirmado:
		
		// 2. Configurar parámetros para el certificado e instanciar objeto X509v3CertificateBuilder
		
        Date    fechaActual = new Date(System.currentTimeMillis());
		
		Calendar calendario = GregorianCalendar.getInstance();		
		calendario.add(Calendar.YEAR, 4);

		Date fechaVencimiento = calendario.getTime();;
		
		X509v3CertificateBuilder certBldr = new X509v3CertificateBuilder(nombreEmisor, numSerie, fechaActual, 
				fechaVencimiento, nombreEmisor, clavePublica); 
		
		// 3. Añadir extensión Basic Constraints para indicar que es un certificado de CA
        
		/*BasicConstraints basicConstraints = new BasicConstraints(5); // Restricción de longitud de ruta=5
	    ASN1ObjectIdentifier basicConstraintsOID = new ASN1ObjectIdentifier("2.5.29.19"); 
		certBldr.addExtension(basicConstraintsOID, true, basicConstraints); 
	     */
		
		certBldr.addExtension(Extension.basicConstraints, true, new BasicConstraints(5));
		
		// 4. Configurar hash para resumen y algoritmo firma 
		// Configura firma en CA y Usuario
		
		DefaultSignatureAlgorithmIdentifierFinder sigAlgFinder = new DefaultSignatureAlgorithmIdentifierFinder();
		DefaultDigestAlgorithmIdentifierFinder digAlgFinder = new DefaultDigestAlgorithmIdentifierFinder();
		AlgorithmIdentifier sigAlgId = sigAlgFinder.find("SHA256withRSA");
		AlgorithmIdentifier digAlgId = digAlgFinder.find(sigAlgId);

		// Configurar el constructor del firmante
		
		BcContentSignerBuilder csBuilder = new BcRSAContentSignerBuilder(sigAlgId, digAlgId);
		
		//	5. Generar certificado
		//Se genera y guarda en fichero (en formato PEM y con extensión .crt) el certificado autofirmado
		
		X509CertificateHolder holder = certBldr.build(csBuilder.build(this.clavePrivadaCA));
		
		//	6. Guardar el certificado en formato PEM como un fichero con extension crt
		
		GestionObjetosPEM.escribirObjetoPEM(GestionObjetosPEM.
				 CERTIFICATE_PEM_HEADER, holder.getEncoded(), 
				NOMBRE_FICHERO_CRT); 
	}



	/**
	 * M�todo que carga la parejas de claves
	 * @throws IOException 
	 */
	public void cargarClaves() throws IOException { 
		// Carga la pareja de claves de los ficheros indicados por NOMBRE_FICHERO_CLAVES 
		// (añadiendo al nombre las cadenas "_pri.txt" y "_pu.txt") 
		// No carga el certificado porque se lee de fichero cuando se necesita. 
		
		GestionClaves gc = new GestionClaves(); 
		clavePrivadaCA = gc.getClavePrivadaMotor((PrivateKeyInfo) 
		GestionObjetosPEM.leerObjetoPEM(NOMBRE_FICHERO_CLAVES+"_pri.txt")); 
		clavePublicaCA = gc.getClavePublicaMotor((SubjectPublicKeyInfo)
		GestionObjetosPEM.leerObjetoPEM(NOMBRE_FICHERO_CLAVES+"_pu.txt")); 
		} 
		/** 
		* M�todo que genera el certificado de un usuario a 
		partir de una petici�n de 
		* certificaci�n 
		*  
		* @param ficheroPeticion:String. Par�metro con la 
		petici�n de certificaci�n 
		  * @param ficheroCertUsu:String.  Par�metro con el 
		nombre del fichero en el que 
		  *                                se guardar� el 
		certificado del usuario 
		  * @throws IOException 
		  * @throws PKCSException 
		  * @throws OperatorCreationException 
		  */ 
		 public boolean certificarPeticion(String ficheroPeticion, String ficheroCertUsu) throws IOException, OperatorCreationException, PKCSException { 
		 
			 // Si la verificacon es ok, se genera el certificado firmado con la clave 
			 // privada de la CA 
			 // Se guarda el certificado en formato PEM como un fichero con extension crt 
		 
			 boolean certificado = false; 
			 GestionClaves gc = new GestionClaves(); 
		 
			 // 1. Verificar que están generadas las clave privada y publica de la CA 	  
		 
			 if (clavePrivadaCA != null && clavePublicaCA != null) { 
		  
				 // 2. Leemos la peticion de certificado y obtenemos el nombre de quien solicita el 
				 // certificado y la clave publica de la peticion. 
		  
				 PKCS10CertificationRequest certRequest = (PKCS10CertificationRequest) GestionObjetosPEM.leerObjetoPEM(ficheroPeticion); 
				 //X500Name nombre = certRequest.getSubject(); 
				 RSAKeyParameters clavePublicaPetCert = gc.getClavePublicaMotor(certRequest.getSubjectPublicKeyInfo()); 
		 
				 DefaultDigestAlgorithmIdentifierFinder signer = new DefaultDigestAlgorithmIdentifierFinder(); 
		  
				 ContentVerifierProvider contentVerifierProvider = new BcRSAContentVerifierProviderBuilder(signer).build(clavePublicaPetCert); 
		   
				 // 3. Verificar firma del solicitante (KPSolicitante en fichero de peticion)   
		   
				 if (certRequest.isSignatureValid(contentVerifierProvider)) { 
		   
					 // Obtenemos la fecha de expedicion y la fecha de vencimiento (1 año) 
		    
				Date fechaActual = new Date(System.currentTimeMillis());
		    			
		    	Calendar calendario = GregorianCalendar.getInstance();		
		    	calendario.add(Calendar.YEAR, 1);

		    	Date fechaVencimiento = calendario.getTime();

					 
					 // 4. Configurar e instanciar el builder o contenedor del certificado
		   
					 X509v3CertificateBuilder certBldr = new X509v3CertificateBuilder(nombreEmisor, numSerie, fechaActual, 
							 fechaVencimiento, certRequest.getSubject(), gc.getClavePublicaSPKI(clavePublicaPetCert)); 
		    
					 // Este código se utiliza para establecer restricciones básicas en un certificado X.509, lo que determina si el
					 // certificado es una entidad de certificación o un certificado final de usuario
		    
					/* BasicConstraints basicConstraints = new BasicConstraints(5); 
					 ASN1ObjectIdentifier basicConstraintsOID = new ASN1ObjectIdentifier("2.5.29.19");
					 certBldr.addExtension(basicConstraintsOID, true, basicConstraints); 
		             */
					
					 certBldr.addExtension(Extension.basicConstraints, true, new BasicConstraints(5));
					
		   
					 // Procedemos a crear los objetos para realizar la firma. 
		
					 DefaultSignatureAlgorithmIdentifierFinder sigAlgFinder  = new DefaultSignatureAlgorithmIdentifierFinder();// Firma 
					 DefaultDigestAlgorithmIdentifierFinder digAlgFinder = new DefaultDigestAlgorithmIdentifierFinder();// Resumen 
		 
					 AlgorithmIdentifier sigAlgId = sigAlgFinder.find("SHA256withRSA"); 
					 AlgorithmIdentifier digAlgId = digAlgFinder.find(sigAlgId); 
					 BcContentSignerBuilder csBuilder = new BcRSAContentSignerBuilder(sigAlgId, digAlgId); 
		   
					 // 5. Firmamos y guardamos en  formato PEM la firma. 
		   
					 X509CertificateHolder holder = certBldr.build(csBuilder.build(clavePrivadaCA)); 
		 
					 GestionObjetosPEM.escribirObjetoPEM(GestionObjetosPEM.CERTIFICATE_PEM_HEADER, holder.getEncoded(),  ficheroCertUsu); 
		   
					 certificado = true; 
		   } 
		 
		  } 
		  return certificado; 
		} 
		}

	// EL ESTUDIANTE PODR� CODIFICAR TANTOS M�TODOS PRIVADOS COMO CONSIDERE INTERESANTE PARA UNA MEJOR ORGANIZACI�N DEL C�DIGO