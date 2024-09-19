package com.event_management.event_management_system_backend.controller;

import com.event_management.event_management_system_backend.Dto.*;
import com.event_management.event_management_system_backend.config.UserAuthenticationProvider;
import com.event_management.event_management_system_backend.mapper.AttendeeMapper;
import com.event_management.event_management_system_backend.mapper.EventMapper;
import com.event_management.event_management_system_backend.model.Attendee;
import com.event_management.event_management_system_backend.model.Event;
import com.event_management.event_management_system_backend.repositories.AttendeeRepository;
import com.event_management.event_management_system_backend.repositories.EventRepository;
import com.event_management.event_management_system_backend.services.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthController {
	@Autowired
    private AdminService adminService;
	@Autowired
    private UserAuthenticationProvider userAuthenticationProvider;
	@Autowired
    private EventRepository eventRepository;
	@Autowired
    private EventMapper eventMapper;
	@Autowired
    private AttendeeMapper attendeeMapper;
	@Autowired
    private AttendeeRepository attendeeRepository;

    @PostMapping("/login")
    public ResponseEntity<AdminDto> login(@RequestBody @Valid CredentialsDto credentialsDto){
        AdminDto adminDto = adminService.login(credentialsDto);
        adminDto.setToken(userAuthenticationProvider.createToken(adminDto.getUsername()));
        return  ResponseEntity.ok(adminDto);
    }

    @PostMapping("/register")
    public ResponseEntity<AdminDto> register(@RequestBody @Valid SignUpDto signUpDto){
        System.out.println(signUpDto);

        AdminDto newAdmin = adminService.register(signUpDto);
        newAdmin.setToken(userAuthenticationProvider.createToken(newAdmin.getUsername()));
        return ResponseEntity.created(URI.create("/admins/" + newAdmin.getId())).body(newAdmin);
    }

    @PostMapping("/addevent")
    public ResponseEntity<EventDto> addEvent(@RequestBody @Valid EventDto eventDto){
        System.out.println(eventDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Event newEvent = eventMapper.eventDtoToEvent(eventDto);
        System.out.println("new events username: "+ newEvent.getUsername());

        Event savedEvent = eventRepository.save(newEvent);
        System.out.println("saved events date: "+ savedEvent.getDate());
        return ResponseEntity.ok(eventDto);
    }

    @GetMapping("/getevent")
    public ResponseEntity<List<EventDto>> getEvents(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AdminDto adminDto = (AdminDto) authentication.getPrincipal();
        String username = adminDto.getUsername();
        //String username = authentication.getName();
        System.out.println("in get events "+ username);

        List<Event> events = eventRepository.findByUsername(username);
        if(!events.isEmpty()) {
            System.out.println(events.get(0).getDate());
        }
        List<EventDto> eventDtoList = eventMapper.listEventToDto(events);
        if(!events.isEmpty()) {
            System.out.println(eventDtoList.get(0).getDate());
        }
        return ResponseEntity.ok(eventDtoList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
        if(eventRepository.existsById(id)){
            eventRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @RequestBody EventDto updatedEventDto){
        System.out.println("Updated event: " + updatedEventDto + " prev id: " + id);
        return eventRepository.findById(id)
                .map(
                        event -> {
                            event.setName( updatedEventDto.getName());
                            event.setCity(updatedEventDto.getClass());
                            event.setCountry(updatedEventDto.getCountry());
                            event.setDate(updatedEventDto.getDate());
                            event.setDescription(updatedEventDto.getDescription());
                            event.setDate(updatedEventDto.getDate());

                            Event savedEvent = eventRepository.save(event);
                            return ResponseEntity.ok(eventMapper.eventToEventDto(savedEvent));
                        }
                )
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping("/getallevents")
    public ResponseEntity<List<EventDto>> getAllEvents(){

        List<Event> events = eventRepository.findAll();
        if(!events.isEmpty()) {
            System.out.println(events.get(0).getDate());
        }
        List<EventDto> eventDtoList = eventMapper.listEventToDto(events);
        if(!events.isEmpty()) {
            System.out.println(eventDtoList.get(0).getDate());
        }
        return ResponseEntity.ok(eventDtoList);
    }

    @PostMapping("/addattendee")
    public ResponseEntity<Attendee> addAttendee(@RequestBody @Valid Attendee attendee){
        System.out.println(attendee.getEventid());


        Attendee savedAttendee = attendeeRepository.save(attendee);
        System.out.println("saved attendee: " + savedAttendee.getEventid());
        return ResponseEntity.ok(savedAttendee);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<List<Attendee>> getAllAttendees(@PathVariable Long id){

        List<Attendee> attendees = attendeeRepository.findByEventid(id);
        if(!attendees.isEmpty()) {
            System.out.println(attendees.get(0).getEventid());
        }
        return ResponseEntity.ok(attendees);
    }

}
