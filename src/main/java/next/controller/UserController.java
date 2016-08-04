package next.controller;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import next.dao.UserDao;
import next.model.User;


@Controller
@RequestMapping("/users")
public class UserController {
//	private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    private UserDao userDao = UserDao.getInstance();

    // 1. 사용자 목록 출력.
    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView list() throws Exception {
        ModelAndView mav = new ModelAndView("/user/list");
        mav.addObject("users", userDao.findAll());
        return mav;
    }

    // 2. 사용자 생성 폼.
    @RequestMapping(value="/new", method=RequestMethod.GET)
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "user/form";
    }

    // 3. 사용자 생성.
    @RequestMapping(value="", method=RequestMethod.POST)
    public String create(User user) throws Exception {
        userDao.insert(user);
        return "redirect:/users";
    }
    
    // 4. 사용자 상세 보기.
    @RequestMapping(value="/{userId}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable String userId) {
        User user = userDao.findByUserId(userId);
        ModelAndView mav = new ModelAndView("/user/profile");
        mav.addObject("user", user);
        return mav;
    }

    // 5. 사용자 수정 폼.
    // /users/{id}/edit - edit()
    @RequestMapping(value="/{userId}/edit", method=RequestMethod.GET)
    public ModelAndView edit(Model model, @PathVariable String userId) throws Exception {
        User user = userDao.findByUserId(userId);
        model.addAttribute("user", user);
        ModelAndView mav = new ModelAndView("user/updateForm");
        mav.addObject("user", user);
        return mav;
//        return "user/updateForm";
    }
    
    // 6. 사용자 수정.
    // /users/{id} - update()
    @RequestMapping(value="/{userId}", method=RequestMethod.PUT)
    public String update(@PathVariable String userId, User updateUser) throws Exception {
        User user = userDao.findByUserId(userId);
        user.update(updateUser);
        userDao.update(user);
        return "redirect:/users";
    }

    // 7. 사용자 삭제.
    // /users/{id} - destory()
    @RequestMapping(value="/{userId}", method=RequestMethod.DELETE)
    public String destory(@PathVariable String userId) {
        User user = userDao.findByUserId(userId);
        userDao.delete(user);
        return "redirect:/users";
    }
    
    // /users/login - login()
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login() {
       return "user/login";
    }
}
