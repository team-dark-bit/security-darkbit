# Cómo Usar Esta Librería en Otro Proyecto

## Paso 1: Instalar la librería

Primero, compila e instala esta librería en tu repositorio local de Maven:

```bash
cd security-darkbit
mvnw.cmd clean install
```

## Paso 2: Crear un nuevo proyecto Spring Boot

Crea un nuevo proyecto Spring Boot o usa uno existente.

## Paso 3: Agregar la dependencia

En el `pom.xml` de tu nuevo proyecto, agrega la dependencia:

```xml
<dependency>
    <groupId>com.darkbit</groupId>
    <artifactId>security-darkbit</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Paso 4: Usar la librería

### Ejemplo 1: Uso Simple en Cualquier Clase

```java
package com.ejemplo.miproyecto;

import com.darkbit.security.util.SecurityUtil;

public class MiServicio {
    
    public String registrarUsuario(String email, String password) {
        // Validar email
        if (!SecurityUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        // Validar contraseña fuerte
        if (!SecurityUtil.isStrongPassword(password)) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número");
        }
        
        // Generar hash de la contraseña
        String passwordHash = SecurityUtil.generateHash(password);
        
        // Generar token de activación
        String activationToken = SecurityUtil.generateSecureToken(32);
        
        // Guardar en base de datos...
        return activationToken;
    }
}
```

### Ejemplo 2: Uso en un Controller REST

```java
package com.ejemplo.miproyecto.controller;

import com.darkbit.security.util.SecurityUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest request) {
        // Sanitizar inputs
        String cleanEmail = SecurityUtil.sanitizeInput(request.getEmail());
        
        // Validar
        if (!SecurityUtil.isValidEmail(cleanEmail)) {
            throw new BadRequestException("Email inválido");
        }
        
        if (!SecurityUtil.isStrongPassword(request.getPassword())) {
            throw new BadRequestException("Contraseña débil");
        }
        
        // Generar hash y token
        String passwordHash = SecurityUtil.generateHash(request.getPassword());
        String token = SecurityUtil.generateSecureToken(32);
        
        // Retornar respuesta
        return Map.of(
            "message", "Usuario registrado",
            "token", token
        );
    }
    
    @GetMapping("/user/{userId}/card")
    public Map<String, String> getUserCard(@PathVariable String userId) {
        // Supongamos que obtenemos el número de tarjeta de la BD
        String cardNumber = "1234567890123456";
        
        // Enmascarar datos sensibles antes de devolverlos
        String maskedCard = SecurityUtil.maskSensitiveData(cardNumber, 4);
        
        return Map.of("cardNumber", maskedCard);
    }
}
```

### Ejemplo 3: Configuración Personalizada (Opcional)

En tu `application.yml`:

```yaml
security:
  darkbit:
    enabled: true
    token-length: 32
```

### Ejemplo 4: Inyectar Propiedades de Configuración

```java
package com.ejemplo.miproyecto.service;

import com.darkbit.security.config.SecurityConfigProperties;
import com.darkbit.security.util.SecurityUtil;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    
    private final SecurityConfigProperties config;
    
    public TokenService(SecurityConfigProperties config) {
        this.config = config;
    }
    
    public String generateToken() {
        if (!config.isEnabled()) {
            throw new IllegalStateException("Security features are disabled");
        }
        
        return SecurityUtil.generateSecureToken(config.getTokenLength());
    }
}
```

## Paso 5: Ejecutar tu proyecto

```bash
mvn spring-boot:run
```

## Funciones Disponibles

| Función | Descripción | Ejemplo |
|---------|-------------|---------|
| `generateHash(String)` | Genera hash SHA-256 | `SecurityUtil.generateHash("password")` |
| `generateSecureToken(int)` | Genera token seguro | `SecurityUtil.generateSecureToken(32)` |
| `validateHash(String, String)` | Valida hash | `SecurityUtil.validateHash("text", hash)` |
| `sanitizeInput(String)` | Limpia input peligroso | `SecurityUtil.sanitizeInput(userInput)` |
| `isValidEmail(String)` | Valida formato email | `SecurityUtil.isValidEmail(email)` |
| `isStrongPassword(String)` | Valida contraseña fuerte | `SecurityUtil.isStrongPassword(pass)` |
| `maskSensitiveData(String, int)` | Enmascara datos | `SecurityUtil.maskSensitiveData(card, 4)` |

## Casos de Uso Comunes

### 1. Sistema de Login

```java
public class LoginService {
    public boolean login(String email, String password) {
        // Obtener hash almacenado de la BD
        String storedHash = userRepository.findPasswordHashByEmail(email);
        
        // Validar contraseña
        return SecurityUtil.validateHash(password, storedHash);
    }
}
```

### 2. API con Tokens de Sesión

```java
@Service
public class SessionService {
    private Map<String, String> sessions = new ConcurrentHashMap<>();
    
    public String createSession(String userId) {
        String token = SecurityUtil.generateSecureToken(48);
        sessions.put(token, userId);
        return token;
    }
}
```

### 3. Protección de Datos Sensibles en Logs

```java
@Service
public class AuditService {
    public void logUserAction(String action, String email) {
        String maskedEmail = SecurityUtil.maskSensitiveData(email, 2);
        logger.info("User {} performed action: {}", maskedEmail, action);
    }
}
```

## Notas Importantes

1. **La librería es thread-safe**: Todos los métodos son estáticos y pueden usarse de forma concurrente.
2. **No requiere configuración**: Funciona out-of-the-box, pero puedes personalizarla si lo deseas.
3. **Spring Boot Auto-Configuration**: Si usas Spring Boot, la configuración se carga automáticamente.
4. **Sin Spring Boot**: También puedes usar las utilidades en proyectos Java simples sin Spring.

## Publicar en GitHub y GitHub Packages

Para que otros puedan usar tu librería directamente desde GitHub:

### 1. Inicializar Git

```bash
git init
git add .
git commit -m "Initial commit: Security Darkbit Library v1.0.0"
```

### 2. Crear repositorio en GitHub y subir

```bash
git remote add origin https://github.com/TU_USUARIO/security-darkbit.git
git branch -M main
git push -u origin main
```

### 3. Publicar en GitHub Packages (Opcional)

Agrega esto a tu `pom.xml`:

```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/TU_USUARIO/security-darkbit</url>
    </repository>
</distributionManagement>
```

Luego ejecuta:

```bash
mvn deploy
```

Otros desarrolladores podrán usar tu librería agregando el repositorio en su `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/TU_USUARIO/security-darkbit</url>
    </repository>
</repositories>
```

