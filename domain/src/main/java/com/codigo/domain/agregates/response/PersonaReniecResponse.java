package com.codigo.domain.agregates.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaReniecResponse {

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;

    public String getApellido(){
        return apellidoPaterno+" "+apellidoMaterno;
    }

}
