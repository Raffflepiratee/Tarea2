# 🌐 Biblioteca Comunitaria Lectores.uy - Tarea 2
Proyecto desarrollado como parte de la **Tarea 2** del curso *Programación de Aplicaciones* de la **Universidad de la República - DGETP**.
Esta segunda entrega amplía la primera versión de escritorio hacia una aplicación web.

## 🗓️ Información Administrativa
- **Inicio:** 15 de Septiembre
- **Entrega final:** 26 de Octubre, hasta las 23:59hs

---

## 🎯 Objetivos de Aprendizaje
- Conceptos generales sobre desarrollo web en Java con Servlets y JSP
- Implementación de Web Services
- Creación de sitios web Responsive

---

## 🧩 Descripción del Sistema
La Biblioteca Comunitaria evoluciona hacia una aplicación web que complementa la interfaz de escritorio. Este MVP incluye tres componentes principales:

### 👥 Usuarios
- **Lectores**: pueden autenticarse (login), visualizar perfil, consultar préstamos activos, solicitar materiales y verificar estados
- **Bibliotecarios**: pueden autenticarse, modificar estado de lectores (`ACTIVO` o `SUSPENDIDO`), cambiar zona de lectores, supervisar préstamos
- **Se debe implementar con**: Servlets y JSP para login y gestión de perfiles, persistencia con Hibernate y trazabilidad vía Web Services.

### 📦 Materiales
- **Libros**: título y cantidad de páginas
- **Artículos Especiales**: descripción, peso y dimensiones
- **Funcionalidades**: Registrar donaciones, consultar inventario completo y filtrar por rango de fechas
- **Se debe implementar con**: formularios JSP para registro, Servlets para lógica de negocio y persistencia con Hibernate.

### 🔄 Préstamos
- **Ciclo completo**: creación, actualización de estado (`PENDIENTE`, `EN CURSO`, `DEVUELTO`), agrupación por estado y devolución
- **Lectores**: crear préstamos, ver historial agrupado por estado
- **Bibliotecarios**: actualizar estados, controlar cumplimiento de devoluciones
- **Se debe implementar con**: Servlets para gestión de préstamos, JSP para visualización y Web Services para trazabilidad.

## 📖 Historias de Usuario
### 1. Gestión de Usuarios
- Login para lectores y bibliotecarios
- Modificar estado de lectores (ACTIVO / SUSPENDIDO)
- Cambiar zona de lectores

### 2. Gestión de Materiales
- Registrar libros y artículos especiales
- Consultar donaciones (lectores y bibliotecarios)
- Consulta opcional por rango de fechas

### 3. Gestión de Préstamos
- Creación de préstamos por lectores
- Actualización de estado por bibliotecarios (EN CURSO, DEVUELTO)
- Visualización de préstamos agrupados por estado
- Edición de préstamos y listado de activos

### 4. Control y Seguimiento (Opcional)
- Historial de préstamos por bibliotecario
- Reportes por zona
- Identificación de materiales con muchos préstamos pendientes

## 📱 Sitio Web Responsive
El sistema está diseñado para ser accesible desde dispositivos móviles, garantizando una experiencia ágil y confiable para la comunidad.
