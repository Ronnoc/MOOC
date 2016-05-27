# http://www.codeskulptor.org/#user41_lvIh8Wpm3YCTwmq.py
# Implementation of classic arcade game Pong

import simplegui
import random

# initialize globals - pos and vel encode vertical info for paddles
WIDTH = 600
HEIGHT = 400       
BALL_RADIUS = 20
PAD_WIDTH = 8
PAD_HEIGHT = 80
HALF_PAD_WIDTH = PAD_WIDTH / 2
HALF_PAD_HEIGHT = PAD_HEIGHT / 2
BALL_LEFT = PAD_WIDTH+BALL_RADIUS
BALL_RIGHT = WIDTH-PAD_WIDTH-BALL_RADIUS
LEFT = False
RIGHT = True
ball_pos=[WIDTH/2,HEIGHT/2]
ball_vel=[0,0]
paddle1_pos=HEIGHT/2
paddle2_pos=HEIGHT/2
paddle1_vel=0
paddle2_vel=0

# initialize ball_pos and ball_vel for new bal in middle of table
# if direction is RIGHT, the ball's velocity is upper right, else upper left
def spawn_ball(direction):
    global ball_pos, ball_vel # these are vectors stored as lists
    ball_pos=[300,200]
    if direction == LEFT :
        ball_vel=[-random.randrange(4,7),-random.randrange(1,3)]
    else :
        ball_vel=[random.randrange(4,7),-random.randrange(1,3)]

# define event handlers

def new_game():
    global paddle1_pos, paddle2_pos, paddle1_vel, paddle2_vel  # these are numbers
    global score1, score2  # these are ints
    score1=0
    score2=0
    paddle1_pos=HEIGHT/2
    paddle2_pos=HEIGHT/2
    paddle1_vel=0
    paddle2_vel=0
    if random.randint(0,1)>0 :
        spawn_ball(LEFT)
    else :
        spawn_ball(RIGHT)

def in_screen(paddle_pos):
    result=max(paddle_pos,0+HALF_PAD_HEIGHT)
    result=min(result,HEIGHT-HALF_PAD_HEIGHT)
    return result
    
def draw(canvas):
    global score1, score2, paddle1_pos, paddle2_pos, ball_pos, ball_vel
 
    # draw mid line and gutters
    canvas.draw_line([WIDTH / 2, 0],[WIDTH / 2, HEIGHT], 1, "White")
    canvas.draw_line([PAD_WIDTH, 0],[PAD_WIDTH, HEIGHT], 1, "White")
    canvas.draw_line([WIDTH - PAD_WIDTH, 0],[WIDTH - PAD_WIDTH, HEIGHT], 1, "White")
    # update paddle's vertical position, keep paddle on the screen
    paddle1_pos+=paddle1_vel
    paddle1_pos=in_screen(paddle1_pos)
    paddle2_pos+=paddle2_vel
    paddle2_pos=in_screen(paddle2_pos)
    # determine whether paddle and ball collide && update ball
    ball_pos_next=[ball_pos[0]+ball_vel[0],ball_pos[1]+ball_vel[1]]
    score_change=False
    if ball_pos_next[0] < BALL_LEFT :
        time=(1.*ball_pos[0]-BALL_LEFT)/-ball_vel[0]
        y=ball_pos[1]+time*ball_vel[1]
        if y >= paddle1_pos - HALF_PAD_HEIGHT and y <= paddle1_pos + HALF_PAD_HEIGHT :
            ball_pos_next[0]=2*BALL_LEFT- ball_pos_next[0]
            ball_vel[0]*=-1.1
            ball_vel[1]*=1.1
        else :
            score2+=1
            score_change=True
            spawn_ball(RIGHT)
    elif ball_pos_next[0] > BALL_RIGHT :
        time=(BALL_RIGHT-1.*ball_pos[0])/ball_vel[0]
        y=ball_pos[1]+time*ball_vel[1]
        if y >= paddle2_pos - HALF_PAD_HEIGHT and y <= paddle2_pos + HALF_PAD_HEIGHT :
            ball_pos_next[0]=2*BALL_RIGHT- ball_pos_next[0]
            ball_vel[0]*=-1.1
            ball_vel[1]*=1.1
        else :
            score1+=1
            score_change=True
            spawn_ball(LEFT)
    if not score_change :
        ball_pos=ball_pos_next;
        if ball_pos[1] < BALL_RADIUS:
            ball_pos[1] = 2*BALL_RADIUS - ball_pos[1]
            ball_vel[1] *=-1
        if ball_pos[1] > HEIGHT - BALL_RADIUS :
            ball_pos[1] = 2*(HEIGHT - BALL_RADIUS) - ball_pos[1]
            ball_vel[1] *=-1
    # draw ball
    for i in range(1,BALL_RADIUS):
        canvas.draw_circle(ball_pos,i,2,"GREY")
    # draw paddles
    canvas.draw_line([HALF_PAD_WIDTH,paddle1_pos-HALF_PAD_HEIGHT],
                     [HALF_PAD_WIDTH,paddle1_pos+HALF_PAD_HEIGHT],PAD_WIDTH,"RED");
    canvas.draw_line([WIDTH-HALF_PAD_WIDTH,paddle2_pos-HALF_PAD_HEIGHT],
                     [WIDTH-HALF_PAD_WIDTH,paddle2_pos+HALF_PAD_HEIGHT],PAD_WIDTH,"BLUE");
    # draw scores
    canvas.draw_text(str(score1),[WIDTH/2-PAD_HEIGHT,PAD_HEIGHT],40,"RED");
    canvas.draw_text(str(score2),[WIDTH/2+PAD_HEIGHT,PAD_HEIGHT],40,"BLUE");
    
def keydown(key):
    global paddle1_vel, paddle2_vel
    if key==simplegui.KEY_MAP['s'] :
        paddle1_vel=5
    if key==simplegui.KEY_MAP['w'] :
        paddle1_vel=-5
    if key==simplegui.KEY_MAP['down'] :
        paddle2_vel=5
    if key==simplegui.KEY_MAP['up'] :
        paddle2_vel=-5

def keyup(key):
    global paddle1_vel, paddle2_vel
    if key==simplegui.KEY_MAP['s'] :
        paddle1_vel=0
    if key==simplegui.KEY_MAP['w'] :
        paddle1_vel=0
    if key==simplegui.KEY_MAP['down'] :
        paddle2_vel=0
    if key==simplegui.KEY_MAP['up'] :
        paddle2_vel=0


# create frame
frame = simplegui.create_frame("Pong", WIDTH, HEIGHT)
frame.set_draw_handler(draw)
frame.set_keydown_handler(keydown)
frame.set_keyup_handler(keyup)
frame.add_button("Restart",new_game)

# start frame
new_game()
frame.start()
