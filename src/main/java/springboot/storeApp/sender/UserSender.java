package springboot.storeApp.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import springboot.storeApp.config.WebConfiguration;
import springboot.storeApp.model.AppUser;

public class UserSender {

    private final RabbitTemplate rabbitTemplate;
    
    @Autowired
    public UserSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
 
    public void addUser(AppUser appUser) {
        this.rabbitTemplate.convertAndSend(WebConfiguration.QUEUE_ADD_USER, appUser);
    }
    
    public void removeUser(AppUser appUser) {
        this.rabbitTemplate.convertAndSend(WebConfiguration.QUEUE_REMOVE_USER, appUser);
    }
    
}