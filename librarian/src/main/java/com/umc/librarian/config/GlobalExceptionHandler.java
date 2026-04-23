package com.umc.librarian.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.net.URISyntaxException;

@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String handleBusinessException(RuntimeException exception,
                                          HttpServletRequest request,
                                          RedirectAttributes redirectAttributes) {
        // Envia a mensagem de erro para o front-end via FlashAttributes (usado nos seus alerts HTML)
        redirectAttributes.addFlashAttribute("error", exception.getMessage());

        String referer = request.getHeader("Referer");
        if (referer == null || referer.isBlank()) {
            return "redirect:/dashboard";
        }

        return "redirect:" + localPathFrom(referer);
    }

    private String localPathFrom(String referer) {
        try {
            URI uri = new URI(referer);
            String path = uri.getRawPath();
            String query = uri.getRawQuery();

            return (path == null || path.isBlank()) ? "/dashboard" : 
                   (query == null ? path : path + "?" + query);
        } catch (URISyntaxException exception) {
            return "/dashboard";
        }
    }
}