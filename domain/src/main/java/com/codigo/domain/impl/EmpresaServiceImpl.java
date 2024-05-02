package com.codigo.domain.impl;

import com.codigo.domain.ports.in.ServiceIn;
import com.codigo.domain.ports.out.ServiceOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmpresaServiceImpl<T,K> implements ServiceIn<T,K> {

    @Autowired(required = false)
    @Qualifier("empresa_adapter")
    private ServiceOut<T,K> serviceEmpresaOut;

    @Override
    public T crearIn(K request) {
        return serviceEmpresaOut.crearOut(request);
    }

    @Override
    public T obtenerIn(Long id) {
        return serviceEmpresaOut.obtenerOut(id);
    }

    @Override
    public List<T> obtenerTodosIn() {
        return serviceEmpresaOut.obtenerTodosOut();
    }

    @Override
    public T actualizarIn(K request, Long id) {
        return serviceEmpresaOut.actualizarOut(request, id);
    }

    @Override
    public T deleteIn(Long id) {
        return serviceEmpresaOut.deleteOut(id);
    }
}
