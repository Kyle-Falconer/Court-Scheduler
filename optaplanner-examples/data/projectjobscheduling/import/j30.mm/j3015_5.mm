************************************************************************
file with basedata            : mf15_.bas
initial value random generator: 352815928
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  32
horizon                       :  215
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     30      0       23       28       23
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          2          15  26
   3        3          3           6  13  25
   4        3          3           5   7   8
   5        3          1          26
   6        3          2           9  10
   7        3          3          10  12  15
   8        3          3          11  12  14
   9        3          3          21  23  24
  10        3          1          22
  11        3          1          30
  12        3          2          17  25
  13        3          2          20  27
  14        3          3          18  25  26
  15        3          2          16  17
  16        3          1          22
  17        3          3          19  21  31
  18        3          3          19  20  27
  19        3          1          29
  20        3          2          21  24
  21        3          1          30
  22        3          3          23  24  27
  23        3          2          30  31
  24        3          1          29
  25        3          1          28
  26        3          1          29
  27        3          1          28
  28        3          1          31
  29        3          1          32
  30        3          1          32
  31        3          1          32
  32        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     1       9   10    0   10
         2     5       8   10    0    9
         3     9       5    9    6    0
  3      1     3      10    4    0    8
         2     4       8    4    6    0
         3     8       5    3    0    8
  4      1     3       9    9    4    0
         2     8       5    4    0    7
         3     8       4    3    3    0
  5      1     1       3    6    0    5
         2     6       3    5    6    0
         3     9       3    5    3    0
  6      1     4       6    6    0    7
         2     7       5    3    0    6
         3     8       4    1    0    6
  7      1     2       5    7    0    7
         2     3       5    7    0    6
         3     6       3    4    9    0
  8      1     5       6    4    3    0
         2     6       4    4    2    0
         3     7       2    3    0    5
  9      1     1       7    8    0    5
         2     5       5    7    4    0
         3     6       1    7    4    0
 10      1     1       6    6    5    0
         2     2       5    5    0    6
         3     7       4    3    0    6
 11      1     1       7    8    0    6
         2     3       5    8    4    0
         3     6       3    7    0    2
 12      1     1      10    7   10    0
         2     6       9    5   10    0
         3    10       9    5    0    8
 13      1     1       8    6    9    0
         2     5       5    5    3    0
         3    10       4    3    0    3
 14      1     5       9    8    9    0
         2     8       7    8    0    8
         3    10       6    7    0    7
 15      1     3       9    5    4    0
         2     4       7    3    2    0
         3     5       5    2    0    6
 16      1     2       3    9    1    0
         2     4       3    7    1    0
         3     6       3    6    1    0
 17      1     4       5    5    4    0
         2     5       4    4    0    6
         3     6       3    3    4    0
 18      1     5       8    6    9    0
         2     6       7    5    9    0
         3     9       6    5    6    0
 19      1     2       5    7    7    0
         2     7       5    6    0    7
         3     8       3    4    0    7
 20      1     1       4    9    8    0
         2     4       2    8    0    2
         3    10       1    8    0    1
 21      1     2       9    7    0    5
         2     6       7    7    6    0
         3    10       5    6    5    0
 22      1     1       6    4    0    9
         2     2       6    4    5    0
         3     5       6    4    0    3
 23      1     2       6    3    0    9
         2     5       6    3    2    0
         3     6       6    2    0    9
 24      1     2       5    5    6    0
         2     3       3    5    4    0
         3     5       1    4    4    0
 25      1     6       9    6    7    0
         2     8       9    3    0    4
         3     8       8    4    2    0
 26      1     4       8    2    5    0
         2     4       8    2    0    4
         3     6       2    1    0    4
 27      1     1       6    9    0    6
         2     7       4    8    0    3
         3     8       2    6    4    0
 28      1     1       6    5    2    0
         2     1       4    7    0    8
         3     1       8    5    0    8
 29      1     2       4    7    5    0
         2     4       3    6    3    0
         3     8       3    5    0    7
 30      1     1       5    8    0    6
         2     2       4    5    0    5
         3     5       2    3    3    0
 31      1     2       8    7    0   10
         2     4       6    5    0    9
         3     5       6    4    4    0
 32      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   24   26   84   90
************************************************************************
