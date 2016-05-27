# http://www.codeskulptor.org/#user41_RZrX2b9ZvngMPLE.py
# input will come from buttons and an input field
# all output for the game will be printed in the console

import simplegui
import random

count = 0
answer = 0
limit_range = 100
limit_count = 7

# helper function to start and restart the game
def new_game():
    # initialize global variables used in your code here
    global answer,count
    count = limit_count
    answer = random.randrange(0,limit_range)
    print "New game. Range is [0,"+str(limit_range)+")"
    print "Number of remaining guesses is",count
    print 
    
# define event handlers for control panel
def range100():
    # button that changes the range to [0,100) and starts a new game
    global limit_count,limit_range
    limit_count = 7
    limit_range = 100
    new_game()


def range1000():
    # button that changes the range to [0,1000) and starts a new game   
    global limit_count,limit_range
    limit_count = 10
    limit_range = 1000
    new_game()
    
def input_guess(guess):
    # main game logic goes here	
    global count
    count = count - 1;
    number = int(guess);
    print "Guess was",number
    print "Number of remaining guesses is",count
    if number == answer:
        print "Correct!\n"
        print "Congratulations!\n"
        new_game()
    else: 
        if number > answer:
            print "Lower!\n"
        else: 
            print "Higer!\n"
        if count == 0:
            print "You ran out of guesses.  The number was",answer
            print
            new_game()

# create frame
frame = simplegui.create_frame("Guess the number!",100,300);

# register event handlers for control elements and start frame
frame.add_button("[0,100) in 7",range100)
frame.add_button("[0,1000) in 10",range1000)
input = frame.add_input("You guess:",input_guess,50)

# call new_game 
new_game()

frame.start()
# always remember to check your completed program against the grading rubric
