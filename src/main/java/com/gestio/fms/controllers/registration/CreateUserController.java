package com.gestio.fms.controllers.registration;

import com.gestio.fms.registration.model.NewUser;
import com.gestio.fms.registration.service.CreateUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "admin/api/v1/createUser.do")
public class CreateUserController {


    private CreateUserServiceImpl createUserServiceImpl;
    @PostMapping()
    public String createUser(@RequestBody NewUser request) {
        return createUserServiceImpl.createUser(request);
    }
}