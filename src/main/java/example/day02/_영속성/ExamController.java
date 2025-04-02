package example.day02._영속성;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/day02/persis")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @GetMapping
    public void get(){
        examService.get();
    }
}
