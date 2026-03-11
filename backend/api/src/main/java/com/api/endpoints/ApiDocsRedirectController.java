package com.api.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class ApiDocsRedirectController {
    @GetMapping({ "api-docs/", "api-docs" })
    public RedirectView redirectWithUsingRedirectView() {
        return new RedirectView("/swagger-ui/index.html");
    }
}
