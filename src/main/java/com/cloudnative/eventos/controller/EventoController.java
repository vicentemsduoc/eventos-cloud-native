package com.cloudnative.eventos.controller;

import com.cloudnative.eventos.dto.EventoRequest;
import com.cloudnative.eventos.dto.EventoResponse;
import com.cloudnative.eventos.service.EventoService;
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

    private final EventoService eventoService;

    @GetMapping
    public List<EventoResponse> listarEventos() {
        return eventoService.listarEventos();
    }

    @PostMapping
    public ResponseEntity<EventoResponse> crearEvento(@Valid @RequestBody EventoRequest request) {
        EventoResponse response = eventoService.crearEvento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}