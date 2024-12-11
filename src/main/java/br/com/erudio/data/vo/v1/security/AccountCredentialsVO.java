package br.com.erudio.data.vo.v1.security;

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
