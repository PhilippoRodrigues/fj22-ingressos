package br.com.caelum.ingresso.rest;

import br.com.caelum.ingresso.model.DetalhesFilme;
import br.com.caelum.ingresso.model.Filme;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class OmdbClient {
    public <T> Optional<T> request(Filme filme, Class<T> tClass){
        RestTemplate client = new RestTemplate();

        String titulo = filme.getNome().replace(" ", "+");

        String url = String.format("https://omdb-fj22.herokuapp.com/movie?title=%s", titulo);

        try{
            return Optional.of(client.getForObject(url, tClass));
        }catch(RestClientException e){
            return Optional.empty();
        }
    }
}
