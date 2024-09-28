public class Main {

    public static void main(String[] args) {
        final int numTables = 5;                // Total number of tables (5 tables in total)
        final int philosophersPerTable = 5;     // Number of philosophers per table (5 philosophers per table)
        Table[] tables = new Table[numTables];  // Create an array of tables

        // Initialize the 5 tables with philosophers and forks
        for (int i = 0; i < numTables; i++) {
            // Create a new Table object for each table
            tables[i] = new Table(philosophersPerTable);

            // Initialize the philosophers and forks at each table
            for (int j = 0; j < philosophersPerTable; j++) {
                // Create a unique name for each philosopher, starting from 'A'
                String philosopherName = Character.toString((char) ('A' + (i * philosophersPerTable) + j));

                // Get the left fork (current fork) for the philosopher
                Fork leftFork = tables[i].getFork(j);

                // Get the right fork (next fork) for the philosopher (circular arrangement)
                Fork rightFork = tables[i].getFork((j + 1) % philosophersPerTable);

                // Create a new philosopher, passing the name, left fork, right fork, and associated table
                Philosopher philosopher = new Philosopher(philosopherName, leftFork, rightFork, tables[i]);

                // Add the philosopher to the table
                tables[i].addPhilosopher(philosopher);
            }
        }

        // Start the dining simulation for each table
        for (Table table : tables) {
            // Start dining at the table, which runs the philosopher threads
            table.startDining();
        }
    }
}
