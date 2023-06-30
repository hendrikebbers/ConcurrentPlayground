package org.example.concurrent.pipe;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.example.concurrent.base.ResourceManager;
import org.example.concurrent.base.impl.ResourceManagerImpl;
import org.example.concurrent.pipe.api.Publisher;
import org.example.concurrent.pipe.impl.PublisherImpl;

public class Sample {

    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManagerImpl();

        Publisher<String> publisher1 = new PublisherImpl<>("foo", resourceManager.getOrCreateExecutor("fooExec"));
        Publisher<String> publisher2 = new PublisherImpl<>("bar", resourceManager.getOrCreateExecutor("barExec"));

        //TODO: check how we can use java.util.stream.Collector to do a
        // "take multiple items by window" and map it to 1 new item
        //Stream.of("foo").collect(Collectors.toSet())



        publisher1.peek(System.out::println)
                .map(String::toUpperCase)
                .filter(s -> fooComponent.isGood(s))
                .forEach(s -> barComponent.handle(s + "1"));

        publisher2.forEach(System.out::println);

        publisher1.publish("a");
    }
}
