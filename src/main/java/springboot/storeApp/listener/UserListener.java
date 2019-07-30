package springboot.storeApp.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import springboot.storeApp.config.WebConfiguration;
import springboot.storeApp.model.AppUser;

public class UserListener {

	static final Logger logger = LoggerFactory.getLogger(UserListener.class);
	 
    @RabbitListener(queues = WebConfiguration.QUEUE_ADD_USER)
    public void addUser(AppUser appUser) {
    	System.out.println("Listening to QUEUE_ADD_USER :: ");
    	logger.info("User to be Added: "+appUser);
        System.out.println("User Added :: "+appUser);
    }
    
    @RabbitListener(queues = WebConfiguration.QUEUE_REMOVE_USER)
    public void removeUser(AppUser appUser) {
    	System.out.println("Listening to QUEUE_REMOVE_USER :: ");
    	logger.info("User to be Removed: "+appUser);
        System.out.println("User Removed :: "+appUser);
    }

}
