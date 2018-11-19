Partners: John Sherer

Project Description: An emulation of the classic "snake" game, with players navigating their gradually-lengthening
snake without colliding with themselves and trying to collect points. The game will be grid based, with a timer
regulating when the next step occurs and the snake advances forward. A current score and high score display will 
appear at the top of the screen.

Due to the requirement of two view modules, the MCV design pattern is preferable. Having the relationship between
each model->view relationship will result in more compartmentalization of code and hopefully an easier read
for future edits. Distinguishing the model and and controller is also helpful, since the controller will handle
ticks to update the model, they can also give it the keypress information at the same time. This lets us
consolidate the information of when a tick occurs and which direction the player is navigating the snake into
a single operation, which will lead to a system that is both easier to test and has less moving parts overall.

Classes used: Main, Controller, View (Used for main display), ScoreView, SnakeModel