package br.com.erudio.integrationstests.vo;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class AccountCredentialsVO implements Serializable {

    private String username;
    private String password;
}
