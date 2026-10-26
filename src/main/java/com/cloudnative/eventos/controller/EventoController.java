package com.cloudnative.eventos.controller;

import com.cloudnative.eventos.dto.EventoRequest;
import com.cloudnative.eventos.dto.EventoResponse;
import com.cloudnative.eventos.model.Evento;
import com.cloudnative.eventos.repository.EventoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoRepository eventoRepository;

    // GET /eventos → lista todos los eventos
    @GetMapping
    public List<EventoResponse> listarEventos() {
        return eventoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // POST /eventos → crea un nuevo evento
    @PostMapping
    public ResponseEntity<EventoResponse> crearEvento(@Valid @RequestBody EventoRequest request) {
        Evento evento = new Evento();
        evento.setNombre(request.getNombre());
        evento.setFecha(request.getFecha());
        evento.setPrecio(request.getPrecio());

        Evento guardado = eventoRepository.save(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(guardado));
    }

    // Método auxiliar para convertir Entidad → DTO
    private EventoResponse toResponse(Evento evento) {
        EventoResponse response = new EventoResponse();
        response.setId(evento.getId());
        response.setNombre(evento.getNombre());
        response.setFecha(evento.getFecha());
        response.setPrecio(evento.getPrecio());
        return response;
    }
}