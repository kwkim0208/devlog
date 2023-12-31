package kr.com.devlog.service;

import kr.com.devlog.Repository.PostRepository;
import kr.com.devlog.domain.Post;
import kr.com.devlog.request.PostCreate;
import kr.com.devlog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @BeforeEach
    void deleteAll(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글작성")
    void test() {
        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();
        postService.write(postCreate);
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void test1() {
        Post requestPost = Post.builder().title("foo").content("bar").build();
        postRepository.save(requestPost);
        Long postId = 1L;
        PostResponse postResponse = postService.getPost(requestPost.getId());

        assertNotNull(postResponse);
        assertEquals("foo", postResponse.getTitle());
        assertEquals("bar", postResponse.getContent());
    }
    @Test
    @DisplayName("등록된 글 조회")
    void test2(){
        Post requestPost1 = Post.builder().title("foo1").content("bar1").build();
        Post requestPost2 = Post.builder().title("foo2").content("bar2").build();
        postRepository.save(requestPost1);
        postRepository.save(requestPost2);
        List<PostResponse> posts = postService.getAllPosts();
        assertEquals(2L,posts.size());

    }

}