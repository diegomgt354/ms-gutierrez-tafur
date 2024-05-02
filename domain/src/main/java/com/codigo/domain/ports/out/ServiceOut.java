package com.codigo.domain.ports.out;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface ServiceOut<T,K> {

    T crearOut(K request);
    T obtenerOut(Long id);
    List<T> obtenerTodosOut();
    T actualizarOut(K request, Long id);
    T deleteOut(Long id);
}
