package com.codigo.application.controller;

import com.codigo.domain.agregates.request.EmpresaRequest;
import com.codigo.domain.agregates.response.EmpresaResponse;
import com.codigo.domain.ports.in.EmpresaServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa/v1")
@RequiredArgsConstructor
public class EmpresaController {
    
    private final EmpresaServiceIn empresaServiceIn;

    @GetMapping("buscartodos")
    public ResponseEntity<List<EmpresaResponse>> buscartodos(){
        return ResponseEntity.ok(empresaServiceIn.obtenerTodosIn());
    }

    @GetMapping("/buscarxId/{id}")
    public ResponseEntity<EmpresaResponse> buscarxId(@PathVariable Long id){
        return ResponseEntity.ok(empresaServiceIn.obtenerIn(id));
    }

    @PostMapping("/crearEmpresa")
    public ResponseEntity<EmpresaResponse> crearEmpresa(@RequestBody EmpresaRequest request){
        return ResponseEntity.ok(empresaServiceIn.crearIn(request));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EmpresaResponse> actualizar(@RequestBody EmpresaRequest request, @PathVariable Long id){
        return ResponseEntity.ok(empresaServiceIn.actualizarIn(request, id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<EmpresaResponse> eliminar(@PathVariable Long id){
        return ResponseEntity.ok(empresaServiceIn.deleteIn(id));
    }
}
