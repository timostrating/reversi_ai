# Reversi AI

We implemented a revresi AI using a multithreaded minimax and by giving every posistion on the field a score.
These scores where optimized by playing thousands of games over the duration of 24 hours and keeping the best scoring AI.
We used gradiant decent in order to move in smaller and smaller steps in the right direction.

## Team

* [Hilko Janssen](https://github.com/hilkojj)
* [Kevin Jersak](https://github.com/Fjjersak)
* [Nick Flokstra](https://github.com/NickFlok)
* [Timo Strating](https://github.com/timostrating)

## Project structure
```
libgdx-stadium-test     - We sarted out with the idea of adding a second visualisation to the project. This has not went past a small test.
src
__algorithm_playground  - We started with testing the minimax algorithm in python before we implemented it in java
__game_util             - Util folder for games specific helpers
__GUI                   - A javafx gui
__network               - The networking side is handeld here. Other parts of the code can use the publicly avalible delegates to acces to netwoking related events
__reversi               - The game rules for reversi and the reversi AI's are in this folder
__tic_tac_toe           - The game rules for tic tac toe and the it's AI's are in this folder
__util                  - General helpers
test                    - This folder contains a few test to make sure core systems did not break doing the development of the project
```
## Training explained

TODO
