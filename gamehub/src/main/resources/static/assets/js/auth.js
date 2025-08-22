const KEY = 'gamehub.cliente';

export function getCliente() {
  try { return JSON.parse(localStorage.getItem(KEY) || 'null'); } catch { return null; }
}

export function getClienteId() {
  const c = getCliente();
  // Tente idCliente, id, or id_cliente conforme o backend
  return c?.idCliente ?? c?.id ?? c?.id_cliente ?? null;
}

export function getClienteInfo() {
  return getCliente();
}

export function setCliente(cliente) {
  localStorage.setItem(KEY, JSON.stringify(cliente));
  window.dispatchEvent(new CustomEvent('auth:changed'));
}

export function logout() {
  localStorage.removeItem(KEY);
  window.dispatchEvent(new CustomEvent('auth:changed'));
}

export function isAutenticado() {
  return getClienteId() != null;
}
