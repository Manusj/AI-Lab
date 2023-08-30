; Domain Definition for shakey robot

(define (domain domain_shaky)
  (:requirements :strips :typing)
  (:types
    shakey        ;the shakey robot
    shakey_hand   ;denotes the hand of shakey robot
    room          ;denotes a room
    ball          ;denotes a ball object in a room
    box           ;denotes the box in the room
    switch        ;denotes a light switch
  )
  (:predicates
    (connected_room ?r1 ?r2 - room)       ;denotes if two rooms are connected by a door
    (wide_door_connected ?r1 ?r2 - room)  ;denotes a wide wide_door
    (shakey_room ?s - shakey ?r - room)   ;denotes shakey position
    (ball_room ?b - ball ?r - room)       ;denotes ball position
    (box_room ?b - box ?r - room)         ;denotes box position
    (switch_room ?s - switch ?r - room)   ;denotes switch is on
    (switch_on ?r - room)                 ;denotes if light is on
    (hold ?h - shakey_hand ?b - ball)     ;denotes ball in shakeys hands
    (shakey_hand_empty ?h - shakey_hand)  ;denotes if shakey hands are empty
  )

  ;action for shakey to move from one room to another
  ;preconditions - shakey must be in the room mentioned from and "from" must be connected to "to"
  ;effect - Shakey's room changes
  (:action move_shakey
    :parameters (?s - shakey ?from ?to - room)
    :precondition (and (shakey_room ?s ?from) (or (connected_room ?to ?from) (wide_door_connected ?from ?to)))
    :effect (and (shakey_room ?s ?to) (not (shakey_room ?s ?from)))
  )

  ;action for shakey to grab an ball
  ;preconditions - shakey must be in the room correct room and ball must be in same room, shakey had must be free and light must be on
  ;effect - Shakey's grabs ball, balls get remoeved from room and moves to shakeys hand
  (:action grab_ball
    :parameters (?s - shakey ?h - shakey_hand ?r - room ?b - ball)
    :precondition (and (shakey_room ?s ?r) (shakey_hand_empty ?h) (ball_room ?b ?r) (switch_on ?r))
    :effect (and (hold ?h ?b) (not (shakey_hand_empty ?h)) (not (ball_room ?b ?r)))
  )

  ;action for shakey to put down an ball
  ;preconditions - shakey must be in the room correct room and shakey must be holding the ball
  ;effect - Shakey's puts down ball in room and hands become free
  (:action putdown_ball
    :parameters (?s - shakey ?h - shakey_hand ?r - room ?b - ball)
    :precondition (and (shakey_room ?s ?r) (hold ?h ?b))
    :effect (and (shakey_hand_empty ?h) (ball_room ?b ?r) (not (hold ?h ?b)))
  )

  ;action for shakey to move box from one room to another
  ;preconditions - shakey must be in the room correct room, box must in same room and from - to rooms must be wide door connected
  ;effect - Shakey moves box - box and shakeys room updated
  (:action move_box
    :parameters (?s - shakey ?b - box ?from ?to - room)
    :precondition (and (shakey_room ?s ?from) (box_room ?b ?from) (wide_door_connected ?from ?to))
    :effect (and (shakey_room ?s ?to) (box_room ?b ?to) (not (box_room ?b ?from)) (not (shakey_room ?s ?from)))
  )

  ;action for turing on light switch
  ;preconditions - shakey must be in the room correct room and correct switch must selected and box must in same room
  ;               and light is not already on and shakey hand must be free
  ;effect - Shakey turns on light
  (:action switch_on
    :parameters (?s - shakey ?sw - switch ?r - room ?b - box ?sh - shakey_hand)
    :precondition (and (shakey_room ?s ?r) (switch_room ?sw ?r) (box_room ?b ?r) (shakey_hand_empty ?sh) (not (switch_on ?r)))
    :effect (and (switch_on ?r))
  )

  ;action for turing off light switch
  ;preconditions - shakey must be in the room correct room and correct switch must selected and box must in same room
  ;                and light is off in room and shakeys hand must be free
  ;effect - Shakey turns off light
  (:action switch_off
    :parameters (?s - shakey ?sw - switch ?r - room ?b - box ?sh - shakey_hand)
    :precondition (and (shakey_room ?s ?r) (switch_room ?sw ?r) (box_room ?b ?r) (switch_on ?r) (shakey_hand_empty ?sh))
    :effect (not (switch_on ?r))
  )
)
