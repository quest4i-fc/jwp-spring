package next.controller;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import next.dao.UserDao;
import next.model.User;


@Controller
@RequestMapping("/users")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    private UserDao userDao = UserDao.getInstance();

    // 1. 사용자 목록 출력.
    @RequestMapping(value="", method=RequestMethod.GET)
    public String list(HttpSession session, Model model) throws Exception {
        if (!UserSessionUtils.isLogined(session)) {
            return "redirect:/users/loginForm";
        }
        model.addAttribute("users", userDao.findAll());
        return "/user/list";
    }

    // 4. 사용자 상세 보기.
    @RequestMapping(value="/{userId}", method=RequestMethod.GET)
    public String profile(@PathVariable String userId, Model model) throws Exception {
        model.addAttribute("user", userDao.findByUserId(userId));
        return "user/profile";
    }

    // 2. 사용자 생성 폼.
    @RequestMapping(value="/new", method=RequestMethod.GET)
    public String form() {
        return "user/form";
    }

    // 3. 사용자 생성.
    @RequestMapping(value="", method=RequestMethod.POST)
    public String create(User user) throws Exception {
        log.debug("User : {}", user);
        userDao.insert(user);
        return "redirect:/";
    }
    

    // 5. 사용자 수정 폼.
    // /users/{id}/edit - edit()
    @RequestMapping(value="/{userId}/edit", method=RequestMethod.GET)
    public String edit(HttpSession session, @PathVariable String userId, Model model) throws Exception {
        if (!UserSessionUtils.isLogined(session)) {
            return "redirect:/users/loginForm";
        }
        
        User user = userDao.findByUserId(userId);
        if (!UserSessionUtils.getUserFromSession(session).isSameUser(user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        model.addAttribute("user", user);
        return "/user/updateForm";
    }
    
    // 6. 사용자 수정.
    // /users/{id} - update()
    @RequestMapping(value="/{userId}", method=RequestMethod.PUT)
    public String update(HttpSession session, @PathVariable String userId, User newUser) throws Exception {
        
        // 1. 로그인 했는지 검사.
        if (!UserSessionUtils.isLogined(session)) {
            return "redirect:/users/loginForm";
        }

        User user = userDao.findByUserId(userId);

        // 2. 같은 사용자인지 검사.
        if (!UserSessionUtils.getUserFromSession(session).isSameUser(user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        // 3. 사용자 정보를 업데이트하고나서 데이터베이스에 적용한다.
        log.debug("Update user : {}", newUser);
        user.update(newUser);
        userDao.update(user);
        return "redirect:/";
    }

    // /users/loginForm
    @RequestMapping(value="/loginForm", method=RequestMethod.GET)
    public String loginForm() throws Exception{
        return "user/login";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String userId, String password, HttpSession session, Model model) throws Exception {
        User user = userDao.findByUserId(userId);
        if (user == null) {
            model.addAttribute("loginFailed", true);
            return "/user/login";
        }
        
        if (user.matchPassword(password)) {
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        } else {
            model.addAttribute("loginFailed", true);
            return "/user/login";
        }
    } // end of login

    // 7. 사용자 삭제.
    // /users/{id} - destory()
    @RequestMapping(value="/{userId}", method=RequestMethod.DELETE)
    public String destory(HttpSession session, @PathVariable String userId) throws Exception{
        User user = userDao.findByUserId(userId);
        userDao.delete(user);
        return "redirect:/users";
    } // end of destory
    
} // end of UserController
