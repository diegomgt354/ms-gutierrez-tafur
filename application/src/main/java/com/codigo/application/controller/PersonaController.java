package com.codigo.application.controller;

import com.codigo.domain.agregates.request.PersonaRequest;
import com.codigo.domain.agregates.response.EmpresaResponse;
import com.codigo.domain.agregates.response.PersonaResponse;
import com.codigo.domain.ports.in.ServiceIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persona/v1")
public class PersonaController {

    @Autowired
    @Qualifier("personaServiceImpl")
    private ServiceIn<PersonaResponse, PersonaRequest> serviceIn;

    @GetMapping("/buscarxId/{id}")
    public ResponseEntity<PersonaResponse> buscarxId(@PathVariable Long id){
        return ResponseEntity.ok(serviceIn.obtenerIn(id));
    }

    @GetMapping("/buscartodos")
    public ResponseEntity<List<PersonaResponse>> buscartodos(){
        return ResponseEntity.ok(serviceIn.obtenerTodosIn());
    }

    @PostMapping("/crearPersona")
    public ResponseEntity<PersonaResponse> crearPersona(@RequestBody PersonaRequest request){
        return ResponseEntity.ok(serviceIn.crearIn(request));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PersonaResponse> actualizar(@RequestBody PersonaRequest request,@PathVariable Long id){
        return ResponseEntity.ok(serviceIn.actualizarIn(request, id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<PersonaResponse> eliminar(@PathVariable Long id){
        return  ResponseEntity.ok(serviceIn.deleteIn(id));
    }

}
