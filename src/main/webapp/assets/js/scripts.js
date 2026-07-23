/* ============================================
   CLICK & DRIVE - SCRIPTS PERSONALIZADOS
   ============================================ */

/**
 * Toggle Password Visibility
 * Muestra u oculta la contraseña en un campo de entrada
 *
 * @param {string} inputId - ID del campo de entrada
 * @param {string} iconId - ID del ícono para toggle
 */
function togglePassword(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);

    if (!input || !icon) return;

    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.replace('fa-eye', 'fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.replace('fa-eye-slash', 'fa-eye');
    }
}

/**
 * Validate Password Strength
 * Valida los requisitos de la contraseña en tiempo real
 *
 * @param {string} passwordId - ID del campo de contraseña
 * @param {string} requirementsIds - IDs de los requisitos (array)
 */
function validatePassword(passwordId, requirementsIds) {
    const input = document.getElementById(passwordId);
    if (!input) return;

    input.addEventListener('input', function() {
        const p = this.value;

        requirementsIds.forEach(function(item) {
            const element = document.getElementById(item.id);
            if (!element) return;

            const isValid = item.test(p);
            const icon = element.querySelector('i');

            if (isValid) {
                element.style.color = '#16a34a';
                if (icon) {
                    icon.className = 'fas fa-check-circle';
                }
            } else {
                element.style.color = '#6b7280';
                if (icon) {
                    icon.className = 'fas fa-circle';
                }
            }
        });
    });
}

/**
 * Validación de requisitos de contraseña
 * Uso: validatePassword('contrasena', [
 *     { id: 'reqLength', test: function(p) { return p.length >= 8; } },
 *     { id: 'reqUpper', test: function(p) { return /[A-Z]/.test(p); } },
 *     ...
 * ]);
 */

/**
 * Validar Confirmación de Contraseña
 *
 * @param {string} passwordId - ID del campo de contraseña
 * @param {string} confirmId - ID del campo de confirmación
 */
function validateConfirmPassword(passwordId, confirmId) {
    const password = document.getElementById(passwordId);
    const confirm = document.getElementById(confirmId);

    if (!password || !confirm) return;

    confirm.addEventListener('input', function() {
        if (this.value !== password.value) {
            this.style.borderColor = '#dc2626';
            this.style.boxShadow = '0 0 0 4px rgba(220, 38, 38, 0.1)';
        } else {
            this.style.borderColor = '#16a34a';
            this.style.boxShadow = '0 0 0 4px rgba(22, 163, 74, 0.1)';
        }
    });
}

/**
 * Validar Formulario de Registro antes de enviar
 *
 * @param {string} formId - ID del formulario
 * @param {string} passwordId - ID del campo de contraseña
 * @param {string} confirmId - ID del campo de confirmación
 * @param {number} minLength - Longitud mínima de contraseña
 */
function validateRegistrationForm(formId, passwordId, confirmId, minLength) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function(e) {
        const password = document.getElementById(passwordId);
        const confirm = document.getElementById(confirmId);

        if (!password || !confirm) return;

        const p = password.value;
        const c = confirm.value;

        if (p !== c) {
            e.preventDefault();
            alert('Las contraseñas no coinciden.');
            confirm.focus();
            return false;
        }

        if (p.length < minLength) {
            e.preventDefault();
            alert('La contraseña debe tener al menos ' + minLength + ' caracteres.');
            password.focus();
            return false;
        }
    });
}

/**
 * Cerrar Alertas Automáticamente
 * Cierra las alertas después de 5 segundos
 */
function autoCloseAlerts() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function() {
                alert.style.display = 'none';
            }, 500);
        }, 5000);
    });
}

/**
 * Buscar en lista de elementos
 * Filtra elementos basado en el texto de búsqueda
 *
 * @param {string} inputId - ID del campo de búsqueda
 * @param {string} containerId - ID del contenedor de elementos
 * @param {string} itemSelector - Selector de elementos a filtrar
 * @param {string} searchTextSelector - Selector del texto a buscar dentro del elemento
 */
function searchItems(inputId, containerId, itemSelector, searchTextSelector) {
    const input = document.getElementById(inputId);
    if (!input) return;

    input.addEventListener('keyup', function() {
        const filter = this.value.toLowerCase().trim();
        const container = document.getElementById(containerId);
        if (!container) return;

        const items = container.querySelectorAll(itemSelector);

        items.forEach(function(item) {
            const textElement = item.querySelector(searchTextSelector);
            if (!textElement) return;

            const text = textElement.textContent.toLowerCase();
            if (text.indexOf(filter) > -1) {
                item.style.display = '';
            } else {
                item.style.display = 'none';
            }
        });
    });
}

/**
 * Formatear Moneda (MXN)
 *
 * @param {number} amount - Cantidad a formatear
 * @returns {string} - Cantidad formateada
 */
