package br.com.erudio.integrationstests.vo.wrappers;

import br.com.erudio.integrationstests.vo.PersonVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class PersonEmbeddedVO implements Serializable {

    @JsonProperty("personVOList")
    private List<PersonVO> persons;
}
