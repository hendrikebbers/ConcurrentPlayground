package org.example.concurrent.pipe;

import org.example.concurrent.base.ResourceManager;
import org.example.concurrent.base.impl.ResourceManagerImpl;
import org.example.concurrent.pipe.api.Publisher;
import org.example.concurrent.pipe.impl.QueueBasedPublisher;

public class Sample {

    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManagerImpl();

        Publisher<String> publisher1 = new QueueBasedPublisher<>("foo", resourceManager.getOrCreateExecutor("fooExec"));
        Publisher<String> publisher2 = new QueueBasedPublisher<>("bar", resourceManager.getOrCreateExecutor("barExec"));

        publisher1.peek(System.out::println)
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("A"))
                .forward(publisher2);
        publisher2.forEach(v -> System.out.println("Pipe2: " + v));

        publisher1.publish("Dallas");
        publisher1.publish("Austin");
        publisher1.publish("Huston");
    }
}
