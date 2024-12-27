package br.com.erudio.integrationstests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class WrapperPersonVO  implements Serializable {

    @JsonProperty("_embedded")
    private PersonEmbeddedVO embedded;

}
