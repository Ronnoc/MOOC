# http://www.codeskulptor.org/#user41_Y07hQM2MQlPUDYi.py
import simplegui
import random
# define global variables
timenow = 0
isRunning = False
x=0
y=0
# define helper function format that converts time
# in tenths of seconds into formatted string A:BC.D
def format(t):
    D=t%10
    t/=10
    C=t%10
    t/=10
    B=t%6
    t/=6
    A=t
    return str(A)+":"+str(B)+str(C)+"."+str(D)
    
# define event handlers for buttons; "Start", "Stop", "Reset"
def Start():
    global isRunning
    isRunning=True

def Stop():
    global isRunning,x,y
    if isRunning :
        y+=1
        if timenow%10==0 :
            x+=1
    isRunning=False

def Reset():
    global timenow,isRunning,x,y
    timenow=0
    isRunning=False
    x=0
    y=0

# define event handler for timer with 0.1 sec interval
def timer_handler():
    global timenow
    if isRunning :
        timenow+=1
 
# define draw handler
def draw_handler(canvas):
    canvas.draw_text(format(timenow),[80,120],64,"White")
    canvas.draw_text(str(x)+"/"+str(y),[240,20],24,"White")
   
# create frame
frame = simplegui.create_frame("Stopwatch: The Game", 300, 200)

# register event handlers
frame.set_draw_handler(draw_handler)
timer = simplegui.create_timer(100,timer_handler)
frame.add_button("Start", Start)
frame.add_button("Stop", Stop)
frame.add_button("Reset", Reset)

# start frame
frame.start()
timer.start()
# Please remember to review the grading rubric
