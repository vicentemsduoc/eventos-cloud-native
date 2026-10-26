package com.cloudnative.eventos.repository;

import com.cloudnative.eventos.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    // También puedes agregar métodos personalizados más adelante, por ejemplo:
    // List<Compra> findByEventoId(Long eventoId);
}