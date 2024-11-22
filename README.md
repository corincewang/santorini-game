# Starting a Game #

## Set Up Backend Server ##
First, cd my-app, then run the following 2 commands:

```
mvn install
mvn exec:exec
```
in the back-end folder. This will start the Java server at http://localhost:8080.

## Set Up Frontend Server ##
Then, go back to my-app directory, and cd frontend to get into frontend directory.

```
npm install
npm start
```

This will start the front-end server at http://localhost:3000. 

You can then play the game at http://localhost:3000. 

# Game Rule #
The game is played on a 5 by 5 grid, where each grid can contain towers consisting of blocks and domes. Two players have two workers each on any field of the grid. Throughout the game, the workers move around and build towers. The first worker to make it on top of a level-3 tower wins.

In the first round, the two players each place 2 workers on different places. Players then take turns. In each turn, they select one of their workers, move this worker to an adjacent unoccupied field, and afterward add a block or dome to an unoccupied adjacent field of their new position. Locations with a worker or a dome are considered occupied. Workers can only climb a maximum of one level when moving. Domes can only be built on level-3 towers. 