package com.codigo.domain.impl;

import com.codigo.domain.agregates.request.EmpresaRequest;
import com.codigo.domain.agregates.response.EmpresaResponse;
import com.codigo.domain.ports.in.EmpresaServiceIn;
import com.codigo.domain.ports.in.base.BaseServiceIn;
import com.codigo.domain.ports.out.EmpresaServiceOut;
import com.codigo.domain.ports.out.base.BaseServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaServiceIn {

    private final EmpresaServiceOut serviceEmpresaOut;

    @Override
    public EmpresaResponse crearIn(EmpresaRequest request) {
        return serviceEmpresaOut.crearOut(request);
    }

    @Override
    public EmpresaResponse obtenerIn(Long id) {
        return serviceEmpresaOut.obtenerOut(id);
    }

    @Override
    public List<EmpresaResponse> obtenerTodosIn() {
        return serviceEmpresaOut.obtenerTodosOut();
    }

    @Override
    public EmpresaResponse actualizarIn(EmpresaRequest request, Long id) {
        return serviceEmpresaOut.actualizarOut(request, id);
    }

    @Override
    public EmpresaResponse deleteIn(Long id) {
        return serviceEmpresaOut.deleteOut(id);
    }
}
