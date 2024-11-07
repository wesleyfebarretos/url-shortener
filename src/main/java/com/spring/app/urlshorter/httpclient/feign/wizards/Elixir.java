package com.spring.app.urlshorter.httpclient.feign.wizards;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Elixir {
    public String id;
    public String name;
    public String effect;
    public String sideEffects;
    public String characteristics;
    public String time;
    public String difficulty;
    public List<Ingredient> ingredients;
    public List<Inventor> inventors;
    public String manufacturer;

    public static class Ingredient {
        public String id;
        public String name;
    }

    public static class Inventor {
        public String id;
        public String firstName;
        public String lastName;
    }
}
