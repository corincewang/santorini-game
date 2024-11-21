import React from 'react';
import './App.css';
import { GameState, Cell } from './Game';
import BoardCell from './Cell.tsx';

interface Props {}

class App extends React.Component<Props, GameState> {
  private initialized: boolean = false;

  constructor(props: Props) {
    super(props);
    this.state = {
      cells: [], // 初始化为一个空数组
      currentPlayer: 'Player 1',
      winner: null,
    };
  }

  newGame = async () => {
    try {
      const response = await fetch('/newgame');
      const json = await response.json();
      console.log('New Game Response:', json);
      this.setState({
        cells: json.gameState?.cells || [],
        currentPlayer: json.gameState?.currentPlayer || 'Player 1',
        winner: null,
      });
    } catch (error) {
      console.error('Failed to start a new game:', error);
    }
  };

  placeWorker = async (x: number, y: number): Promise<void> => {
    try {
      const response = await fetch(`/play?action=place&x=${x}&y=${y}`);
      const json = await response.json();
      console.log('Worker placed at:', x, y, 'Response:', json);

      this.setState({
        cells: json.gameState?.cells || [],
        currentPlayer: json.gameState?.currentPlayer || 'Player 1',
      });
    } catch (error) {
      console.error('Failed to place worker:', error);
    }
  };

  handleCellClick = (cell: Cell) => {
    if (cell.playable) {
      this.placeWorker(cell.x, cell.y);
    } else {
      console.log(`Cell (${cell.x}, ${cell.y}) is not playable.`);
    }
  };

  createCell = (cell: Cell, index: number): React.ReactNode => {
    return (
      <div key={index} onClick={() => this.handleCellClick(cell)}>
        <BoardCell cell={cell} />
      </div>
    );
  };

  componentDidMount(): void {
    if (!this.initialized) {
      this.newGame();
      this.initialized = true;
    }
  }

  render(): React.ReactNode {
    const cells = this.state.cells || [];
    return (
      <div>
        <h1>Santorini Game</h1>
        {this.state.winner ? (
          <h2>{this.state.winner} Wins!</h2>
        ) : (
          <h2>Current Turn: {this.state.currentPlayer}</h2>
        )}
        <div id="board">
          {cells.map((cell, i) => this.createCell(cell, i))}
        </div>
        <div id="bottombar">
          <button onClick={this.newGame}>New Game</button>
          <button>Undo</button>
        </div>
        <div id="actions">
          <button onClick={() => alert('Place Worker Mode Activated')}>Place Worker</button>
        </div>
      </div>
    );
  }
}

export default App;
