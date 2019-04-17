package com.company.backend;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class ControlRequest {

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest req) {
        if(isCookie(req)){
            return new ModelAndView("redirect:/main");
        }else {
            return new ModelAndView("/index.html");
        }
    }


    @RequestMapping("/main")
    public ModelAndView main(HttpServletRequest req){
        if(!isCookie(req)){
            return new ModelAndView("redirect:/");
        }else {
            return new ModelAndView("/main.html");
        }
    }


    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp){
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
        return new ModelAndView("redirect:/");
    }



    private boolean isCookie(HttpServletRequest req){
        boolean iscookie = false;
        try {
            Cookie[] cookies = req.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("clientID")){
                    iscookie = true;
                }
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return iscookie;
    }


}

