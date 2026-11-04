package com.cloudnative.eventos.service;

import com.cloudnative.eventos.dto.EventoRequest;
import com.cloudnative.eventos.dto.EventoResponse;
import com.cloudnative.eventos.model.Evento;
import com.cloudnative.eventos.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public List<EventoResponse> listarEventos() {
        return eventoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EventoResponse crearEvento(EventoRequest request) {
        Evento evento = new Evento();
        evento.setNombre(request.getNombre());
        evento.setFecha(request.getFecha());
        evento.setPrecio(request.getPrecio());

        Evento guardado = eventoRepository.save(evento);
        return toResponse(guardado);
    }

    private EventoResponse toResponse(Evento evento) {
        EventoResponse response = new EventoResponse();
        response.setId(evento.getId());
        response.setNombre(evento.getNombre());
        response.setFecha(evento.getFecha());
        response.setPrecio(evento.getPrecio());
        return response;
    }
}