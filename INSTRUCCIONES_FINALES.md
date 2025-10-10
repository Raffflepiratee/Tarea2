# ✅ Instrucciones Finales - Lista de Usuarios

## 🎯 Cambios Realizados

### 1. ✅ **Corregidos errores de tipeo**
- `BiblotecaComunitaria` → `BibliotecaComunitaria` (en Tarea1)
- Corregida la URL del servicio SOAP

### 2. ✅ **Arreglada la conexión SOAP**
- URL corregida: `http://localhost:8080/usuarios?wsdl`
- Ahora se conectará correctamente al backend

### 3. ✅ **Proyecto simplificado**
- Solo existe `usuarios.jsp`
- Muestra la lista de usuarios de tu base de datos

---

## 🚀 Cómo Ejecutar

### **Paso 1: Ejecutar el Backend (Tarea 1)**
```bash
cd Tarea1
mvn exec:java
```
✅ Esto abre la ventana de la aplicación y publica los servicios SOAP

### **Paso 2: Generar el WAR (Tarea 2)**
```bash
cd Tarea2
mvn clean package
```
✅ El WAR se genera en: `Tarea2/target/biblioteca-web.war`

### **Paso 3: Desplegar en Tomcat**
1. Copia `target/biblioteca-web.war` a tu carpeta `webapps` de Tomcat
2. Ejecuta Tomcat con `catalina.bat start` (o `catalina.sh start` en Linux/Mac)

### **Paso 4: Ver los Usuarios**
Abre en tu navegador:
```
http://localhost:8080/biblioteca-web/usuarios.jsp
```

---

## 🎯 **IMPORTANTE:**

### **Ahora Mostrará tus Usuarios Reales**
- ✅ La URL del servicio SOAP está corregida
- ✅ Se conectará automáticamente al backend de Tarea 1
- ✅ Verás los usuarios de tu base de datos (Antony, Abella, Jachon, etc.)

### **Si Sigues Viendo Datos Mock:**
1. Verifica que Tarea 1 esté ejecutándose
2. Verifica que puedas acceder a: `http://localhost:8080/usuarios?wsdl`
3. Reinicia Tomcat después de desplegar el nuevo WAR

---

## 📋 Verificación Rápida

**Para verificar que el backend está funcionando:**
```
http://localhost:8080/usuarios?wsdl
```
Deberías ver el archivo WSDL (código XML).

**Para ver los usuarios:**
```
http://localhost:8080/biblioteca-web/usuarios.jsp
```
Deberías ver la tabla con tus usuarios reales.

---

## ✨ Resumen

1. ✅ Todos los errores de tipeo corregidos
2. ✅ URL del servicio SOAP arreglada
3. ✅ Proyecto simplificado (solo usuarios.jsp)
4. ✅ Listo para mostrar usuarios reales de tu base de datos

**¡Todo está listo para funcionar!** 🚀
