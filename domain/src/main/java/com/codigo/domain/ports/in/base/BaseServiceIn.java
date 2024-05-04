package com.codigo.domain.ports.in.base;

import java.util.List;

public interface BaseServiceIn<T,K> {

    T crearIn(K request);
    T obtenerIn(Long id);
    List<T> obtenerTodosIn();
    T actualizarIn(K request, Long id);
    T deleteIn(Long id);

}
