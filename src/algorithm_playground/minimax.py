""" https://github.com/Cledersonbc/tic-tac-toe-minimax/blob/master/py_version/minimax.py """

from math import inf as infinity

board = [ [-1,1,-1], [0,0,0], [0,1,0], ]
# board = [
#     [-1, -1, 1],
#     [ 1,  0, 0],
#     [ 0, -1, 1],
# ]

MAX = +1
MIN = -1

PLAYER_1 = 1
PLAYER_2 = -1


def evaluate(board_state):
    nr_of_1s = 0
    nr_of_2s = 0

    for x, row in enumerate(board_state):
        for y, cell in enumerate(row):
            if cell == PLAYER_1:
                nr_of_1s += 1
            elif cell == PLAYER_2:
                nr_of_2s += 1

    if is_winner(board_state, PLAYER_1):
        return +1 / nr_of_1s
    if is_winner(board_state, PLAYER_2):
        return -1 / nr_of_2s
    return 0


def is_winner(board_state, player):
    win_board_state = [
        [board_state[0][0], board_state[0][1], board_state[0][2]], [board_state[1][0], board_state[1][1], board_state[1][2]], [board_state[2][0], board_state[2][1], board_state[2][2]],
        [board_state[0][0], board_state[1][0], board_state[2][0]], [board_state[0][1], board_state[1][1], board_state[2][1]], [board_state[0][2], board_state[1][2], board_state[2][2]],
        [board_state[0][0], board_state[1][1], board_state[2][2]], [board_state[2][0], board_state[1][1], board_state[0][2]],
    ]
    return [player, player, player] in win_board_state
        

def game_over(board_state):
    return is_winner(board_state, PLAYER_1) or is_winner(board_state, PLAYER_2)


def empty_cells(board_state):
    cells = []
    
    for y in range(0, len(board_state)):
        for x in range(0, len(board_state)):
            if board_state[x][y] == 0:
                cells.append([x, y])

    for x, row in enumerate(board_state):
        for y, cell in enumerate(row):
            if cell == 0:
                cells.append([x, y])
    return cells


def valid_move(x, y):
    return [x, y] in empty_cells(board)


def print_board(board):
    for row in board:
        print(row)
    print("")

def minimax(board, depth, alpha, beta, player):
    """ https://github.com/Cledersonbc/tic-tac-toe-minimax/blob/master/py_version/minimax.py """
    if player == MAX:
        best = [-1, -1, -infinity]
    else:
        best = [-1, -1, +infinity]

    if depth == 0 or game_over(board):
        score = evaluate(board)
        return [-1, -1, score]

    for cell in empty_cells(board):
        x, y = cell[0], cell[1]
        board[x][y] = player
        # print_board(board)
        score = minimax(board, depth - 1, alpha, beta, -player)
        board[x][y] = 0
        score[0], score[1] = x, y

        evaluation = score[2]

        if player == MAX:
            if evaluation > best[2]:
                best = score

            alpha = max(alpha, evaluation)
            if beta <= alpha:
                break
        else:
            if evaluation < best[2]:
                best = score

            beta = min(beta, evaluation)
            if beta <= alpha:
                break

    return best


depth = 999  # len(empty_cells(board))
score = minimax(board, depth, -infinity, +infinity, PLAYER_1)
print(f"(x = {score[0]}, y = {score[1]}) score = {score[2]}")
