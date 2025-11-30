package es.educastur.oms.modelo;

// Estados de pedido simplificados y coherentes con la cadena logística:
// PENDIENTE: pedido creado por el cliente (pendiente de confirmación)
// CONFIRMADO: pedido confirmado y stock reservado
// EN_PREPARACION: personal preparando el pedido en almacén
// PREPARADO: listo para envío / salida
// EN_TRANSITO: en transporte hacia el cliente
// ENTREGADO: entregado al cliente
// COMPLETADO: proceso finalizado (facturación / cerrado)
// CANCELADO: pedido cancelado
public enum EstadoPedido {
    PENDIENTE,
    CONFIRMADO,
    EN_PREPARACION,
    PREPARADO,
    EN_TRANSITO,
    ENTREGADO,
    COMPLETADO,
    CANCELADO
}
