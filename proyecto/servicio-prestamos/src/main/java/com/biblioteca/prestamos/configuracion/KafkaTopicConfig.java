package com.biblioteca.prestamos.configuracion;

import com.biblioteca.prestamos.evento.PrestamoEventoPublicador;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Declara el topic "prestamos-eventos" con una sola
 * particion (suficiente para este volumen: un topic
 * didactico, no un cluster de produccion) para que
 * exista aunque el broker no tenga auto-creacion
 * activada.
 */
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicPrestamosEventos() {
        return TopicBuilder
            .name(PrestamoEventoPublicador.TOPIC)
            .partitions(1)
            .replicas(1)
            .build();
    }
}
