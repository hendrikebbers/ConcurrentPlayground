package org.example.concurrent.flow;

import io.reactivex.rxjava3.core.Flowable;
import org.example.concurrent.base.impl.ResourceManagerImpl;
import org.example.concurrent.flow.api.Publisher;
import org.example.concurrent.flow.components.ConsensusComponent;
import org.example.concurrent.flow.events.ConsensusEvent;
import org.example.concurrent.flow.impl.PublisherImpl;
import org.reactivestreams.FlowAdapters;

public class Sample2 {

    public static void main(String[] args) {
        ConsensusComponent consensusComponent = new ConsensusComponent();

        Publisher<ConsensusEvent> consensus = new PublisherImpl<>(new ResourceManagerImpl().createSingleThreadExecutor());

        final org.reactivestreams.Publisher<ConsensusEvent> publisher = FlowAdapters.toPublisher(consensus);

        Flowable<ConsensusEvent> flowable = Flowable.fromPublisher(publisher);
        flowable.blockingStream().map(consensusComponent::getState).forEach(System.out::println);
    }
}
