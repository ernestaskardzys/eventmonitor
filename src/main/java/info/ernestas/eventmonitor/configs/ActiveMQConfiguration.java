package info.ernestas.eventmonitor.configs;

import info.ernestas.eventmonitor.service.EventService;
import info.ernestas.eventmonitor.service.activemq.ActiveMQErrorHandler;
import info.ernestas.eventmonitor.service.activemq.IncomingEventListener;
import info.ernestas.eventmonitor.service.activemq.ProcessedEventListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.MessageListener;

@Configuration
@EnableJms
public class ActiveMQConfiguration {

    private String incomingEventQueue;

    private String processedEventQueue;

    private String brokerName;

    private String brokerUrl;

    private Integer consumersCount;

    private Integer maxConsumersCount;

    @Bean
    public BrokerService brokerService() throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setPersistent(false);
        brokerService.setUseJmx(false);
        brokerService.addConnector(brokerUrl);
        brokerService.setBrokerName(brokerName);
        brokerService.setUseShutdownHook(false);
        return brokerService;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    @Bean
    public ActiveMQQueue incomingEventQueue() {
        return new ActiveMQQueue(incomingEventQueue);
    }

    @Bean
    public ActiveMQQueue processedEventQueue() {
        return new ActiveMQQueue(processedEventQueue);
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setDefaultDestination(incomingEventQueue());
        return jmsTemplate;
    }

    @Bean
    public DefaultMessageListenerContainer incomingEventQueueContainer(ConnectionFactory connectionFactory, SimpMessagingTemplate simpMessagingTemplate, EventService eventService) {
        DefaultMessageListenerContainer containerFactory = new DefaultMessageListenerContainer();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setDestination(incomingEventQueue());
        containerFactory.setMessageListener(incomingEventMessageListener(eventService));
        containerFactory.setErrorHandler(new ActiveMQErrorHandler(simpMessagingTemplate));
        containerFactory.setConcurrentConsumers(consumersCount);
        containerFactory.setMaxConcurrentConsumers(maxConsumersCount);
        return containerFactory;
    }

    @Bean
    public DefaultMessageListenerContainer processedEventQueueContainer(ConnectionFactory connectionFactory, SimpMessagingTemplate simpMessagingTemplate) {
        DefaultMessageListenerContainer containerFactory = new DefaultMessageListenerContainer();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setDestination(processedEventQueue());
        containerFactory.setMessageListener(processedEventMessageListener(simpMessagingTemplate));
        containerFactory.setConcurrentConsumers(consumersCount);
        containerFactory.setMaxConcurrentConsumers(maxConsumersCount);
        return containerFactory;
    }

    @Bean
    public MessageListener incomingEventMessageListener(EventService eventService) {
        return new IncomingEventListener(eventService);
    }

    @Bean
    public MessageListener processedEventMessageListener(SimpMessagingTemplate simpMessagingTemplate) {
        return new ProcessedEventListener(simpMessagingTemplate);
    }

    @Value("${activemq.queue.name.incomingEventQueue}")
    public void setIncomingEventQueue(String incomingEventQueue) {
        this.incomingEventQueue = incomingEventQueue;
    }

    @Value("${activemq.queue.name.processedEventQueue}")
    public void setProcessedEventQueue(String processedEventQueue) {
        this.processedEventQueue = processedEventQueue;
    }

    @Value("${activemq.broker.name}")
    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    @Value("${activemq.broker.url}")
    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    @Value("${activemq.queue.consumers.count}")
    public void setConsumersCount(Integer consumersCount) {
        this.consumersCount = consumersCount;
    }

    @Value("${activemq.queue.consumers.maxCount}")
    public void setMaxConsumersCount(Integer maxConsumersCount) {
        this.maxConsumersCount = maxConsumersCount;
    }

}
