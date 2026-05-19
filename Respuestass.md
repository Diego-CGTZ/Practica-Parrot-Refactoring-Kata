# Ficha de Análisis y Retrospectiva — Parrot Refactoring Kata

## 1. CODE SMELLS DETECTADOS:
- [x] Switch Statement / Conditional by Type
- [x] Campos en la clase base que solo usa una subclase
- [x] Enum que modela tipos de objetos (señal de que debería haber subclases)
- [x] "Unreachable" exception como muleta del switch *(En el caso default o no explícito)*

## 2. ATRIBUTOS POR TIPO DE LORO:
| TIPO | Usa `numberOfCoconuts` | Usa `voltage` | Usa `isNailed` |
| --- | --- | --- | --- |
| **EUROPEAN** | No | No | No |
| **AFRICAN** | Sí | No | No |
| **NORWEGIAN_BLUE** | No | Sí | Sí |

## 3. COMPORTAMIENTO DE getSpeed() POR TIPO:
- **EUROPEAN**: speed = `getBaseSpeed()` (12.0)
- **AFRICAN**: speed = `Math.max(0, getBaseSpeed() - getLoadFactor() * numberOfCoconuts)`
- **NORWEGIAN_BLUE**: speed = `0` (si nailed) / `Math.min(24.0, voltage * getBaseSpeed())` (si libre)

## 4. COMPORTAMIENTO DE getCry() POR TIPO:
- **EUROPEAN**: cry = `"Sqoork!"`
- **AFRICAN**: cry = `"Sqaark!"`
- **NORWEGIAN_BLUE**: cry = `"Bzzzzzz"` (si voltage > 0) / `"..."` (si no)

## 5. PREGUNTA DE ORO
**¿Qué pasa con `numberOfCoconuts`, `voltage` e `isNailed` cuando los mueves a las subclases que los necesitan? ¿Pueden eliminarse del constructor de la clase base?**
> **Respuesta:** Sí, desaparecen de la clase base por completo. La clase base `Parrot` ya no necesita pedir esos parámetros en su constructor porque no los utiliza para nada. Cada subclase concreta se encarga de declarar y pedir en su constructor únicamente los datos que necesita, logrando así que las variables tengan la cohesión correcta.

---

## 6. MÉTRICAS DE ÉXITO Y MEJORA DEL DISEÑO

| Métrica | Antes | Después | Cumple Objetivo |
| --- | --- | --- | --- |
| `switch` en clase genérica `Parrot` | 2 | **0** | ✅ Sí |
| `case` por tipo de loro en `Parrot` | 6 | **0** | ✅ Sí |
| Campos solo usados por una subclase en el padre | 3 (`numberOfCoconuts`, `voltage`, `isNailed`) | **0** | ✅ Sí |
| Clases concretas por tipo | 0 | **3** | ✅ Sí |
| Para añadir un 4° loro hay que modificar | `Parrot.java` (2 métodos) | **0 clases existentes** | ✅ Sí |

---

## 7. RETROSPECTIVA Y COMPARATIVA (Solución C# / Java de Emily Bache)

**¿Tomaste el mismo orden de extracción de subclases?**
Sí, seguimos el orden que va de menor a mayor complejidad (técnica del surfista): Primero el loro europeo (sin variables extras), luego el africano (1 variable extra) y finalmente el azul noruego (2 variables extras).

**¿Hiciste `Parrot` abstracta antes o después de mover la lógica?**
Después. Primero extrajimos las subclases, comprobamos que los tests pasaban (en este caso el flujo de instanciación original), y una vez que la lógica de negocio estaba delegada en las hijas, procedimos a volver la clase padre abstracta y eliminar el switch original.

**¿Eliminaste `ParrotTypeEnum`? ¿Emily lo hace?**
Sí, se eliminó por completo. Emily Bache también menciona o lo muestra como el desenlace lógico del proceso de polimorfismo, ya que si dependemos de la instanciación de clases concretas, el Enum pierde su propósito como bandera de control de flujo.

**¿Cuántos commits tiene tu historial? ¿Son más o menos granulares que ella?**
El historial tiene 8 commits granulares. Tienen un nivel de granularidad muy similar, aplicando cambios en pasos muy atómicos: creación de la clase y extracción → test, actualización del test → commit. 

**¿Hubo algún test que se pusiera en rojo inesperadamente? ¿Qué reveló ese fallo?**
Durante el proceso tuvimos un detalle con la versión de JUnit en Gradle que no existía, impidiendo que corrieran los tests al inicio. Tuvimos que arreglar el `build.gradle` y actualizar la versión. Posteriormente, los tests pasaron sin problema en cada micro-paso.

**¿Podrías añadir ahora un cuarto tipo de loro (`DeadParrot`) en menos de 5 minutos?**
Sí. Gracias al nuevo diseño polimórfico, bastaría con crear un archivo nuevo `DeadParrot.java` que extienda de `Parrot` y devuelva velocidad 0 y ningún grito, sin necesidad de modificar el código de los otros loros ni de la clase base (Open/Closed Principle cumplido a la perfección).

---

## 8. EVIDENCIAS Y CAPTURAS DEL CÓDIGO FINAL

*Sustituye los siguientes espacios por las capturas de pantalla de tu IDE:*

### 8.1 Clase Base Abstracta (Parrot.java)
*(Coloca aquí la captura de la clase Parrot sin condicionales)*
![Captura Parrot.java]()

### 8.2 Subclase EuropeanParrot
*(Coloca aquí la captura)*
![Captura EuropeanParrot.java]()

### 8.3 Subclase AfricanParrot (con su constante)
*(Coloca aquí la captura)*
![Captura AfricanParrot.java]()

### 8.4 Subclase NorwegianBlueParrot (con su constante)
*(Coloca aquí la captura)*
![Captura NorwegianBlueParrot.java]()

### 8.5 Tests actualizados pasando en verde
*(Coloca aquí la captura de ParrotTest.java instanciando las subclases y la consola con los tests en verde)*
![Captura Tests y Consola verde]()

### 8.6 Historial de Commits (Git Log)
*(Coloca aquí la captura de la terminal mostrando tu historial de commits granulares)*
![Captura Git Log]()
