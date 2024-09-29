Questions/Topics for discussion:



Symbols in matrix probability
1) Probabilities of bonus symbols indicated for whole matrix. Does it mean that for example probability 3 for symbol A per specific cell will be 3/matrix size(column*rows)?
If I understand requirements correctly probability of bonus symbol per cell will be calculated in this way: probability of bonus cell divided by matrix size.
Example:
Having matrix 2x2 and Symbols:
```json
"standard_symbols": [
      {
        "column": 0,
        "row": 0,
        "symbols": {
            "A": 1,
            "B": 2
        }
      },
      {
        "column": 0,
        "row": 1,
        "symbols": {
            "A": 1,
            "B": 2
        }
      },
      {
        "column": 1,
        "row": 0,
        "symbols": {
            "A": 3,
            "B": 7
        }
      },
      {
        "column": 1,
        "row": 1,
        "symbols": {
            "A": 3,
            "B": 7
        }
      }
    ],
    "bonus_symbols": {
      "symbols": {
        "10x": 1,
        "+1000": 3,
        "MISS": 5
      }
    }
```

Probability of all symbols per individual cell will be(instead of divide bonus symbols probability by matrix size I multiply standard symbols probability by matrix size):
Cell    Probability per Symbol
[0:0] = [(10x=1), (+1000=3), (MISS=5). (A=4), (B=8)]
[0:1] = [(10x=1), (+1000=3), (MISS=5). (A=4), (B=8)]
[1:0] = [(10x=1), (+1000=3), (MISS=5). (A=12), (B=28)]
[1:1] = [(10x=1), (+1000=3), (MISS=5). (A=12), (B=28)]

If my undersatnding requirements incorrect, pls let me know I detail I will adjust logic

2) What behaviour has to be if there were missed symbols probability configuration for one or more cells in matrix
In configuration file indicated matrix 4x4 size. But configuration provides symbols probability for standard symbols just for 3x3 matrix size(missed 4th column and row details).
   I will set matrix 3x3 size. And will provide config file validation fail if there are missed probabilities for specific cells of matrix.
3) What behaviour has to be if there were no bonus probability configuration provided
4) Regarding configuration structure: probably "win_combinations" details are not best place to store inside "probability". Not sure if they one correspond to another.


Winning Symbols Combinations topic
1) Provided configuration design does not limit length of symbols line by length of matrix. In another word covered area allows to have different length combinations:
Example:

Matrix size
```json
"columns": 3, 
"rows": 3, 
```      

But can be
```json
"covered_areas": [
    ["0:0"],
    ["0:1", "1:1"],
    ["0:2", "1:2", "2:2", "3:2"]
    ]
```

either we need make more strict design of config file or agreed how logic will behave in this way(not enough points or having more than matrix length)

2) How to behave if covered are provided as empty matrix, to succeed or fail combination per symbol?
3) Do we have to check for left/right diagonally linear win combinations that covered areas suits strategy? Otherwise they can be easy misconfigured now.
Examples:
```json
  "same_symbols_diagonally_left_to_right": { 
    ...
    "group": "ltr_diagonally_linear_symbols",
    "covered_areas": [
    ["0:2", "1:1", "2:0"]
    ]
    },
    "same_symbols_diagonally_right_to_left": { 
    ...
    "group": "rtl_diagonally_linear_symbols",
    "covered_areas": [
     ["0:0", "1:1", "2:2"]
    ]
    }
```

Other topics:
1) Regarding configuration structure: is "win_combinations" details should be under "probability" details? 