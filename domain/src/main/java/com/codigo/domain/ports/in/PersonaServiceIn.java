package com.codigo.domain.ports.in;

import com.codigo.domain.agregates.request.PersonaRequest;
import com.codigo.domain.agregates.response.PersonaResponse;
import com.codigo.domain.ports.in.base.BaseServiceIn;

public interface PersonaServiceIn extends BaseServiceIn<PersonaResponse, PersonaRequest> {
}
