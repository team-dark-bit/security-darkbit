# 🧪 Crear Proyecto de Prueba

## Opción 1: Crear Proyecto de Prueba Simple

### 1. Crear estructura de directorio
```bash
mkdir test-security-darkbit
cd test-security-darkbit
```

### 2. Crear `pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.ejemplo</groupId>
    <artifactId>test-security-darkbit</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.12-SNAPSHOT</version>
    </parent>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- Tu librería Security Darkbit -->
        <dependency>
            <groupId>com.darkbit</groupId>
            <artifactId>security-darkbit</artifactId>
            <version>1.0.0</version>
        </dependency>

        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <url>https://repo.spring.io/snapshot</url>
            <snapshots><enabled>true</enabled></snapshots>
        </repository>
    </repositories>
</project>
```

### 3. Crear clase principal
Archivo: `src/main/java/com/ejemplo/TestApplication.java`

```java
package com.ejemplo;

import com.darkbit.security.util.SecurityUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
        
        // Prueba rápida en consola
        System.out.println("\n=== Probando Security Darkbit Library ===");
        System.out.println("Hash: " + SecurityUtil.generateHash("test123"));
        System.out.println("Token: " + SecurityUtil.generateSecureToken(16));
        System.out.println("Email válido: " + SecurityUtil.isValidEmail("test@example.com"));
        System.out.println("===========================================\n");
    }

    @GetMapping("/")
    public String home() {
        return "Security Darkbit Library Test - Funcionando! ✅";
    }

    @PostMapping("/api/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        // Validar email
        if (!SecurityUtil.isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }

        // Validar contraseña fuerte
        if (!SecurityUtil.isStrongPassword(request.getPassword())) {
            throw new IllegalArgumentException(
                "La contraseña debe tener al menos 8 caracteres, " +
                "una mayúscula, una minúscula y un número"
            );
        }

        // Generar hash de contraseña
        String passwordHash = SecurityUtil.generateHash(request.getPassword());

        // Generar token de sesión
        String sessionToken = SecurityUtil.generateSecureToken(32);

        return new RegisterResponse(
            "Usuario registrado exitosamente",
            sessionToken,
            passwordHash
        );
    }

    @GetMapping("/api/mask")
    public MaskResponse maskData(@RequestParam String data) {
        String masked = SecurityUtil.maskSensitiveData(data, 3);
        return new MaskResponse(data, masked);
    }

    // DTOs
    record RegisterRequest(String email, String password) {}
    
    record RegisterResponse(String message, String token, String passwordHash) {}
    
    record MaskResponse(String original, String masked) {}
}
```

### 4. Crear `application.yml` (opcional)
Archivo: `src/main/resources/application.yml`

```yaml
server:
  port: 8080

security:
  darkbit:
    enabled: true
    token-length: 32
```

### 5. Ejecutar el proyecto
```bash
mvn spring-boot:run
```

### 6. Probar los endpoints

**Test 1: Página principal**
```bash
curl http://localhost:8080/
```

**Test 2: Registro de usuario**
```bash
curl -X POST http://localhost:8080/api/register ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"usuario@example.com\",\"password\":\"MiPassword123\"}"
```

**Test 3: Enmascarar datos**
```bash
curl "http://localhost:8080/api/mask?data=1234567890123456"
```

---

## Opción 2: Proyecto Spring Initializr

### 1. Generar proyecto
Ve a https://start.spring.io/ y configura:
- Project: Maven
- Language: Java
- Spring Boot: 3.2.x o superior
- Group: com.ejemplo
- Artifact: test-security
- Dependencies: Spring Web

### 2. Descargar y descomprimir

### 3. Agregar tu librería al `pom.xml`
```xml
<dependency>
    <groupId>com.darkbit</groupId>
    <artifactId>security-darkbit</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 4. Usar en tu código
```java
import com.darkbit.security.util.SecurityUtil;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public Map<String, Object> test() {
        return Map.of(
            "hash", SecurityUtil.generateHash("test"),
            "token", SecurityUtil.generateSecureToken(16),
            "emailValid", SecurityUtil.isValidEmail("test@example.com")
        );
    }
}
```

---

## Opción 3: Usar en Proyecto Existente

Si ya tienes un proyecto Spring Boot:

### 1. Agregar dependencia
En tu `pom.xml`:
```xml
<dependency>
    <groupId>com.darkbit</groupId>
    <artifactId>security-darkbit</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Usar directamente
```java
import com.darkbit.security.util.SecurityUtil;

public class MiServicio {
    public void miMetodo() {
        String hash = SecurityUtil.generateHash("datos");
        // ... usar hash
    }
}
```

---

## 🔍 Verificar que la librería está instalada

```bash
ls C:\Users\abelk\.m2\repository\com\darkbit\security-darkbit\1.0.0\
```

Deberías ver:
- `security-darkbit-1.0.0.jar`
- `security-darkbit-1.0.0.pom`

---

## 🎯 Casos de Uso Reales

### Sistema de Autenticación
```java
@Service
public class AuthService {
    
    public User register(String email, String password) {
        if (!SecurityUtil.isValidEmail(email)) {
            throw new BadRequestException("Email inválido");
        }
        
        if (!SecurityUtil.isStrongPassword(password)) {
            throw new BadRequestException("Contraseña débil");
        }
        
        String passwordHash = SecurityUtil.generateHash(password);
        String activationToken = SecurityUtil.generateSecureToken(48);
        
        User user = new User(email, passwordHash, activationToken);
        return userRepository.save(user);
    }
    
    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        
        return SecurityUtil.validateHash(password, user.getPasswordHash());
    }
}
```

### API con Tokens de Sesión
```java
@Service
public class SessionService {
    private final Map<String, UserSession> activeSessions = new ConcurrentHashMap<>();
    
    public String createSession(Long userId) {
        String token = SecurityUtil.generateSecureToken(48);
        activeSessions.put(token, new UserSession(userId, LocalDateTime.now()));
        return token;
    }
    
    public boolean isValidSession(String token) {
        return activeSessions.containsKey(token);
    }
}
```

### Logging Seguro
```java
@Aspect
@Component
public class AuditAspect {
    
    @AfterReturning("@annotation(Auditable)")
    public void logAction(JoinPoint joinPoint) {
        String userEmail = getCurrentUserEmail();
        String maskedEmail = SecurityUtil.maskSensitiveData(userEmail, 2);
        
        log.info("User {} performed action: {}", 
            maskedEmail, 
            joinPoint.getSignature().getName()
        );
    }
}
```

---

## ✅ Checklist de Prueba

- [ ] Compilar proyecto de prueba: `mvn clean compile`
- [ ] Ejecutar proyecto: `mvn spring-boot:run`
- [ ] Probar endpoint de registro
- [ ] Probar validación de email
- [ ] Probar validación de contraseña
- [ ] Probar generación de hash
- [ ] Probar generación de token
- [ ] Probar enmascaramiento de datos

---

## 🐛 Solución de Problemas

### Error: "Cannot resolve com.darkbit:security-darkbit"
**Solución:** Reinstala la librería:
```bash
cd "D:\ABEL\ENTERPRISE\PROYECTO 1\security-darkbit"
.\mvnw.cmd clean install
```

### Error: "Class not found: SecurityUtil"
**Solución:** Verifica el import:
```java
import com.darkbit.security.util.SecurityUtil;
```

### Error: "Method is never used" en el IDE
**Solución:** Es solo un warning. Los métodos están diseñados para ser usados en otros proyectos.

---

¡Listo! Ahora puedes crear un proyecto de prueba y verificar que todo funciona correctamente. 🎉

