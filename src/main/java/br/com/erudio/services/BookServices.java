package br.com.erudio.services;

import br.com.erudio.controllers.BookController;
import br.com.erudio.controllers.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.Book;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.BookRepository;
import br.com.erudio.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Service
public class BookServices {

    @Autowired
    private BookRepository bookRepository;

    public List<BookVO> findAll() {
        var books = DozerMapper.parseListObjects(bookRepository.findAll(), BookVO.class);
        books
              .forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));
        return books;
    }

    public BookVO findById(Long id){
        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO book) {

        if (book == null) throw new RequiredObjectIsNullException();

        log.info("Creating new BookVO!");
        var entity = DozerMapper.parseObject(book, Book.class);
        var vo = DozerMapper.parseObject(bookRepository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) {

        if (book == null) throw new RequiredObjectIsNullException();

        log.info("Updating new BookVO!");
        var entity = bookRepository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var vo = DozerMapper.parseObject(bookRepository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id){
        log.info("Deleting BookVO!");
        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        bookRepository.delete(entity);
    }

}
