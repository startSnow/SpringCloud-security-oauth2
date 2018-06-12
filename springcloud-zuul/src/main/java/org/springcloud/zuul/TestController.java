package org.springcloud.zuul;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {
    @Autowired  
    @Qualifier("sessionRegistry")  
    private SessionRegistry sessionRegistry;   
    @RequestMapping(name="/test/onlineUser")
    public String  PrintAllOnlineUser()  {  
    	    List<Object> principals = sessionRegistry.getAllPrincipals();  
    	  
    	    List<String> usersNamesList = new ArrayList<String>();  
    	  
    	    for (Object principal: principals) {  
    	        if (principal instanceof User) {  
    	            usersNamesList.add(((User) principal).getUsername());  
    	        }  
    	    }  
    	      
			return "count:"+usersNamesList.size()+"=>"+usersNamesList.toString();  
    	} 
    }
    
