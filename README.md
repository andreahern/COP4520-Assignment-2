# COP4520 Assignment 2
**Problem 1: Minotaur’s Birthday Party Simulation**

**Problem 2: Minotaur’s Crystal Vase Simulation**

- [COP4520 Assignment 2](#cop4520-assignment-2)
    - [Compile And Run](#compile-and-run)
    - [Documentation](#documentation)


### Compile And Run
To compile the Minotaur's Birthday Party Simulation just run:
```
javac problem_1/MinotaurParty.java
```

A java class file name `MinotaurParty.class` will be created that can then be executed as such:
```
java problem_1.MinotaurParty
```

Doing so will print the number of guests needed to enter the labyrinth to the console.

---

To compile the Minotaur's Crystal Vase Simulation just run:
```
javac problem_1/MinotaurVase.java
```

A java class file name `MinotaurVase.class` will be created that can then be executed as such:
```
java problem_2.MinotaurVase
```

Doing so will print the time it took for all guests to see the vase.

### Documentation
The implementation of Minotaur's Birthday Party simulation uses the efficient algorithm that has the following pseudocode:

1. If a guest enters the labyrinth and hasn't eaten a cupcake yet they eat one.
2. If the guest has already eaten a cupcake when they enter the labyrinth then they leave the cupcake there.
3. If the guest that enters is the leader and its their first time then they eat the cupcake and start counting from 1 each time they visit the labyrinth and there is a cupcake present.
4. Once the leader counts up to the number of guests in total they claim to the Minotaur that everyone has eaten a cupcake.

---

The Minotaur’s Crystal Vase Simulation uses the second strategy provided to control who enters the showroom.

This strategy of using a sign to show when the showroom is available is the most optimal at minimizing the wait and lost time of the guests.
