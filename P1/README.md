# Práctica 1: Criptografía Simétrica y Asimétrica

Este repositorio contiene la implementación de una práctica relacionada con la **criptografía simétrica y asimétrica**, desarrollada como parte de la asignatura **Seguridad en Redes y Servicios** en el curso 2023-2024. El objetivo de la práctica es estudiar y manejar **APIs criptográficas** utilizando **Java** y la **API Bouncy Castle**.

## Objetivo de la Práctica

El objetivo de esta práctica es implementar un sistema de cifrado y descifrado de mensajes utilizando criptografía simétrica y asimétrica, permitiendo a los usuarios enviar y recibir mensajes de manera segura y, en el caso de la criptografía asimétrica, también garantizar la autenticidad de los mensajes.

## Descripción General

La práctica está dividida en dos partes:

1. **Criptografía Simétrica**: Uso de una única clave para cifrar y descifrar datos.
2. **Criptografía Asimétrica**: Uso de un par de claves (pública y privada) para cifrar, descifrar y firmar mensajes.

## Funcionalidades Implementadas

### 1. Criptografía Simétrica

La criptografía simétrica utiliza una única clave secreta tanto para el cifrado como para el descifrado de datos. En la práctica, se utiliza el algoritmo **Threefish** en modo **CBC (Cipher Block Chaining)**.

- **Generación de Clave**: La clase `Simetrica` implementa el método `generarClave()` para generar una clave secreta de 512 bits, almacenándola en un fichero.
- **Cifrado y Descifrado**:
  - El método `cifrar()` permite cifrar un archivo utilizando la clave generada.
  - El método `descifrar()` permite recuperar el mensaje en claro a partir del archivo cifrado.

### 2. Criptografía Asimétrica

La criptografía asimétrica implica el uso de un par de claves (pública y privada), lo que permite tanto la **confidencialidad** (cifrado con clave pública y descifrado con clave privada) como la **autenticación** (firma digital).

- **Generación de Claves**: La clase `Asimetrica` implementa el método `generadorParejaClaves()`, que genera un par de claves (pública y privada) y las guarda en ficheros.
- **Cifrado y Descifrado**:
  - Se puede cifrar con la clave pública y descifrar con la privada para asegurar la confidencialidad.
  - Alternativamente, se puede cifrar con la clave privada y descifrar con la pública para la autenticación.
- **Firma y Verificación**:
  - El método `firmar()` permite crear una firma digital sobre un archivo con la clave privada.
  - El método `verificarFirma()` permite verificar la firma utilizando la clave pública del firmante.

### 3. Programa Principal

El fichero `Principal.java` contiene el menú principal que permite al usuario seleccionar entre la criptografía simétrica o asimétrica, y las distintas funcionalidades disponibles (generar clave, cifrar, descifrar, firmar, verificar). Dependiendo de la opción seleccionada, se instancia la clase correspondiente (`Simetrica` o `Asimetrica`) y se ejecutan los métodos indicados.

## Requisitos Técnicos

- **Lenguaje de programación**: Java
- **Entorno de desarrollo**: Eclipse
- **Librerías**: Bouncy Castle versión 1.5
  - [Documentación de Bouncy Castle](https://www.bouncycastle.org/docs/docs1.5on/index.html)

## Cómo Ejecutar el Proyecto

1. **Configuración del Entorno**:
   - Clonar el repositorio y abrir el proyecto en **Eclipse**.
   - Añadir las librerías de **Bouncy Castle** necesarias al proyecto.
  
2. **Ejecución del Programa**:
   - Compilar y ejecutar la clase `Principal`.
   - El programa presentará un menú para seleccionar entre **criptografía simétrica** y **asimétrica**.
   - Dependiendo de la elección, se presentará otro menú para seleccionar las operaciones de **generación de clave**, **cifrado**, **descifrado**, **firma** o **verificación**.


