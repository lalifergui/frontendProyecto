# 📱 BiblioSwipe - Frontend (Android)

Este repositorio contiene el código frontend de **BiblioSwipe**, una aplicación móvil desarrollada para Android.

El objetivo principal del proyecto es crear una plataforma de **"match" entre lectores** que permite a los usuarios conectar, descubrir nuevos libros y compartir intereses literarios. Esta versión frontend está centrada en el diseño de las ventanas, la navegación y la lógica de la interfaz de usuario.

---

## 🛠️ Tecnologías Principales

El frontend está construido con el siguiente stack tecnológico:

* **Lenguaje:** Kotlin
* **Plataforma:** Android (Nativo)
* **IDE de Desarrollo:** Android Studio
* **Gestor de Dependencias:** Gradle (Kotlin DSL - `build.gradle.kts`)

## 🚀 Inicio Rápido

Sigue estos pasos para abrir el proyecto y ejecutar la aplicación en un emulador o dispositivo físico.

### 📋 Prerrequisitos

Necesitas tener instalado en tu sistema:

* **Android Studio** (versión reciente recomendada).
* **Android SDK** (Configurado para la API de Android más reciente).

### ⚙️ Instalación

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/frontendProyecto.git](https://github.com/tu-usuario/frontendProyecto.git)
    cd frontendProyecto
    ```

2.  **Abrir el proyecto:**
    Abre la carpeta `frontendProyecto` directamente desde **Android Studio** (usando la opción "Open an existing Android Studio project").

3.  **Sincronizar Gradle:**
    Android Studio debería sincronizar Gradle automáticamente. Si no lo hace, haz clic en el botón de sincronización (icono del elefante) para descargar todas las dependencias.

### ▶️ Ejecución

Para ejecutar la aplicación:

1.  Asegúrate de tener un **Emulador de Android** (AVD) configurado o un dispositivo físico conectado.
2.  Selecciona el dispositivo de destino en la barra de herramientas de Android Studio.
3.  Haz clic en el botón **Run 'app'** (el icono de triángulo verde) para construir e instalar la aplicación.

---

## 📂 Estructura del Proyecto

El código fuente principal se encuentra en la carpeta `app`, siguiendo la estructura estándar de un proyecto Android.

| Carpeta/Archivo | Propósito |
| :--- | :--- |
| `app/` | Contiene el código fuente de la aplicación (`src/main/kotlin`) y los recursos (`res/`). |
| `app/src/main/res/layout` | Archivos XML con el diseño de las ventanas y componentes de la interfaz. |
| `gradle/` | Archivos auxiliares del envoltorio de Gradle (Gradle Wrapper). |
| `build.gradle.kts` | Archivo principal de configuración de dependencias y módulos del proyecto. |
| `.gitignore` | Lista de archivos que Git debe ignorar (caché, archivos de compilación, etc.). |

---

## 🧑‍💻 Autor

Este proyecto está siendo desarrollado por:

* **Laura Fernández Guirao**


---
**Nota:** Este repositorio es la parte frontend del proyecto. La funcionalidad completa depende de la conexión con el repositorio de backend correspondiente.
