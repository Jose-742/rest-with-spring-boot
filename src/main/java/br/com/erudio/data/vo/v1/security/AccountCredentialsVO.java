package br.com.erudio.data.vo.v1.security;

import lombok.*;

import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AccountCredentialsVO implements Serializable {

    private String username;
    private String password;
}
