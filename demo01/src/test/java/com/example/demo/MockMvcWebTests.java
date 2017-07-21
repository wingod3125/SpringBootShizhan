package com.example.demo;

import com.example.demo.readingList.Book;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.awt.geom.Path2D.contains;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Administrator on 7/21 0021.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MockMvcWebTests {
    @Autowired
    private WebApplicationContext webContext;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())//开启Spring Security支持
                .build();
    }

    @Test
    public void homePage() throws Exception {
        mockMvc.perform(get("/readingList"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", Matchers.is(Matchers.empty())));
    }

    @Test
    @WithMockUser(username = "craig",
                    password = "password",
                    roles = "READER")
    public void homePage_unauthenticatedUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    /*@Test
    public void postBook() throws Exception {
        mockMvc.perform(post("/readingList")
                .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                .param("title","Book Title")
                .param("author","Book Author")
                .param("isbn","123456")
                .param("description","DESCRIPTION"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location","/readingList"));

        Book expectedBook = new Book();
        expectedBook.setTitle("Book Title");
        expectedBook.setAuthor("Book Author");
        expectedBook.setIsbn("123456");
        expectedBook.setDescription("DESCRIPTION");

        mockMvc.perform(get("/readingList"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(1)));
//                .andExpect(model().attribute("books",contains(samePropertyValuesAs(expectedBook))));
    }*/
}
