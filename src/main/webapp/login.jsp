<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Biblioteca Comunitaria</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/login.css" rel="stylesheet">
</head>
<body>
    <header class="site-header">
        <div class="container header-content">
            <div class="brand">Biblioteca Comunitaria UY</div>
        </div>
    </header>

    <main class="login-wrapper">
        <section class="login-card" role="region" aria-label="Formulario de inicio de sesión">
            <h2 class="card-title">Login</h2>

            <form id="loginForm" class="login-form" novalidate>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input id="email" name="email" type="email" class="form-control" placeholder="tu@ejemplo.com" required>
                </div>

                <div class="mb-4">
                    <label for="password" class="form-label">Contraseña</label>
                    <input id="password" name="password" type="password" class="form-control" placeholder="••••••••" required>
                </div>

                <div class="d-flex justify-content-center">
                    <button type="submit" class="btn btn-login">Iniciar Sesión</button>
                </div>
            </form>
        </section>
    </main>

    <footer class="page-footer">
        <div class="container text-center small">© Biblioteca Comunitaria</div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="js/login.js"></script>
</body>
</html>