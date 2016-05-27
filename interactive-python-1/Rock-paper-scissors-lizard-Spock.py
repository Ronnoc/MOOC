# http://www.codeskulptor.org/#user41_cmJLSAug0Cook7r.py
# name_to_number返回了-1而rpsls没有抛异常

# The key idea of this program is to equate the strings
# "rock", "paper", "scissors", "lizard", "Spock" to numbers
# as follows:
#
# 0 - rock
# 1 - Spock
# 2 - paper
# 3 - lizard
# 4 - scissors

# helper functions
List = ["rock","Spock","paper","lizard","scissors"]
def name_to_number(name):
    # convert name to number using if/elif/else
    for i in range(0,5):
        if List[i]==name:
            return i
    # don't forget to return the result!
    print "Fail to convert"
    return -1

def number_to_name(number):
    # convert number to a name using if/elif/else
    if(number>=0 and number <5):
        return List[number]
    # don't forget to return the result!
    print "Fail to convert"
    
import random

def rpsls(player_choice): 
    
    # print a blank line to separate consecutive games
    print ""
    # print out the message for the player's choice
    print "Player chooses " + player_choice
    # convert the player's choice to player_number using the function name_to_number()
    player_number = name_to_number(player_choice)
    # compute random guess for comp_number using random.randrange()
    comp_number=random.randrange(0,5)
    # convert comp_number to comp_choice using the function number_to_name()
    comp_choice=number_to_name(comp_number)
    # print out the message for computer's choice
    print "Computer chooses " + comp_choice
    # compute difference of comp_number and player_number modulo five
    dif = (comp_number - player_number) % 5
    # use if/elif/else to determine winner, print winner message
    if dif == 0:
        print "Player and computer tie!"
    elif dif < 3:
        print "Computer wins!"
    else:
        print "Player wins!"
    
# test your code - THESE CALLS MUST BE PRESENT IN YOUR SUBMITTED CODE
rpsls("rock")
rpsls("Spock")
rpsls("paper")
rpsls("lizard")
rpsls("scissors")

# always remember to check your completed program against the grading rubric


