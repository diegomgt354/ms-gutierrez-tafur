package com.codigo.domain.ports.out;

import com.codigo.domain.agregates.request.PersonaRequest;
import com.codigo.domain.agregates.response.PersonaResponse;
import com.codigo.domain.ports.out.base.BaseServiceOut;

public interface PersonaServiceOut extends BaseServiceOut<PersonaResponse, PersonaRequest> {
}