function formatCurrency(amount) {
    return '$' + amount.toLocaleString('es-MX', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

/**
 * Formatear Fecha
 *
 * @param {string|Date} date - Fecha a formatear
 * @param {string} format - Formato deseado (ej. 'dd/mm/yyyy')
 * @returns {string} - Fecha formateada
 */
function formatDate(date, format) {
    const d = new Date(date);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    const hours = String(d.getHours()).padStart(2, '0');
    const minutes = String(d.getMinutes()).padStart(2, '0');

    switch(format) {
        case 'dd/mm/yyyy':
            return day + '/' + month + '/' + year;
        case 'mm/dd/yyyy':
            return month + '/' + day + '/' + year;
        case 'dd-mm-yyyy':
            return day + '-' + month + '-' + year;
        case 'yyyy-mm-dd':
            return year + '-' + month + '-' + day;
        case 'dd/mm/yyyy hh:mm':
            return day + '/' + month + '/' + year + ' ' + hours + ':' + minutes;
        default:
            return day + '/' + month + '/' + year;
    }
}

/**
 * Obtener Parámetros de URL
 *
 * @returns {object} - Objeto con los parámetros de la URL
 */
function getUrlParams() {
    const params = {};
    const queryString = window.location.search.substring(1);
    const pairs = queryString.split('&');

    pairs.forEach(function(pair) {
        if (pair === '') return;
        const parts = pair.split('=');
        const key = decodeURIComponent(parts[0]);
        const value = decodeURIComponent(parts[1] || '');
        params[key] = value;
    });

    return params;
}

/**
 * Mostrar Notificación Toast
 *
 * @param {string} message - Mensaje a mostrar
 * @param {string} type - Tipo de notificación (success, error, warning, info)
 * @param {number} duration - Duración en milisegundos
 */
function showToast(message, type, duration) {
    // Crear contenedor si no existe
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.style.position = 'fixed';
        container.style.bottom = '20px';
        container.style.right = '20px';
        container.style.zIndex = '9999';
        container.style.maxWidth = '350px';
        document.body.appendChild(container);
    }

    // Crear toast
    const toast = document.createElement('div');
    toast.className = 'alert alert-' + type + ' alert-dismissible fade show';
    toast.style.borderRadius = '12px';
    toast.style.boxShadow = '0 4px 20px rgba(0,0,0,0.15)';
    toast.style.marginTop = '10px';
    toast.style.animation = 'slideInRight 0.3s ease';

    let icon = 'fa-info-circle';
    if (type === 'success') icon = 'fa-check-circle';
    if (type === 'error') icon = 'fa-exclamation-circle';
    if (type === 'warning') icon = 'fa-exclamation-triangle';

    toast.innerHTML = `
        <i class="fas ${icon} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%);"></button>
    `;

    container.appendChild(toast);

    // Auto-cerrar después de la duración
    const durationMs = duration || 5000;
    setTimeout(function() {
        toast.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100px)';
        setTimeout(function() {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 500);
    }, durationMs);
}

/**
 * Cargar Script después de que el DOM esté listo
 */
document.addEventListener('DOMContentLoaded', function() {
    // Auto-cerrar alertas
    autoCloseAlerts();

    // Inicializar validación de contraseña en registro
    const passwordInput = document.getElementById('contrasena');
    const confirmInput = document.getElementById('confirmarContrasena');

    if (passwordInput && confirmInput) {
        // Validación de requisitos
        const reqIds = [
            { id: 'reqLength', test: function(p) { return p.length >= 8; } },
            { id: 'reqUpper', test: function(p) { return /[A-Z]/.test(p); } },
            { id: 'reqLower', test: function(p) { return /[a-z]/.test(p); } },
            { id: 'reqNumber', test: function(p) { return /[0-9]/.test(p); } }
        ];

        passwordInput.addEventListener('input', function() {
            const p = this.value;
            reqIds.forEach(function(item) {
                const element = document.getElementById(item.id);
                if (element) {
                    const icon = element.querySelector('i');
                    if (item.test(p)) {
                        element.style.color = '#16a34a';
                        if (icon) icon.className = 'fas fa-check-circle';
                    } else {
                        element.style.color = '#6b7280';
                        if (icon) icon.className = 'fas fa-circle';
                    }
                }
            });
        });

        // Validación de confirmación
        confirmInput.addEventListener('input', function() {
            if (this.value !== passwordInput.value) {
                this.style.borderColor = '#dc2626';
                this.style.boxShadow = '0 0 0 4px rgba(220, 38, 38, 0.1)';
            } else {
                this.style.borderColor = '#16a34a';
                this.style.boxShadow = '0 0 0 4px rgba(22, 163, 74, 0.1)';
            }
        });
    }

    // Validación de formulario de registro
    const registroForm = document.getElementById('registroForm');
    if (registroForm) {
        registroForm.addEventListener('submit', function(e) {
            const password = document.getElementById('contrasena');
            const confirm = document.getElementById('confirmarContrasena');

            if (!password || !confirm) return;

            if (password.value !== confirm.value) {
                e.preventDefault();
                alert('Las contraseñas no coinciden.');
                confirm.focus();
                return false;
            }

            if (password.value.length < 8) {
                e.preventDefault();
                alert('La contraseña debe tener al menos 8 caracteres.');
                password.focus();
                return false;
            }
        });
    }
});

/**
 * Estilos de animación para toasts
 */
const style = document.createElement('style');
style.textContent = `
    @keyframes slideInRight {
        from {
            transform: translateX(100px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
`;
document.head.appendChild(style);

/**
 * Función para redireccionar con mensaje
 *
 * @param {string} url - URL de destino
 * @param {string} message - Mensaje a mostrar
 * @param {string} type - Tipo de mensaje
 */
function redirectWithMessage(url, message, type) {
    // Guardar mensaje en sessionStorage
    sessionStorage.setItem('toastMessage', message);
    sessionStorage.setItem('toastType', type);
    window.location.href = url;
}

/**
 * Mostrar mensaje guardado en sessionStorage
 */
function showStoredMessage() {
    const message = sessionStorage.getItem('toastMessage');
    const type = sessionStorage.getItem('toastType');
    if (message && type) {
        showToast(message, type);
        sessionStorage.removeItem('toastMessage');
        sessionStorage.removeItem('toastType');
    }
}

// Mostrar mensajes almacenados al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    showStoredMessage();
});