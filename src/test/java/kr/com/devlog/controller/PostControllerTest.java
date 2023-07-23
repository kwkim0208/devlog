package kr.com.devlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kr.com.devlog.Repository.PostRepository;
import kr.com.devlog.domain.Post;
import kr.com.devlog.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Rollback(value = false)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void deleteData() {
        postRepository.deleteAll();
    }

    @Test
    void test1() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();

        String jsonData = objectMapper.writeValueAsString(postCreate);
        //contentType
        mockMvc.perform(post("/posts").contentType(APPLICATION_JSON).content(jsonData)).
                andExpect(status().isOk()).
                andExpect(content().string("")).
                andDo(print());
    }

    @Test
    void titleIsNull() throws Exception {
        //given

        PostCreate postCreate = PostCreate.builder().title("").content("내용입니다.").build();
        String jsonData = objectMapper.writeValueAsString(postCreate);
        //contentType
        mockMvc.perform(post("/posts").contentType(APPLICATION_JSON).content(jsonData)).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.code").value("400")).
                andExpect(jsonPath("$.message").value("잘못된 요청입니다.")).
                andExpect(jsonPath("$.validation..title").value("타이틀을 입력해주세요")).
                andDo(print());
    }

    @Test
    void postRequest() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();
        String jsonData = objectMapper.writeValueAsString(postCreate);
        //contentType
        mockMvc.perform(post("/posts").contentType(APPLICATION_JSON).content(jsonData)).
                andExpect(status().isOk()).
                andDo(print());
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
//        List<Post> all = postRepository.findAll();
//        Post findPost=new Post();
//        for (Post post : all) {
//            findPost.setTitle(post.getTitle());
//            findPost.setContent(post.getContent());
//        }
//        System.out.println(findPost.getTitle());
//
//
//        Post post = postRepository.findPostByTitle("제목입니다.");
//        assertEquals("제목입니다.", post.getTitle());
//        assertEquals("내용입니다",post.getContent());
    }
}