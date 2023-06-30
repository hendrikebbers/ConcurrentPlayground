package org.example.concurrent.flow;

import org.example.concurrent.base.ResourceManager;
import org.example.concurrent.base.impl.ResourceManagerImpl;
import org.example.concurrent.flow.api.Pipe;
import org.example.concurrent.flow.api.Publisher;
import org.example.concurrent.flow.api.SimpleSubscriber;
import org.example.concurrent.flow.components.ConsensusComponent;
import org.example.concurrent.flow.events.ConsensusStateEvent;
import org.example.concurrent.flow.events.ConsensusEvent;
import org.example.concurrent.flow.impl.PublisherImpl;

public class Sample {

    public static void main(String[] args) {

        ResourceManager resourceManager = new ResourceManagerImpl();

        Publisher<String> pSync = new PublisherImpl<>(resourceManager.createSingleThreadExecutor());
        Pipe<String> pSync2 = pSync.split();
        pSync.map(Integer::parseInt).subscribe((SimpleSubscriber<Integer>) item -> System.out.println(item));

        PublisherImpl<String> pAsync = new PublisherImpl<>(resourceManager.createSingleThreadExecutor());
        pAsync.map(Integer::parseInt)
                .map(i -> i.toString())
                .forward(pSync);
        pSync2.forward(pAsync);

        pAsync.publish("1");
    }

    private static void foo() {
        ConsensusComponent consensusComponent = new ConsensusComponent();

        Publisher<ConsensusEvent> consensus = new PublisherImpl<>(new ResourceManagerImpl().createSingleThreadExecutor());

        final Pipe<ConsensusStateEvent> conensusStatePipe = consensus.map(consensusComponent::getState);
        conensusStatePipe.subscribe((SimpleSubscriber<ConsensusStateEvent>) item -> System.out.println(item));

        final Pipe<ConsensusStateEvent> conensusStatePipe2 = conensusStatePipe.split();
        conensusStatePipe2.subscribe((SimpleSubscriber<ConsensusStateEvent>) item -> {
            if(item.isConsensusReached()) {
                System.out.println("Consensus reached");
            } else {
                ConsensusEvent consusEvent = new ConsensusEvent();
                consensus.publish(consusEvent);
            }
        });

        consensus.publish(new ConsensusEvent());
    }
}
