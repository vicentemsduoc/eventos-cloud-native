package com.cloudnative.eventos.service;

import com.cloudnative.eventos.dto.CompraRequest;
import com.cloudnative.eventos.dto.CompraResponse;
import com.cloudnative.eventos.model.Compra;
import com.cloudnative.eventos.model.Evento;
import com.cloudnative.eventos.repository.CompraRepository;
import com.cloudnative.eventos.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final EventoRepository eventoRepository;
    private final ReporteService reporteService;

    public CompraResponse realizarCompra(CompraRequest request) {
        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Evento no encontrado con id: " + request.getEventoId()));

        BigDecimal precioUnitario = evento.getPrecio();
        BigDecimal precioTotal = precioUnitario.multiply(BigDecimal.valueOf(request.getCantidad()));

        Compra compra = new Compra();
        compra.setEvento(evento);
        compra.setCantidad(request.getCantidad());
        compra.setPrecioUnitario(precioUnitario);
        compra.setPrecioTotal(precioTotal);
        compra.setFechaCompra(LocalDateTime.now());

        Compra guardada = compraRepository.save(compra);

        try {
            reporteService.generarYSubirResumen(guardada);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el resumen", e);
        }

        return toResponse(guardada);
    }

    private CompraResponse toResponse(Compra compra) {
        CompraResponse response = new CompraResponse();
        response.setId(compra.getId());
        response.setNombreEvento(compra.getEvento().getNombre());
        response.setFechaEvento(compra.getEvento().getFecha());
        response.setCantidad(compra.getCantidad());
        response.setPrecioUnitario(compra.getPrecioUnitario());
        response.setPrecioTotal(compra.getPrecioTotal());
        response.setFechaCompra(compra.getFechaCompra());
        return response;
    }

    public Map<String, Object> obtenerEstadisticas() {
        List<Compra> compras = compraRepository.findAll();

        Map<Long, List<Compra>> porEvento = compras.stream()
                .collect(Collectors.groupingBy(c -> c.getEvento().getId()));

        List<Map<String, Object>> estadisticas = new ArrayList<>();

        for (Map.Entry<Long, List<Compra>> entry : porEvento.entrySet()) {
            List<Compra> lista = entry.getValue();
            int totalEntradas = lista.stream().mapToInt(Compra::getCantidad).sum();
            BigDecimal totalVentas = lista.stream()
                    .map(Compra::getPrecioTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> item = new HashMap<>();
            item.put("eventoId", entry.getKey());
            item.put("nombreEvento", lista.get(0).getEvento().getNombre());
            item.put("totalEntradasVendidas", totalEntradas);
            item.put("totalVentas", totalVentas);
            item.put("cantidadCompras", lista.size());
            estadisticas.add(item);
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("estadisticasPorEvento", estadisticas);
        resultado.put("totalGeneralEntradas", compras.stream().mapToInt(Compra::getCantidad).sum());
        resultado.put("totalGeneralVentas", compras.stream()
                .map(Compra::getPrecioTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return resultado;
    }
}
