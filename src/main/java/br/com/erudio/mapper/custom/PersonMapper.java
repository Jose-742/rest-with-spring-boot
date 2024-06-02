package br.com.erudio.mapper.custom;

import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonVOV2 convertEntityToVo(Person person) {
        PersonVOV2 personVO = new PersonVOV2();
        personVO.setId(person.getId());
        personVO.setFirstName(person.getFirstName());
        personVO.setLastName(person.getLastName());
        personVO.setAddress(person.getAddress());
        personVO.setBirthDate(new Date());
        personVO.setGender(person.getGender());
        return personVO;
    }

    public Person convertToVoEntity(PersonVOV2 person) {
        Person entity = new Person();
        entity.setId(person.getId());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        //entity.setBirthDate(new Date());
        entity.setGender(person.getGender());
        return entity;
    }
}
