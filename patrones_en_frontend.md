# Patrones de Diseño — Representación en el Frontend (TransmiApp)

Este documento detalla cómo los **10 patrones de diseño** implementados en el backend con Spring Boot se traducen, consumen y representan en la interfaz de usuario del frontend desarrollado en **Angular**.

---

## Índice de Patrones

1. [Singleton — Configuración Global (`TransmiConfig`)](#1-singleton--configuración-global-transmiconfig)
2. [Factory Method — Tipos de Vehículos (`VehicleFactory`)](#2-factory-method--tipos-de-vehículos-vehiclefactory)
3. [Builder — Estructura de Rutas (`Route.RouteBuilder`)](#3-builder--estructura-de-rutas-routeroutebuilder)
4. [Observer — Reactividad y Novedades (`TransportEventNotifier`)](#4-observer--reactividad-y-novedades-transporteventnotifier)
5. [Strategy — Cálculo de Tarifas (`FareCalculator`)](#5-strategy--cálculo-de-tarifas-farecalculator)
6. [Adapter — Geolocalización y Tiempos (`ExternalMapAdapter`)](#6-adapter--geolocalización-y-tiempos-externalmapadapter)
7. [Decorator — Canales de Alerta (`NotificationService`)](#7-decorator--canales-de-alerta-notificationservice)
8. [Facade — Planificador e Integración (`TransitSystemFacade`)](#8-facade--planificador-e-integración-transitsystemfacade)
9. [Template Method — Reportes Uniformes (`ReportGenerator`)](#9-template-method--reportes-uniformes-reportgenerator)
10. [Command — Operaciones y Deshacer (`TransitCommand`)](#10-command--operaciones-y-deshacer-transitcommand)

---

## 1. Singleton — Configuración Global (`TransmiConfig`)

* **Propósito en el Backend:** Garantiza una única instancia centralizada con las constantes operacionales (tarifa base, moneda, modo mantenimiento).
* **Representación en el Frontend:**
  * **Dónde se ve:** En los indicadores de costo y la consistencia del sistema (ej. tarifas calculadas en base a los **$2.950 COP** de tarifa estándar).
  * **Componentes / Servicios:** `transmi-data.service.ts` y componentes que presenten costos de viaje.
  * **Efecto visual:** Mantiene uniforme el nombre del sistema ("TransMilenio"), la ciudad ("Bogotá") y la divisa ("COP") en toda la interfaz sin inconsistencias.

---

## 2. Factory Method — Tipos de Vehículos (`VehicleFactory`)

* **Propósito en el Backend:** Instanciar diferentes clases concretas de buses (`BusArticulado`, `BusBiarticulado`, `BusAlimentador`, `BusPadron`) ocultando la lógica de inicialización del cliente.
* **Representación en el Frontend:**
  * **Dónde se ve:** 
    1. En el **Dashboard (Inicio)**: La barra de ocupación y los buses activos cambian dinámicamente según la capacidad del bus (`160` para Articulado, `250` para Biarticulado, `40` para Alimentador).
    2. En el **Tracking (Mapa)**: Los iconos de los buses en movimiento y sus detalles reflejan el tipo específico instanciado por la fábrica.
  * **Componentes / Servicios:** [dashboard.component.ts](file:///home/mateo-m/Escritorio/Transmiapp/front_end/src/app/features/dashboard/dashboard.component.ts) y [tracking.component.ts](file:///home/mateo-m/Escritorio/Transmiapp/front_end/src/app/features/tracking/tracking.component.ts).
  * **Efecto visual:** Visualización de diferentes iconos o etiquetas de tipo de vehículo y cálculo exacto de su % de ocupación en tiempo real en las tarjetas de ruta.

---

## 3. Builder — Estructura de Rutas (`Route.RouteBuilder`)

* **Propósito en el Backend:** Construcción paso a paso de objetos complejos de tipo `Route` (estaciones intermedias ordenadas, origen, destino, colores, frecuencias).
* **Representación en el Frontend:**
  * **Dónde se ve:** En las tarjetas del **Dashboard** y en la visualización detallada del mapa. Verás la secuencia exacta de paradas ordenadas (ej. *Portal Norte ➔ Héroes ➔ Calle 72 ➔ Marly ➔ Calle 26 ➔ Ricaurte ➔ Portal Tunal*).
  * **Componentes / Servicios:** [dashboard.component.html](file:///home/mateo-m/Escritorio/Transmiapp/front_end/src/app/features/dashboard/dashboard.component.html) y `transmi-data.service.ts`.
  * **Efecto visual:** Trayectos bien representados con flechas (`A ➔ B ➔ C`), colores de ruta correctos (ej. `#E84155` para troncales rojas) y listas de paradas en el orden exacto definido por el Builder.

---

## 4. Observer — Reactividad y Novedades (`TransportEventNotifier`)

* **Propósito en el Backend:** Notificar automáticamente a los suscriptores (servicios de logs, sistemas de alertas, auditorías) cuando ocurre un evento vial o de transporte.
* **Representación en el Frontend:**
  * **Dónde se ve:** En la sección de **Novedades / Alertas** y en el comportamiento dinámico de los buses en el mapa.
  * **Componentes / Servicios:** [novedades.component.ts](file:///home/mateo-m/Escritorio/Transmiapp/front_end/src/app/features/novedades/novedades.component.ts) e `interval(15000)` en el servicio de datos.
  * **Efecto visual:** 
    * El contador de incidentes activos del header se actualiza automáticamente.
    * Al reportar un incidente vial desde la web, este se propaga al backend y regresa reactivamente como una novedad activa que afecta visualmente el estado de las rutas (ej. cambia de "Activa" a "Demorada").
    * Se aprovechan los **Angular Signals** (`signal`, `computed`) en el front para propagar reactivamente los cambios notificados por el Observer.

---

## 5. Strategy — Cálculo de Tarifas (`FareCalculator`)

* **Propósito en el Backend:** Algoritmos intercambiables para calcular el pasaje final según el perfil de usuario (Normal, Estudiante, Adulto Mayor).
* **Representación en el Frontend:**
  * **Dónde se ve:** En las vistas de planificación de rutas o simulación de tarifas.
  * **Componentes / Servicios:** Endpoint `/api/transmi/fare?userType=ESTUDIANTE` consumido para mostrar cotizaciones de pasajes.
  * **Efecto visual:** El usuario selecciona su tipo de perfil en un menú desplegable en el frontend, y el sistema muestra instantáneamente el cálculo final con el descuento aplicado por la estrategia del backend de manera instantánea y transparente.

---

## 6. Adapter — Geolocalización y Tiempos (`ExternalMapAdapter`)

* **Propósito en el Backend:** Traduce y homologa formatos incompatibles de APIs geográficas externas al formato interno del sistema de geolocalización.
* **Representación en el Frontend:**
  * **Dónde se ve:** En el **Tracking (Mapa en vivo)** y en el indicador de "tiempo próximo bus".
  * **Componentes / Servicios:** [tracking.component.ts](file:///home/mateo-m/Escritorio/Transmiapp/front_end/src/app/features/tracking/tracking.component.ts) y `transmi-data.service.ts`.
  * **Efecto visual:** 
    * Pintar buses en coordenadas exactas de latitud y longitud.
    * Mostrar estimaciones amigables como **"Próximo bus: 3.5 min"** o **"Distancia: 5.2 km"**, en lugar de milisegundos o coordenadas nativas crudas que entrega un servicio de mapas externo.

---

## 7. Decorator — Canales de Alerta (`NotificationService`)

* **Propósito en el Backend:** Adición dinámica de responsabilidades (enviar por SMS, Email o Push) sobre un servicio básico de notificaciones.
* **Representación en el Frontend:**
  * **Dónde se ve:** Al enviar o reportar incidentes desde el formulario de **Novedades**.
  * **Componentes / Servicios:** [novedades.component.html](file:///home/mateo-m/Escritorio/Transmiapp/front_end/src/app/features/novedades/novedades.component.html) y `/api/transmi/notifications`.
  * **Efecto visual:** Checkboxes o interruptores en la UI que permiten al usuario seleccionar a qué canales adicionales enviar las alertas (SMS, Email, etc.), los cuales el backend decorará dinámicamente en tiempo de ejecución.

---

## 8. Facade — Planificador e Integración (`TransitSystemFacade`)

* **Propósito en el Backend:** Simplificar múltiples APIs complejas de subsistemas en una sola fachada unificada y de alto nivel.
* **Representación en el Frontend:**
  * **Dónde se ve:** En la interacción global y la carga unificada del estado de la aplicación.
  * **Componentes / Servicios:** `/api/transmi/trip` (Planificar viaje) y `/api/transmi/status` (Estado del sistema).
  * **Efecto visual:** El frontend cuenta con un código limpio y eficiente. En lugar de hacer 4 llamadas simultáneas para calcular distancias, buscar buses y validar tarifas, el front hace una única consulta a la **Fachada** del backend y esta le retorna toda la solución consolidada.

---

## 9. Template Method — Reportes Uniformes (`ReportGenerator`)

* **Propósito en el Backend:** Define el esqueleto estructural rígido para la creación de reportes (Header ➔ Contenido ➔ Sumario ➔ Footer) delegando la obtención de datos a las subclases de reporte.
* **Representación en el Frontend:**
  * **Dónde se ve:** En la sección administrativa o de analíticas del sistema para descargar/visualizar reportes de pasajeros y flota.
  * **Componentes / Servicios:** Endpoint `/api/transmi/reports/{tipo}`.
  * **Efecto visual:** Visualización de reportes estructurados con formato idéntico de cabecera y pie de página, cambiando únicamente la tabla central de datos y el sumario analítico según el reporte seleccionado.

---

## 10. Command — Operaciones y Deshacer (`TransitCommand`)

* **Propósito en el Backend:** Encapsula solicitudes de operación como objetos, permitiendo registrar un historial de acciones y soportar retrocesos (Deshacer/Undo).
* **Representación en el Frontend:**
  * **Dónde se ve:** En el panel de control del operador (ej. botones de "Asignar Vehículo", "Lanzar Alerta en Estación" y un botón de "Deshacer última acción").
  * **Componentes / Servicios:** Endpoints en `/api/transmi/commands/*`.
  * **Efecto visual:** 
    * Panel con un listado o historial cronológico que muestra las últimas acciones ejecutadas (ej. *"Asignado bus TM1011 a ruta H75"*).
    * Un botón de **"Deshacer (Undo)"** habilitado en la UI que, al presionarse, remueve el bus o la alerta visual de forma dinámica en la pantalla y base de datos.

---

## Resumen de Mapeo: Backend ➔ Frontend

| Patrón | Implementación Backend | Endpoint API | Vista / Componente Frontend | Manifestación Visual en UI |
| :--- | :--- | :--- | :--- | :--- |
| **Singleton** | `TransmiConfig` | `GET /api/transmi/config` | `transmi-data.service.ts` | Consistencia en tarifa base ($2.950 COP), moneda y ciudad. |
| **Factory Method** | `VehicleFactory` | `POST /api/transmi/vehicles` | `dashboard.component.ts` | Tarjetas de bus con capacidad adaptada (160/250/40) y ocupación %. |
| **Builder** | `RouteBuilder` | `POST /api/transmi/routes` | `dashboard.component.html` | Secuencias y trayectos de paradas en el orden correcto (`A ➔ B ➔ C`). |
| **Observer** | `TransportEventNotifier`| `POST /api/transmi/events/arrival` | `novedades.component.ts` | Alertas de incidentes reactivas que cambian el color/estado de rutas. |
| **Strategy** | `FareCalculator` | `GET /api/transmi/fare` | Simulador de tarifas | Cambios instantáneos en costos según perfil (Regular/Estudiante). |
| **Adapter** | `ExternalMapAdapter` | `GET /api/transmi/geo/distance` | `tracking.component.ts` | Buses pintados en mapa GPS y tiempos estimativos de arribo ("3 min"). |
| **Decorator** | `BasicNotification` | `POST /api/transmi/notifications` | Formulario de reportes | Interruptores en pantalla para habilitar canales SMS/Email dinámicos. |
| **Facade** | `TransitSystemFacade` | `GET /api/transmi/trip` | Módulo de planificación | Petición limpia todo en uno para cotizar, medir y trazar rutas. |
| **Template Method**| `ReportGenerator` | `GET /api/transmi/reports/{type}`| Panel de Analíticas | Reportes estandarizados con la misma cabecera y pie de página. |
| **Command** | `AssignVehicleCommand` | `POST /api/transmi/commands/*` | Panel de Control de Tránsito | Historial de acciones ejecutadas y botón interactivo de **Deshacer**. |
