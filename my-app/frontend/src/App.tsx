import React from 'react';
import './App.css';
import { GameState, Cell } from './Game';
import BoardCell from './Cell';

interface Props { }

class App extends React.Component<Props, GameState> {
  private initialized: boolean = false;

  constructor(props: Props) {
    super(props);
    this.state = {
      cells: [],
      currentPlayer: 'Player 1',
      winner: null
    };
  }

  newGame = async () => {
    const response = await fetch('/newgame'); // Fetches a new game from the backend
    const json = await response.json();
    this.setState({
      cells: json['cells'],
      currentPlayer: json['currentPlayer'],
      winner: null,
    });
  };

  placeWorker = async (x: number, y: number): Promise<void> => {
    const response = await fetch(`/placeworker?x=${x}&y=${y}`);
    const json = await response.json();
    this.setState({ cells: json['cells'] });
  };

  moveWorker = async (x: number, y: number): Promise<void> => {
    const response = await fetch(`/move?x=${x}&y=${y}`);
    const json = await response.json();
    this.setState({ cells: json['cells'] });
  };

  buildBlock = async (x: number, y: number): Promise<void> => {
    const response = await fetch(`/build?x=${x}&y=${y}`);
    const json = await response.json();
    this.setState({ cells: json['cells'] });
  };

  switchTurn = async (): Promise<void> => {
    const response = await fetch('/switchturn');
    const json = await response.json();
    this.setState({ currentPlayer: json['currentPlayer'] });
  };

  checkWin = async (): Promise<void> => {
    const response = await fetch('/checkwin');
    const json = await response.json();
    if (json['winner']) {
      this.setState({ winner: json['winner'] });
    }
  };

  playAction = (action: string, x: number, y: number): React.MouseEventHandler => {
    return async (e) => {
      e.preventDefault();
      switch (action) {
        case 'place':
          await this.placeWorker(x, y);
          break;
        case 'move':
          await this.moveWorker(x, y);
          break;
        case 'build':
          await this.buildBlock(x, y);
          break;
      }
      await this.checkWin();
      if (!this.state.winner) {
        await this.switchTurn();
      }
    };
  };

  createCell = (cell: Cell, index: number): React.ReactNode => {
    if (cell.playable) {
      return (
        <div key={index}>
          <a href="/" onClick={this.playAction(cell.action || 'place', cell.x, cell.y)}>
            <BoardCell cell={cell} />
          </a>
        </div>
      );
    } else {
      return <div key={index}><BoardCell cell={cell} /></div>;
    }
  };

  componentDidMount(): void {
    if (!this.initialized) {
      this.newGame();
      this.initialized = true;
    }
  }

  render(): React.ReactNode {
    return (
      <div>
        <h1>Santorini Game</h1>
        {this.state.winner ? (
          <h2>{this.state.winner} Wins!</h2>
        ) : (
          <h2>Current Turn: {this.state.currentPlayer}</h2>
        )}
        <div id="board">
          {this.state.cells.map((cell, i) => this.createCell(cell, i))}
        </div>
        <div id="bottombar">
          <button onClick={this.newGame}>New Game</button>
          <button>Undo</button>
        </div>
      </div>
    );
  }
}

export default App;
