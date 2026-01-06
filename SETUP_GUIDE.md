---

# Guía de Configuración Estándar: Kotlin Multiplatform (KMP)

**Fecha:** Enero 2026
**Propósito:** Definir el flujo de trabajo y configuración base para nuevos proyectos móviles multiplataforma en la empresa.

---

## 1. Entorno de Desarrollo Requerido

Antes de iniciar cualquier proyecto KMP, el entorno local debe cumplir con los siguientes requisitos para garantizar la compilación cruzada (Android & iOS).

* **Sistema Operativo:** macOS (Requerido para compilar iOS).
* **JDK:** Version 17 (Eclipse Temurin o Amazon Corretto recomendados).
* **Android Studio:** Versión más reciente (Ladybug o superior).
* *Plugin:* Kotlin Multiplatform Mobile (KMM).


* **Xcode:** Versión 15.0 o superior.
* **Herramienta de Diagnóstico:** `kDoctor`.
* Instalación: `brew install kdoctor`
* Uso: Ejecutar `kdoctor` en la terminal y corregir cualquier error marcado con [x] antes de empezar.



---

## 2. Inicialización del Proyecto

Para mantener la consistencia en la gestión de dependencias y estructura de carpetas, se recomienda utilizar el **JetBrains KMP Wizard**.

**Pasos de generación:**

1. Acceder a [kmp.jetbrains.com](https://kmp.jetbrains.com/).
2. **Project Name:** `[NombreDelProyecto]`.
3. **iOS framework distribution:** Seleccionar **"Direct embedding"**.
* *Nota:* Esto configura automáticamente el proyecto como un **Monorepo**, donde el proyecto de Xcode compila directamente el código de Kotlin sin necesidad de CocoaPods externos.


4. **UI Framework:**
* Para UI 100% compartida: Seleccionar *Compose Multiplatform*.
* Para UI Híbrida (Recomendado): Seleccionar *Do not share UI* (o compartirla pero usar SwiftUI como punto de entrada en iOS).



---

## 3. Estructura de Monorepo

El proyecto debe seguir la siguiente estructura de directorios para facilitar la navegación y el CI/CD:

```text
Raíz del Proyecto
├── composeApp (o shared)      # Módulo Kotlin Multiplatform
│   ├── build.gradle.kts       # Configuración de Build del módulo
│   └── src
│       ├── commonMain         # Lógica de Negocio y Modelos (Puro Kotlin)
│       ├── androidMain        # Implementación específica Android (JVM/Android SDK)
│       └── iosMain            # Implementación específica iOS (Kotlin Native / UIKit)
│
├── iosApp                     # Proyecto Nativo Xcode
│   ├── iosApp.xcodeproj
│   └── ContentView.swift      # UI Nativa (SwiftUI)
│
├── gradle
│   └── libs.versions.toml     # Catálogo de Versiones (Dependencias centralizadas)
└── settings.gradle.kts        # Configuración de módulos del proyecto

```

---

## 4. Configuración del Build (Gradle Kotlin DSL)

Se establece el uso estricto de **Gradle Kotlin DSL (`.kts`)** y **Version Catalogs**.

### 4.1. Definición de Targets (`build.gradle.kts`)

El módulo compartido debe configurar los objetivos de compilación para cubrir la gama completa de dispositivos:

```kotlin
kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    // Configuración para iOS (Simuladores y Dispositivos Reales)
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp" // Nombre del framework visible en Swift
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            // Dependencias comunes (Ktor, Coroutines, Serialization)
        }
    }
}

```

### 4.2. Gestión de Dependencias (`libs.versions.toml`)

Todas las versiones deben declararse en el catálogo para evitar conflictos:

```toml
[versions]
kotlin = "2.1.0"
compose = "1.7.0"
agp = "8.7.0"

[libraries]
androidx-activity-compose = { module = "androidx.activity:activity-compose", version.ref = "androidx-activityCompose" }
kotlin-test = { module = "org.jetbrains.kotlin:kotlin-test", version.ref = "kotlin" }

[plugins]
androidApplication = { id = "com.android.application", version.ref = "agp" }
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }

```

---

## 5. Estrategia de Interoperabilidad

Para comunicar la lógica compartida con las plataformas nativas, se utiliza el patrón `expect`/`actual`.

### 5.1. Desde Kotlin hacia Nativo (commonMain -> iosMain)

Se definen interfaces o funciones esperadas en `commonMain` y se implementan en las carpetas específicas.

* **iOS (iosMain):** Puede acceder directamente a APIs de C y Objective-C (`UIKit`, `Foundation`, `CoreLocation`) usando `import platform.*`.
* **Restricción:** Para uso de punteros o structs de C, se debe usar la anotación `@OptIn(ExperimentalForeignApi::class)`.

### 5.2. Desde Nativo hacia Kotlin (Swift -> commonMain)

El código compartido se compila en un `.framework` que Swift importa.

* **Instanciación:** Las clases de Kotlin se instancian en Swift como clases normales.
* **Flujo:** `View (SwiftUI)` -> `ViewModel/Controller (Kotlin Shared)` -> `Data Layer`.

---

## 6. Configuración de Xcode (Build Phases)

Para asegurar que los cambios en Kotlin se reflejen en iOS al compilar desde Xcode, el proyecto debe tener una **"Run Script Phase"** antes de "Compile Sources":

```bash
cd "$SRCROOT/.."
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode

```

*Este paso suele ser configurado automáticamente por el Wizard, pero es crítico verificarlo si hay errores de "Class not found".*

---