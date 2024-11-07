package com.spring.app.urlshorter.controller.integration.wizardapi;

import com.spring.app.urlshorter.httpclient.feign.wizards.Elixir;
import com.spring.app.urlshorter.httpclient.feign.wizards.WizardsAPIFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("integration/wizards")
@RequiredArgsConstructor
public class WizardController {
    private final WizardsAPIFeignClient wizardAPIFeignClient;

    @GetMapping("/elixirs")
    public List<Elixir> getElixirs() {
        return wizardAPIFeignClient.getExilirs();
    }
}
