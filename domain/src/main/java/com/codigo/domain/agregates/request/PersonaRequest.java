package com.codigo.domain.agregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaRequest {

    private String tipoDocumento;
    private String numeroDocumento;
    private String empresa;
    private String email;
    private String telefono;
    private String direccion;

    public String getEmail(){
        String correo = numeroDocumento+"@correo.com";
        return (email!=null)?email:correo;
    }

}
