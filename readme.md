# Reversi AI :trophy: (first place out of 25 groups)

We implemented a revresi AI using a multithreaded minimax and by giving every position on the field a score.
These scores where optimized by playing thousands of games over the duration of 24 hours and keeping the best scoring AI.
We used gradient decent in order to move in smaller and smaller steps in the right direction.

Our groep finished first with our reversi AI at our local school tournament. We successfully won against 25 other groups from the Hanze University of Applied Sciences.

## Team

* [Hilko Janssen](https://github.com/hilkojj)
* [Kevin Jersak](https://github.com/Fjjersak)
* [Nick Flokstra](https://github.com/NickFlok)
* [Timo Strating](https://github.com/timostrating)

## Project structure
```
libgdx-stadium-test    - We sarted out with the idea of adding a second visualization to the project.
                         This has not went past a small test.
src
__algorithm_playground - We started with testing the minimax algorithm in python before we implemented
                         it in java
__game_util            - Util folder for games specific helpers
__GUI                  - A javafx gui
__network              - The networking side is handled here. Other parts of the code can use the
                         publicly available delegates to access to networking related events
__reversi              - The game rules for reversi and the reversi AI's are in this folder
__tic_tac_toe          - The game rules for tic tac toe and the it's AI's are in this folder
__util                 - General helpers
test                   - This folder contains a few tests to make sure core systems did not break doing
                         the development of the project
```
## Training explained


<p align="center">
  <img src="https://raw.githubusercontent.com/timostrating/reversi_ai/master/img/learning.png" alt="learning" width="700" height="400">
</p>

We use a multithreaded version of minimax with alpha beta pruning on a depth of 7. As a board evaluation we gave every position a score. We trained these scores by modifying the old scores randomly and by checking if the AI has improved. Every generation we decreased how much we would change the scores. After a few changes we would let the winners battle each other in order to keep the strongest scores from a generation.
