package com.spring.learning.jobportal.scopes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scope")
@RequiredArgsConstructor
public class ScopeController {

    private final SessionScopedBean sessionScopedBean;
    private final RequestScopedBean requestScopedBean;
    private final ApplicationScopedBean applicationScopedBean;


    @GetMapping("/request")
    public ResponseEntity<String> testRequestScope(){
        requestScopedBean.setUserName("arvind");
        return ResponseEntity.ok().body(requestScopedBean.getUserName());

    }

    @GetMapping("/session")
    public ResponseEntity<String> testSessionScope(){
        sessionScopedBean.setUserName("singh");
        return ResponseEntity.ok().body(sessionScopedBean.getUserName());

    }

    @GetMapping("test")
    public ResponseEntity<Integer> testScope(){
        return ResponseEntity.ok().body(applicationScopedBean.getVisitorCount());

    }

    @GetMapping("/application")
    public ResponseEntity<Integer> testApplicationScope(){
        applicationScopedBean.incrementVisitorCount();
        return ResponseEntity.ok().body(applicationScopedBean.getVisitorCount());

    }

}
