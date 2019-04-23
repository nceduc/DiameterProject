package com.company.backend;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class ControlRequest {

    private static final Logger logger = Logger.getLogger(ControlRequest.class);

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
            logger.warn("Cookies not found [ControlRequest.class]\n" + ex.getMessage());
        }

        return iscookie;
    }


}

