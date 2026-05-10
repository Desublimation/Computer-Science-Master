Project: 0/1 Knapsack Problem (Programming Assignment 4)

Name: Yinghua Yin

--------------------------------------------------
Academic Honesty:

I have done this assignment completely on my own. I have not copied it, nor have I given my solution to anyone else. I understand that if I am involved in plagiarism or cheating, I will receive penalties according to the university policy.

--------------------------------------------------
Language:
Java

--------------------------------------------------
Environment:
This program is developed and tested in Linux environment.

--------------------------------------------------
Compilation:

javac knapsack.java

--------------------------------------------------
Execution:

1. Create a random knapsack problem:
java knapsack createkn01 knapsack01.txt

2. Solve using brute force method:
java knapsack bruteforce knapsack01.txt

3. Solve using dynamic programming (refinement algorithm):
java knapsack dynpro knapsack01.txt

4. Solve using improved greedy algorithm:
java knapsack igreedy knapsack01.txt

--------------------------------------------------
Output Files:

knapsack01.txt  → generated problem
output1.txt     → brute force result
entries2.txt    → DP computed entries
output2.txt     → DP result
output3.txt     → greedy result

--------------------------------------------------
Output Format:

<number of selected items> <total profit> <total weight>
ItemX <profit> <weight>

--------------------------------------------------
Notes:

- The dynamic programming method uses the refinement algorithm,
  computing only necessary entries instead of the full matrix.

- The greedy method implements the improved Greedy 4 algorithm.

- The brute force and dynamic programming methods produce the same optimal result.

--------------------------------------------------