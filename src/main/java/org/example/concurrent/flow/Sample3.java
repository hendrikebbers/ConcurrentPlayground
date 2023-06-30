package org.example.concurrent.flow;

import org.example.concurrent.base.ResourceManager;
import org.example.concurrent.base.impl.ResourceManagerImpl;
import org.example.concurrent.flow.api.Pipe;
import org.example.concurrent.flow.api.Publisher;
import org.example.concurrent.flow.events.ConsensusEvent;
import org.example.concurrent.flow.impl.PublisherImpl;

public class Sample3 {


    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManagerImpl();


        
        Publisher<ConsensusEvent> q1 = new PublisherImpl<>(resourceManager.createSingleThreadExecutor());
        Publisher<ConsensusEvent> q2 = new PublisherImpl<>(resourceManager.createSingleThreadExecutor());

        q1.map(ConsensusEvent::getConsensusState)
                .filter(ConsensusEvent::isConsensusReached)
                //.subscribe(item -> System.out.println("Consensus reached"));
                        .forward(q2);


        q2.subscribe(item -> System.out.println("Consensus reached 1"));

        final Pipe<ConsensusEvent> q2_1 = q2.split();
        q2_1.subscribe(item -> System.out.println("Consensus reached 2"));

        final Pipe<ConsensusEvent> q2_2 = q2.split();
        q2_2.subscribe(item -> System.out.println("Consensus reached 3"));

        q1.publish(new ConsensusEvent());

    }
}
