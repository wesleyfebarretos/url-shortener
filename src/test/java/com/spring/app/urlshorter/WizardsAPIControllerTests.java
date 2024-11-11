package com.spring.app.urlshorter;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.spring.app.urlshorter.httpclient.feign.wizards.Elixir;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WizardsAPIControllerTests extends BaseIntegrationTests {

    @Nested
    class Get {
        @Test
        @DisplayName("it should all elixirs")
        public void getElixirs() throws Exception {
            Elixir elixir = new Elixir()
                    .setId("0106fb32-b00d-4d70-9841-4b7c2d2cca71")
                    .setName("Fergus Fungal Budge")
                    .setEffect("Treats ringworm, fungicide")
                    .setSideEffects("Potential negative side effects if used by elves")
                    .setDifficulty("Unknown")
                    .setManufacturer(null)
                    .setCharacteristics(null)
                    .setIngredients(List.of())
                    .setInventors(List.of());

            WireMock.stubFor(
                    WireMock.get("/Elixirs").willReturn(
                            WireMock.aResponse()
                                    .withHeader("Content-Type", "application/json")
                                    .withStatus(200)
                                    .withBody(asJsonString(List.of(elixir)))
                    )
            );

            mockMvc.perform(get("/integration/wizards/elixirs")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(elixir.getId()))
                    .andExpect(jsonPath("$[0].name").value(elixir.getName()))
                    .andExpect(jsonPath("$[0].sideEffects").value(elixir.getSideEffects()));
        }
    }
}