package com.mysite.sbb.question;

import java.security.Principal;
import java.util.List;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.answer.AnswerForm;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
	private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
    	
    	Page<Question> paging = this.questionService.getList(page);
    	model.addAttribute("paging", paging);
        return "question_list";
    }
    
    @GetMapping(value = "/detail/{id}") //id처럼 변동 값이면 PathVariable 사용
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
    	Question question = this.questionService.getQuestion(id);
    	model.addAttribute("question", question);
    	return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")//로그인 한 경우에만 실행
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
    	return "question_form";
    }
    @PreAuthorize("isAuthenticated()")//로그인 한 경우에만 실행
    @PostMapping("/create") //Valid는 @Size같은 검증 기능을 동작 시킨다. BindingResult는 Valid의 검증이 수행된 결과
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }
    
    
    
    
    
    
}