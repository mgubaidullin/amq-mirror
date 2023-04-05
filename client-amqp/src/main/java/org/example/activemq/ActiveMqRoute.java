package org.example.activemq;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.component.amqp.AMQPConnectionDetails;
import org.apache.camel.processor.aggregate.StringAggregationStrategy;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class ActiveMqRoute extends EndpointRouteBuilder {

    AtomicInteger atomicInteger = new AtomicInteger(0);

    @ConfigProperty(name = "name")
    String name;
    @ConfigProperty(name = "connection")
    String connection;

    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel("log:dlq").useOriginalBody().maximumRedeliveries(5).redeliveryDelay(3000));

        System.out.println("Application: " + name);

        from(timer("demo").period(1000).repeatCount(3)).routeId("sender")
                .process(e -> e.getIn().setBody("V_" + atomicInteger.incrementAndGet())).autoStartup(name.contains("sender"))
                .log("${body}")
                .setHeader("start", constant(System.currentTimeMillis()))
                .to(amqp("topic:demo1").connectionFactory(new JmsConnectionFactory(connection)))
                .log("${headers} : ${body}");

            from(amqp("topic:demo1")
                    .durableSubscriptionName("subscription1").subscriptionDurable(true)
                    .clientId("amqp-receiver").connectionFactory(new JmsConnectionFactory(connection)))
                    .routeId("receiver").autoStartup(name.contains("receiver"))
                    .log("${header.start} : ${body}");
    }

}