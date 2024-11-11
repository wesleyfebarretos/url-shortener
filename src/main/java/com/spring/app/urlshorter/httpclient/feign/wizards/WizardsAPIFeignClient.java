package com.spring.app.urlshorter.httpclient.feign.wizards;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "wizards", url = "${integration.wizard-api.url}")
public interface WizardsAPIFeignClient {
    @GetMapping(value = "/Elixirs", consumes = "application/json")
    List<Elixir> getExilirs();
}
