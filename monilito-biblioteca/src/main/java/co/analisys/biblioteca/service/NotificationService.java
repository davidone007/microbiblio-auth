package co.analisys.biblioteca.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void enviarNotificacion(String email, String mensaje) {
        // Lógica para enviar notificación
        System.out.println("Notificación enviada a " + email + ": " + mensaje);
    }
}
