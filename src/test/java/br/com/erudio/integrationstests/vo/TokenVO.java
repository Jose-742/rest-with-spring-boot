package br.com.erudio.integrationstests.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO implements Serializable {

    private String username;
    private Boolean authenticated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;

}
