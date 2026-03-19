# Security Darkbit Library

Librería de utilidades de seguridad para aplicaciones Spring Boot.

## 📋 Descripción

`security-darkbit` es una librería Java que proporciona utilidades comunes de seguridad como:
- Generación de hashes SHA-256
- Generación de tokens seguros
- Validación de emails
- Validación de contraseñas fuertes
- Sanitización de inputs
- Enmascaramiento de datos sensibles

## 🚀 Instalación

### 1. Compilar e instalar la librería localmente

```bash
mvn clean install
```

### 2. Agregar la dependencia en tu proyecto

En el `pom.xml` de tu proyecto Spring Boot, agrega:

```xml
<dependency>
    <groupId>com.darkbit</groupId>
    <artifactId>security-darkbit</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 📖 Uso

### Uso Básico (Sin Spring Boot)

Puedes usar las utilidades directamente sin necesidad de Spring Boot:

```java
import com.darkbit.security.util.SecurityUtil;

public class MiClase {
    public void ejemploDeUso() {
        // Generar hash
        String hash = SecurityUtil.generateHash("miTexto");
        
        // Generar token seguro
        String token = SecurityUtil.generateSecureToken(32);
        
        // Validar email
        boolean esValido = SecurityUtil.isValidEmail("usuario@ejemplo.com");
        
        // Validar contraseña
        boolean esFuerte = SecurityUtil.isStrongPassword("MiPassword123");
        
        // Enmascarar datos sensibles
        String enmascarado = SecurityUtil.maskSensitiveData("1234567890", 2);
    }
}
```

### Uso con Spring Boot

La librería se auto-configura automáticamente. Puedes configurar propiedades en `application.yml`:

```yaml
security:
  darkbit:
    enabled: true
    token-length: 32
```

## 🧪 Ejemplos

### Generar Hash SHA-256

```java
String password = "miContraseña";
String hash = SecurityUtil.generateHash(password);
System.out.println(hash); // Imprime el hash en hexadecimal
```

### Generar Token Seguro

```java
String token = SecurityUtil.generateSecureToken(32);
System.out.println(token); // Token de 32 bytes en Base64
```

### Validar Email

```java
boolean esValido = SecurityUtil.isValidEmail("usuario@ejemplo.com");
if (esValido) {
    System.out.println("Email válido");
}
```

### Validar Contraseña Fuerte

Una contraseña se considera fuerte si:
- Tiene al menos 8 caracteres
- Contiene al menos una mayúscula
- Contiene al menos una minúscula
- Contiene al menos un número

```java
boolean esFuerte = SecurityUtil.isStrongPassword("MiPassword123");
if (esFuerte) {
    System.out.println("Contraseña fuerte");
}
```

### Sanitizar Input

```java
String input = "<script>alert('XSS')</script>";
String limpio = SecurityUtil.sanitizeInput(input);
System.out.println(limpio); // Imprime sin caracteres peligrosos
```

### Enmascarar Datos Sensibles

```java
String numeroTarjeta = "1234567890123456";
String enmascarado = SecurityUtil.maskSensitiveData(numeroTarjeta, 4);
System.out.println(enmascarado); // 1234********3456
```

## 🧪 Ejecutar Tests

```bash
mvn test
```

## 🏗️ Ejecutar Ejemplo de Demostración

```bash
mvn compile exec:java -Dexec.mainClass="com.darkbit.SecurityLibraryExample"
```

## 📦 Publicar en GitHub

### 1. Inicializar repositorio Git

```bash
git init
git add .
git commit -m "Initial commit: Security Darkbit Library v1.0.0"
```

### 2. Crear repositorio en GitHub

1. Ve a https://github.com/new
2. Crea un nuevo repositorio llamado `security-darkbit`
3. NO inicialices con README (ya lo tenemos)

### 3. Subir el código

```bash
git remote add origin https://github.com/TU_USUARIO/security-darkbit.git
git branch -M main
git push -u origin main
```

## 📝 Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- Spring Boot 3.5.x

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Haz fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es un ejemplo y puede ser utilizado libremente.

## ✨ Autor

Darkbit Security Team

## 📚 Notas de Versión

### v1.0.0 (2026-03-09)
- Versión inicial de la librería
- Utilidades básicas de seguridad
- Auto-configuración para Spring Boot
- Tests unitarios completos

