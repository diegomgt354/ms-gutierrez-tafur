package com.codigo.application.controller;

import com.codigo.domain.agregates.request.PersonaRequest;
import com.codigo.domain.agregates.response.PersonaResponse;
import com.codigo.domain.ports.in.PersonaServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persona/v1")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaServiceIn personaServiceIn;

    @GetMapping("/buscarxId/{id}")
    public ResponseEntity<PersonaResponse> buscarxId(@PathVariable Long id){
        return ResponseEntity.ok(personaServiceIn.obtenerIn(id));
    }

    @GetMapping("/buscartodos")
    public ResponseEntity<List<PersonaResponse>> buscartodos(){
        return ResponseEntity.ok(personaServiceIn.obtenerTodosIn());
    }

    @PostMapping("/crearPersona")
    public ResponseEntity<PersonaResponse> crearPersona(@RequestBody PersonaRequest request){
        return ResponseEntity.ok(personaServiceIn.crearIn(request));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PersonaResponse> actualizar(@RequestBody PersonaRequest request,@PathVariable Long id){
        return ResponseEntity.ok(personaServiceIn.actualizarIn(request, id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<PersonaResponse> eliminar(@PathVariable Long id){
        return  ResponseEntity.ok(personaServiceIn.deleteIn(id));
    }

}
