package org.example.activemq;

import org.apache.camel.builder.component.ComponentsBuilderFactory;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
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

        ComponentsBuilderFactory.amqp().connectionFactory(new JmsConnectionFactory(connection)).register(getContext(), "amqp");

        from(timer("demo").period(1000).repeatCount(3)).routeId("producer").autoStartup(name.contains("producer"))
                .process(e -> e.getIn().setBody("V_" + atomicInteger.incrementAndGet()))
                .setHeader("start", constant(System.currentTimeMillis()))
                .to(amqp("topic:demo1"))
                .log("${header.start} : ${body}");

            from(amqp("topic:demo1").subscriptionDurable(true).subscriptionName(name).clientId(name))
                    .routeId("consumer").autoStartup(name.contains("consumer"))
                    .log("${header.start} : ${body}");
    }

}