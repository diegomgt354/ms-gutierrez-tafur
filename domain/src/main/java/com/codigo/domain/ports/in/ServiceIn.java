package com.codigo.domain.ports.in;

import java.util.List;
import java.util.Optional;

public interface ServiceIn<T,K> {

    T crearIn(K request);
    T obtenerIn(Long id);
    List<T> obtenerTodosIn();
    T actualizarIn(K request, Long id);
    T deleteIn(Long id);

}
