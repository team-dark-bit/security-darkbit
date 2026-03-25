# Security Darkbit Library

Librería de utilidades de seguridad para aplicaciones Spring Boot.

---

## Publicación en JitPack

Esta librería está publicada en [JitPack](https://jitpack.io), lo que permite consumirla fácilmente desde cualquier proyecto Maven o Gradle.

### Configuración del proyecto

Se ha creado el archivo `jitpack.yml` en la raíz del proyecto con el siguiente contenido:

```yaml
jdk:
  - openjdk21
```

---

## Cómo publicar una nueva versión

JitPack trabaja en base a **tags de Git** para generar las versiones de la librería. Cada vez que se sube un nuevo tag, JitPack generará automáticamente una nueva versión disponible para su uso.

Para publicar una nueva versión, ejecuta:

```bash
git tag v1.0.0
git push origin v1.0.0
```

> ⚠️ JitPack **no genera la versión de forma inmediata**; la construye la primera vez que es consumida por un proyecto.

---

## Cómo consumir la librería

### 1. Agregar el repositorio de JitPack

En el `pom.xml` del proyecto que consume la librería, agrega:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### 2. Agregar la dependencia

```xml
<dependency>
    <groupId>com.github.team-dark-bit</groupId>
    <artifactId>security-darkbit</artifactId>
    <version>v1.0.0</version>
</dependency>
```

### 3. Actualizar la versión

Cada vez que exista un nuevo tag en el repositorio, actualiza el campo `<version>` en el `pom.xml` del proyecto consumidor para usar las nuevas funcionalidades o correcciones incluidas en esa versión.
