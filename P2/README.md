# Práctica 2: Autoridad de Certificación (CA) con Funcionalidad Mínima

Este repositorio contiene la implementación de una **Autoridad de Certificación (CA)** con funcionalidades básicas, desarrollada como parte de la asignatura de **Seguridad en Redes y Servicios** de la DTE-UPM. Esta práctica está dividida en dos partes, en las cuales se desarrollan distintas funcionalidades para gestionar una CA y usuarios que interactúan con ella.

## Objetivo de la Práctica

El objetivo de esta práctica es implementar una **Autoridad de Certificación (CA)** con funcionalidad mínima que permita la emisión, gestión y verificación de certificados digitales. Los alumnos aprenderán a utilizar **Java** junto con la **API criptográfica Bouncy Castle** para desarrollar estas capacidades.

## Parte 1: Implementación Básica de la Autoridad de Certificación

En la primera parte de la práctica, se desarrollarán las siguientes funcionalidades:

1. **Generación de Claves**:
   - Implementar la clase `Usuario` con el método `generarClavesUsuario()` para generar y almacenar las claves públicas y privadas del usuario.
   - Implementar la clase `CA` con el método `generarClavesyCertificado()` para generar un par de claves para la CA y emitir un certificado autofirmado.

2. **Parámetros del Certificado**:
   - Configurar los parámetros del certificado utilizando `X509v3CertificateBuilder`, incluyendo el nombre del sujeto, número de serie, validez, y la clave pública del sujeto.

3. **Firma del Certificado**:
   - Configurar y firmar el certificado con la clave privada de la CA utilizando `BcContentSignerBuilder`.

4. **API Utilizada**:
   - Utilizar la **API criptográfica Bouncy Castle** para la generación y gestión de claves y certificados. Se requiere añadir los `.jar` proporcionados (`bcprov-jdk15on-168.jar` y `bcpkix-jdk15on-168.jar`) al proyecto.

## Parte 2: Extensión de Funcionalidades

En la segunda parte de la práctica, se extenderá la funcionalidad de la CA y del usuario:

1. **Creación de una Petición de Certificado**:
   - Implementar el método `crearPetCertificado()` en la clase `Usuario` para crear una solicitud de certificado. La solicitud incluirá el nombre del sujeto (`X.500`), la clave pública y otros atributos opcionales.
   - Utilizar el estándar PKCS #10 para la creación de la petición de certificado.

2. **Verificación de Certificados**:
   - Implementar el método `verificarCertificadoExterno()` en la clase `Usuario` para verificar un certificado emitido por la CA.
   - Leer el certificado, comprobar la validez de la fecha y verificar la firma con la clave pública de la CA.

3. **Certificación de Peticiones**:
   - Implementar el método `certificarPeticion()` en la clase `CA` para emitir un certificado basado en la petición del usuario.

## Requisitos Técnicos

- **Lenguaje de programación**: Java
- **Entorno de desarrollo**: Eclipse
- **Librerías**: Bouncy Castle versión 1.6
  - [Bouncy Castle Lightweight API](https://www.bouncycastle.org/docs/docs1.5/index.html)
  - [Bouncy Castle PKIX API](https://www.bouncycastle.org/docs/pkixdocs1.5on/index.html)

## Estructura del Proyecto

- **Clases Principales**:
  - `Usuario`: Representa un usuario que solicita certificados.
  - `CA`: Representa la Autoridad de Certificación que emite y gestiona los certificados.
  - `GestionClaves`: Clase auxiliar para la generación y gestión de claves.
  - `GestionObjetosPEM`: Clase auxiliar para gestionar los objetos en formato PEM.

## Ejecución

1. **Configuración del Entorno**:
   - Asegúrate de tener configurado el entorno en Eclipse y de incluir las librerías de Bouncy Castle en el proyecto.
  
2. **Generación de Claves y Certificados**:
   - Utiliza la clase `Usuario` para generar un par de claves y crear una petición de certificado.
   - Utiliza la clase `CA` para generar su propio certificado y firmar las peticiones de los usuarios.

3. **Verificación**:
   - Verifica la validez de los certificados utilizando la clase `Usuario`.

## Notas Importantes

- El camino de certificación está limitado a un máximo de **5**.
- Reutiliza el código ya implementado en métodos anteriores para realizar nuevas funcionalidades, como `certificarPeticion()`.

## Entrega

La práctica debe ser entregada a través de la plataforma Moodle antes de la fecha indicada:

- Alumnos del martes: **lunes 15 de abril de 2024 a las 23:55**.
- Alumnos del jueves: **miércoles 10 de abril de 2024 a las 23:55**.

## Autores

- M.L. Martín, P. Castillejo, E. Gago / DTE-UPM
