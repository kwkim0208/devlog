package kr.com.devlog.service;

import kr.com.devlog.Repository.PostRepository;
import kr.com.devlog.domain.Post;
import kr.com.devlog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = Post.builder().title(postCreate.getTitle()).content(postCreate.getContent()).build();
        postRepository.save(post);

    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 글입니다."));

    }
}
