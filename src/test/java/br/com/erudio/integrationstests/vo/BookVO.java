package br.com.erudio.integrationstests.vo;

import java.io.Serializable;
import java.util.Date;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class BookVO implements Serializable{

    private Long id;
    private String author;
    private Date launchDate;
    private Double price;
    private String title;
}
