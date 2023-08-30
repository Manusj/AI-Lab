; This is the problem defintion for the shaky domain defined
; The evironment has 4 rooms - room 1 is connected to room 2, room 2 to room 3 and room 3 to room 4
; All room connection are done by wide door for simplicity
; There is one ball each in every other room other than room 1. The box is in room 4 and all lights are off
; The task agent has is to move all balls to room 1 and switch off lights in all other rooms
(define (problem shakey_problem)
  (:domain domain_shaky)
  (:objects
    r1 r2 r3 r4 - room        ;creating 4 objects to represent each room
    sw1 sw2 sw3 sw4 - switch  ;creating 4 switch object for each room
    bx - box                  ;creating a box object
    ba1 ba2 ba3 - ball        ;creating 3 ball objects
    s - shakey                ;creating an object for shakey
    sh1 sh2 - shakey_hand     ;creating two hand objects for shakey
  )
  (:init
    ; widedoor connecting rooms
    (wide_door_connected r1 r2) (wide_door_connected r2 r1) (wide_door_connected r2 r3) (wide_door_connected r3 r2)
    (wide_door_connected r3 r4) (wide_door_connected r4 r3)
    ;keeping balls in rooms 2,3 and 4
    (ball_room ba1 r2) (ball_room ba2 r3) (ball_room ba3 r4)
    ;keeping shakey in room 1 at start settings shaky hands as free
    (shakey_room s r1) (shakey_hand_empty sh1) (shakey_hand_empty sh2)
    ;mapping switchs to rooms
    (switch_room sw1 r1) (switch_room sw2 r2) (switch_room sw3 r3) (switch_room sw4 r4)
    ;setting box in room 4
    (box_room bx r4)
  )
  (:goal
    ; putting all balls in room 1 and turing lights off in all rooms other than room 1
    (and (ball_room ba1 r1) (ball_room ba2 r1) (ball_room ba3 r1) (switch_on r1))
  )
)
