package kr.com.devlog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCreate {
    //null 도 체크해줌
    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;
    @NotBlank(message = "콘텐츠를 입력해주세요")
    private String content;

}
