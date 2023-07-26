package kr.com.devlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kr.com.devlog.Repository.PostRepository;
import kr.com.devlog.domain.Post;
import kr.com.devlog.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Test
    @DisplayName("글 한개 조히")
    void test4() throws  Exception{
        Post post=Post.builder().title("foo").content("bar").build();
        postRepository.save(post);
        mockMvc.perform(get("/posts/{postId}",post.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }
    @Test
    @DisplayName("글한개 조회 && 글 제목은 10글자까지만 가능")
    void test5() throws Exception{
        Post post=Post.builder().title("1234567890123456").content("bar").build();
        postRepository.save(post);
        mockMvc.perform(get("/posts/{postId}",post.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }
    @Test
    @DisplayName("등록된 글 조회")
    void test6() throws Exception {
        Post post1=Post.builder().title("title_1").content("content_1").build();
        Post post2=Post.builder().title("title_2").content("content_2").build();
        postRepository.save(post1);
        postRepository.save(post2);
        mockMvc.perform(get("/posts").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("[0].id").value(post1.getId()))
                .andExpect(jsonPath("[0].title").value("title_1"))
                .andExpect(jsonPath("[0].content").value("content_1"))
                .andExpect(jsonPath("[1].id").value(post2.getId()))
                .andExpect(jsonPath("[1].title").value("title_2"))
                .andExpect(jsonPath("[1].content").value("content_2"))
                .andDo(print());

    }
}