package br.org.sesisenai.clinipet.security.utils;

import br.org.sesisenai.clinipet.model.entity.Pessoa;
import br.org.sesisenai.clinipet.model.factory.PessoaFactory;
import br.org.sesisenai.clinipet.security.model.entity.UserJpa;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserJpaDeserializer extends JsonDeserializer<UserJpa> {

    @Override
    public UserJpa deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = objectMapper.readTree(jsonParser);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        ArrayNode authoritiesNode = (ArrayNode) node.get("authorities");

        for (JsonNode authoriJsonNode : authoritiesNode) {
            String authority = authoriJsonNode.get("authority").asText();
            SimpleGrantedAuthority sga = new SimpleGrantedAuthority(authority);
            authorities.add(sga);
        }

        Pessoa pessoa = objectMapper.convertValue(node.get("pessoa"), Pessoa.class);
        SimpleGrantedAuthority authority = authorities.get(0);
        pessoa = new PessoaFactory().getPessoa(authority, pessoa);

        return new UserJpa(pessoa);
    }
}
