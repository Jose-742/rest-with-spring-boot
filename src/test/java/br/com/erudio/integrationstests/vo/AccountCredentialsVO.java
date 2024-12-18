package br.com.erudio.integrationstests.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
@AllArgsConstructor
public class AccountCredentialsVO implements Serializable {

    private String username;
    private String password;
}
