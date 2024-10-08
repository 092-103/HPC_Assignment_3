# Dining Philosophers Problem Simulation

## Overview

This project simulates the Dining Philosophers problem using multithreading in Java. Each philosopher is represented by a thread that alternates between thinking and eating. The simulation manages deadlock situations and demonstrates how philosophers can be moved to a sixth table when deadlocks occur at the initial tables.

## Problem Statement

In this simulation, there are 5 tables, each with 5 philosophers. Philosophers attempt to pick up forks (resources) to eat. If all philosophers at a table become hungry and wait indefinitely for forks, a deadlock is detected. When a deadlock occurs, the philosopher who last moved to the sixth table is identified and the program terminates.

## Features

- Each philosopher is represented by a thread.
- Deadlock detection when all philosophers at a table are waiting for forks.
- Philosophers move to a sixth table upon deadlock.
- Tracks and prints the last philosopher who caused the deadlock at the sixth table.

#   H P C _ A s s i g n m e n t _ 3  
 