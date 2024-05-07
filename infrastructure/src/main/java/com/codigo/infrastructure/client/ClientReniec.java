package com.codigo.infrastructure.client;

import com.codigo.domain.agregates.response.PersonaReniecResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-document", url = "https://api.apis.net.pe/v2/reniec")
public interface ClientReniec {

    @GetMapping("/dni")
    PersonaReniecResponse getInfoReniec(@RequestParam("numero") String numero, @RequestHeader("Authorization") String authorization);

}
