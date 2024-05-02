package com.codigo.application.controller;

import com.codigo.domain.agregates.request.EmpresaRequest;
import com.codigo.domain.agregates.response.EmpresaResponse;
import com.codigo.domain.ports.in.ServiceIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa/v1")
public class EmpresaController {

    @Autowired
    @Qualifier("empresaServiceImpl")
    private ServiceIn<EmpresaResponse, EmpresaRequest> serviceIn;

    @GetMapping("buscartodos")
    public ResponseEntity<List<EmpresaResponse>> buscartodos(){
        return ResponseEntity.ok(serviceIn.obtenerTodosIn());
    }

    @GetMapping("/buscarxId/{id}")
    public ResponseEntity<EmpresaResponse> buscarxId(@PathVariable Long id){
        return ResponseEntity.ok(serviceIn.obtenerIn(id));
    }

    @PostMapping("/crearEmpresa")
    public ResponseEntity<EmpresaResponse> crearEmpresa(@RequestBody EmpresaRequest request){
        return ResponseEntity.ok(serviceIn.crearIn(request));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EmpresaResponse> actualizar(@RequestBody EmpresaRequest request, @PathVariable Long id){
        return ResponseEntity.ok(serviceIn.actualizarIn(request, id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<EmpresaResponse> eliminar(@PathVariable Long id){
        return ResponseEntity.ok(serviceIn.deleteIn(id));
    }
}
