package com.codigo.domain.ports.out.base;

import java.util.List;

public interface BaseServiceOut<T,K> {

    T crearOut(K request);
    T obtenerOut(Long id);
    List<T> obtenerTodosOut();
    T actualizarOut(K request, Long id);
    T deleteOut(Long id);
}
