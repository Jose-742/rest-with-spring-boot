package br.com.erudio.data.vo.v1;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PersonVO implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
}
