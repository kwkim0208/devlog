package kr.com.devlog.controller;


import jakarta.validation.Valid;
import kr.com.devlog.domain.Post;
import kr.com.devlog.request.PostCreate;
import kr.com.devlog.response.PostResponse;
import kr.com.devlog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
 /*   @PostMapping("/posts")
    public String get(@RequestParam String title,@RequestParam String content)
    {
        log.info("title={}, content={}",title,content);
        return "hello world";
    }*/
 /*@PostMapping("/posts")
 public String get(@RequestParam Map<String,String>params)
 {
     log.info("params={}",params);
     return "hello world";
 }*/
///데이터 검증이 필요한이유 서버의 안전성을 높이기위해서
    @PostMapping("/posts")
    public void post(@Valid @RequestBody PostCreate params ) {
     /*   Map<String,String> error=new HashMap<>();
        if(result.hasErrors()){
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String invalidFieldName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();
            error.put(invalidFieldName,errorMessage);
            return error;

        }*/
        log.info("params={}",params);
        String title=params.getTitle();
   /*     if(title==null||title.equals("")){
            //이런식의 데이터 검증은 너무 중복되는 코드 많아짐
            //다른 방법이 필요함(필드가 많아지면 누락가능성이 있음) 꼼꼼하게 검증해야한다
            throw new Exception("타이값이 없어요");
        }*/
        postService.write(params);
    }
    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable(name = "postId")Long id){
        PostResponse post = postService.getPost(id);
        return post;
    }
    @GetMapping("/posts")
    public List<PostResponse> getAllPosts(){
        return postService.getAllPosts();
    }
}
