package com.codigo.domain.agregates.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmpresaSunatResponse {

        private String razonSocial;
        private String tipoDocumento;
        private String numeroDocumento;
        private String condicion;
        private String direccion;
        private String distrito;
        private String provincia;
        private String departamento;
        private boolean EsAgenteRetencion;

}
