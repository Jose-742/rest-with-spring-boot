package br.com.erudio.integrationstests.vo.pagedmodels;

import br.com.erudio.integrationstests.vo.PersonVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class PagedModelPerson {

    @XmlElement(name = "content")
    private List<PersonVO> content;
}