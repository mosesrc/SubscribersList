package com.tts.subscriberlist.subscriber;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SubscriberController {

	@Autowired
	private SubscriberRepository subscriberRepository;

	@GetMapping("/")
	public String index(Subscriber subscriber) {
		return "subscriber/index";
	}

	// Read
	@GetMapping("/allSubs")
	public String getSubscribers(Model model) {
		List<Subscriber> allSubs = (List<Subscriber>) subscriberRepository.findAll();
		model.addAttribute("subscribers", allSubs);
		return "/subscriber/subscribers";
	}

	@GetMapping("/updateSub/{id}")
	public String displayEditPage(@PathVariable(value = "id") Long id, Model model) {
		Subscriber userFound = subscriberRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("subscriber", userFound);
		return "subscriber/edit";
	}

	// Search
	@GetMapping("/searchForSub")
	public String searchForSubscriber(@RequestParam(value = "firstName", required = false) String firstName,
			Model model) {
		List<Subscriber> allSubs = (List<Subscriber>) subscriberRepository.findAll();

		for (Subscriber user : allSubs) {
			if (user.getFirstName().toLowerCase().trim().equals(firstName.toLowerCase().trim())) {
				model.addAttribute("user", user);
			}
		}
		return "subscriber/search";
	}

	// Create
	@PostMapping("/addSub")
	public String addNewSubscriber(Subscriber subscriber, Model model) {
		subscriberRepository.save(new Subscriber(subscriber.getId(), subscriber.getFirstName(),
				subscriber.getLastName(), subscriber.getUserName(), subscriber.getSignedUp()));
		model.addAttribute("firstName", subscriber.getFirstName());
		model.addAttribute("lastName", subscriber.getLastName());
		model.addAttribute("userName", subscriber.getUserName());
		return "subscriber/result";
	}

	// Update
	@PostMapping("/updateSub/{id}")
	public String updateSubscriber(@PathVariable(value = "id") Long id, @Valid Subscriber subscriber, Model model) {
		Optional<Subscriber> userFound = subscriberRepository.findById(id);
		if (userFound.isPresent()) {
			Subscriber editedUser = userFound.get();
			editedUser.setFirstName(subscriber.getFirstName());
			editedUser.setLastName(subscriber.getLastName());
			editedUser.setUserName(subscriber.getUserName());
			subscriberRepository.save(editedUser);
			model.addAttribute("subscriber", editedUser);
		}
		return getSubscribers(model);
	}

	@GetMapping("/deleteSub/{id}")
	public String deleteSubscriber(@PathVariable(value = "id") Long id, Model model) {
		Subscriber userFound = subscriberRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		subscriberRepository.delete(userFound);
		return getSubscribers(model);
	}
}
