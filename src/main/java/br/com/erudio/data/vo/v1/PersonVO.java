package br.com.erudio.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonPropertyOrder({"id", "address", "firstName", "lastName", "gender"}) // ordenando os atributos
public class PersonVO implements Serializable {

    private Long id;

    @JsonProperty("first_name") //Alterando nome do atributo
    private String firstName;
    private String lastName;
    private String address;

    @JsonIgnore //ignorando atributo json
    private String gender;
}
