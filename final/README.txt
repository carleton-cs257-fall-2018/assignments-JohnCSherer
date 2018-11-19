Partners: John Sherer

Project Description: An emulation of the classic "snake" game, with players navigating their gradually-lengthening
snake without colliding with themselves and trying to collect points. The game will be grid based, with a timer
regulating when the next step occurs and the snake advances forward. A current score and high score display are located
at the top of the screen.

The game tracks keypresses individually and will only apply the first legal directional command given in a frame.
However, if a second command is given before the frame ends and the first command is applied, this second command
will be queued for the subsequent frame. If the next frame passes without any input, then the queued command is
used. This is to enable very quick button sequences to wiggle between rows and columns easily. For example, a snake
player traveling up a row might find themselves to be one unit too far to the right. To compensate, they can quickly
hit [left]>[up]. Even if both presses take place in the same frame, they will be spread out over the following two 
frames.

I have only observed one bug so far. Mashing movement keys can sometimes cause the snake to turn a complete 180.
This is supposed to be impossible, and probably is a loophole caused by the queue system described above. However,
since it only appears to occur when trying to cause it intentionally, I have decided not to spend too much time 
trying to solve it.

Due to the requirement of two view modules, the MCV design pattern is preferable. Having the relationship between
each model->view relationship will result in more compartmentalization of code and hopefully an easier read
for future edits. Distinguishing the model and and controller is also helpful, since the controller will handle
ticks to update the model, they can also give it the keypress information at the same time. This lets us
consolidate the information of when a tick occurs and which direction the player is navigating the snake into
a single operation, which will lead to a system that is both easier to test and has less moving parts overall.

Classes used: Main, Controller, View (Used for main display), ScoreView, SnakeModel