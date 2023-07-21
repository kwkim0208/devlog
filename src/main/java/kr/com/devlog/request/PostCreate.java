package kr.com.devlog.request;

import jakarta.validation.constraints.NotBlank ;
import lombok.Data;

@Data
public class PostCreate {
    //null 도 체크해줌
    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;
    @NotBlank(message = "콘텐츠를 입력해주세요")
    private String content;


}
