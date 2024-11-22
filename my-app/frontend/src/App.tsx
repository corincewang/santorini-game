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
      cells: [], 
      currentPlayer: 'Player 1',
      winner: null,
      action: 'place', 
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
  
      if (json.error) {
        alert(json.error);
      } else {
        const updatedCells = json.gameState?.cells || [];
  
        const mergedCells = this.state.cells.map((cell) => {
          const updatedCell = updatedCells.find(
            (updated) => updated.x === cell.x && updated.y === cell.y
          );
          return updatedCell || cell;
        });
  
        this.setState({
          cells: mergedCells,
          currentPlayer: json.gameState?.currentPlayer || 'Player 1',
        });
  
        if (json.gameState?.action === 'chooseWorker') {
          this.setState({ action: 'chooseWorker' }); 
        }
      }
    } catch (error) {
      console.error('Failed to place worker:', error);
    }
  };
  
  // 选择工人
  chooseWorker = async (x, y) => {
    try {
      const response = await fetch(`/play?action=chooseWorker&x=${x}&y=${y}`);
      const json = await response.json();
      this.setState({ ...json, action: 'move' });  // 选择后切换到移动动作
    } catch (error) {
        console.error("Error chooseing worker:", error);
    }
  };
  
  moveWorker = async (x: number, y: number): Promise<void> => {
    try {
      const response = await fetch(`/play?action=move&x=${x}&y=${y}`);
      const json = await response.json();
  
      if (json.error) {
        alert(json.error);
      } else {
        const updatedCells = json.gameState?.cells || [];
  
        const mergedCells = this.state.cells.map((cell) => {
          const updatedCell = updatedCells.find(
            (updated) => updated.x === cell.x && updated.y === cell.y
          );
          return updatedCell || cell;
        });
  
        this.setState({
          cells: mergedCells,
          currentPlayer: json.gameState?.currentPlayer || 'Player 1',
        });
      }
    } catch (error) {
      console.error('Failed to move worker:', error);
    }
  };
  

  handleCellClick = (cell: Cell) => {
    const { action, currentPlayer } = this.state;
    console.log("current action: ", action)
    console.log("cell.player: ", cell.player)
    console.log("currentPlayer: ", currentPlayer)

    if (action === 'chooseWorker' && cell.player === currentPlayer) {
        this.chooseWorker(cell.x, cell.y);
        
    }
    // 检查是否是放置工人的阶段并且该格子没有工人
    else if (action === 'place' && !cell.worker) {
        this.placeWorker(cell.x, cell.y);
    }
    // 检查是否是移动工人的阶段并且该格子是可以移动到的
    else if (action === 'move' && cell.playable) {
        this.moveWorker(cell.x, cell.y);
    }
    // 其他情况输出日志
    else {
        console.log(`Action ${action} not permitted on cell (${cell.x}, ${cell.y}).`);
    }
};
  

  createCell = (cell: Cell, index: number): React.ReactNode => {
    let cellClass = '';
    if (cell.player === 'Player1') {
      cellClass = 'player-one';
    } else if (cell.player === 'Player2') {
      cellClass = 'player-two';
    } else if (cell.playable) {
      cellClass = 'playable';
    }
  
    return (
      <div
        key={index}
        className={`board-cell ${cellClass}`}
        onClick={() => this.handleCellClick(cell)}
      >
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

        </div>
      </div>
    );
  }
  
}

export default App;
