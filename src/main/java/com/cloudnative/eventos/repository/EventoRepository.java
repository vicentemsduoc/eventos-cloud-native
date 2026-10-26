package com.cloudnative.eventos.repository;

import com.cloudnative.eventos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    // Métodos básicos que ya vienen heredados:
    // save(), findById(), findAll(), deleteById(), etc.
}