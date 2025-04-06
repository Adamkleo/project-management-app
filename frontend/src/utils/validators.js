// src/utils/validators.js

export function validateNotBlank(value, fieldName = 'Este campo') {
    return value ? null : `${fieldName} no puede estar vacío.`;
}

export function validateName(value) {
    if (!value) return 'El nombre es obligatorio.';
    if (!/^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$/.test(value)) return 'Solo letras y espacios.';
    if (value.length < 3 || value.length > 40) return 'Debe tener entre 3 y 40 caracteres.';
    return null;
}

export function validateNIF(value) {
    return /^[0-9]{8}[A-HJ-NP-TV-Z]$/.test(value) ? null : 'NIF inválido.';
}

export function validatePhone(value) {
    return /^\+?[0-9]{7,15}$/.test(value) ? null : 'Teléfono inválido.';
}

export function validateEmail(value) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value) ? null : 'Email inválido.';
}

export const validateDateYYYYMMDD = (value) => {
    if (!value) return true; // Allow empty unless combined with validateNotBlank
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    if (!regex.test(value)) {
        return 'Formato debe ser YYYY-MM-DD';
    }
    // Basic date validity check (doesn't check days in month perfectly)
    const parts = value.split('-');
    const year = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10);
    const day = parseInt(parts[2], 10);
    if (year < 1900 || year > 2100 || month < 1 || month > 12 || day < 1 || day > 31) {
        return 'Fecha inválida';
    }
    // Could add more robust date checks here (e.g., using Date object)
    return true;
}