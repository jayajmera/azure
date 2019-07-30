package springboot.storeApp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springboot.storeApp.listener.UserListener;



@Configuration
@EnableWebMvc
public class WebConfiguration  implements RabbitListenerConfigurer {

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		// Load file: validation.properties
		messageSource.setBasename("classpath:validation");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	public static final String QUEUE_ADD_USER = "add-user-queue";
	public static final String QUEUE_REMOVE_USER = "remove-user-queue";
	public static final String EXCHANGE_USER = "user-exchange";
	
	
	@Bean
    Queue addUserQueue() {
        return QueueBuilder.durable(QUEUE_ADD_USER).build();
    }
 
    @Bean
    Queue removeUserQueue() {
        return QueueBuilder.durable(QUEUE_REMOVE_USER).build();
    }
 
    @Bean
    Exchange userExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_USER).build();
    }
 
    @Bean
    Binding addUserbinding(Queue addUserQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(addUserQueue).to(userExchange).with(QUEUE_ADD_USER);
    }
    
    @Bean
    Binding removeUserbinding(Queue removeUserQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(removeUserQueue).to(userExchange).with(QUEUE_REMOVE_USER);
    }
    
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConnectionFactory(connectionFactory);

        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }
 
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
 
    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }
 
    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }
    
/*  @Bean
    public UserListener userListener(){
    	return new UserListener();
    }*/

}