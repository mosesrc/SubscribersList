package com.tts.subscriberlist.subscriber;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SubscriberRepository extends CrudRepository<Subscriber, Long> {
	List<Subscriber> findByFirstName(String firstName);
}
