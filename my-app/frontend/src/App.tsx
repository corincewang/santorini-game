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
      showWinner: false,
      godCards: {},
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
            this.updateGameStateFromResponse(json);
        }
    } catch (error) {
        console.error('Failed to place worker:', error);
    }
  };

  updateGameStateFromResponse = (json) => {
    const updatedCells = json.gameState?.cells || [];

    const mergedCells = this.state.cells.map((cell) => {
        const updatedCell = updatedCells.find(
            (updated) => updated.x === cell.x && updated.y === cell.y
        );
        return updatedCell || cell;
    });

    this.setState({
        cells: mergedCells,
        currentPlayer: json.gameState?.currentPlayer,
    }, () => {
        console.log("Updated state after placing workers:", this.state);
    });

    // Check if it's time to choose a worker, which typically follows placing
    if (json.gameState?.action === 'chooseWorker') {
        this.setState({ action: 'chooseWorker' });
     
    }
};

  

  disableGodCardSelection = (currentPlayer) => {
    if (this.state.godCards[currentPlayer]) {
      return;
    }

    // Update the state to reflect that the current player can no longer select a god card
    this.setState(prevState => {
        return {
            godCards: {
                ...prevState.godCards,
                [currentPlayer]: 'No God Card'
            }
        };
    }, () => {
        console.log(`Updated godCards state: `, this.state.godCards);
    });
};


  chooseWorker = async (x, y) => {
    try {
      const response = await fetch(`/play?action=chooseWorker&x=${x}&y=${y}`);
      const json = await response.json();
      this.setState({ ...json, action: 'move' }); 
      this.disableGodCardSelection(json.gameState.currentPlayer);
    } catch (error) {
        console.error("Error chooseing worker:", error);
    }
  };

  
  moveWorker = async (x: number, y: number): Promise<void> => {
    try {
      const response = await fetch(`/play?action=move&x=${x}&y=${y}`);
      const json = await response.json();

      console.log('Move Worker Response:', json);
      // Handle game end state
      if (json.gameState.endState) {
        this.setState({
            winner: json.winner,
            showWinner: true,
        });
      }
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
          action: 'build'  
        });

       
      }
    } catch (error) {
      console.error('Failed to move worker:', error);
    }
  };
  

  buildBlock = async (x: number, y: number): Promise<void> => {
    try {
      const response = await fetch(`/play?action=build&x=${x}&y=${y}`);
      const json = await response.json();
  
      if (json.error) {
        alert(json.error);
      } else {
        const updatedCells = json.gameState?.cells || [];
        const currentPlayer = json.gameState?.currentPlayer || 'Player 1';
        const nextAction = json.gameState?.action || 'chooseWorker'; 
  
        const mergedCells = this.state.cells.map((cell) => {
          const updatedCell = updatedCells.find(
            (updated) => updated.x === cell.x && updated.y === cell.y
          );
          return updatedCell || cell;
        });
  
        this.setState({
          cells: mergedCells,
          currentPlayer: currentPlayer,
          action: nextAction  
        });


      }
    } catch (error) {
      console.error('Failed to build block:', error);
    }
  };

  selectGodCard = async (player, godCard) => {
    try {
        console.log(`Attempting to select God Card: ${godCard} for Player: ${player}`);
        
        const response = await fetch(`/play?action=selectGodCard&player=${player}&godCard=${godCard}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        const json = await response.json();
        console.log('Parsed JSON response:', json);

        if (!response.ok) {
            console.error('Error selecting God Card:', json.error);
            alert(json.error);
        } else {
            const godCards = { ...this.state.godCards, [player]: godCard };
            this.setState({ godCards });
            console.log('God card selection updated successfully:', json);
        }
    } catch (error) {
        console.error('Failed to select God Card:', error);
    }
};
mnn 


  renderGodCardSelection = () => {
    const { currentPlayer, godCards } = this.state;
    const godCardOptions = ["Demeter", "Hephaestus", "Minotaur", "Pan"];

    // Check if the god card selection is disabled for the current player
    // or if the current player has already selected a god card.
    if (godCards[currentPlayer] === 'No God Card' || godCards[currentPlayer]) {
        return null; // Do not render the selection if disabled or already selected
    }

    return (
        <div className="god-card-selection">
            <h4>Choose a God Card for {currentPlayer}</h4>
            {godCardOptions.map(card => (
                <button key={card} onClick={() => this.selectGodCard(currentPlayer, card)} className="god-card-btn">
                    {card}
                </button>
            ))}
        </div>
    );
  };


  renderGodCardStatus = () => {
    const { godCards } = this.state;
    const players = ["Player1", "Player2"];

    return (
        <div className="god-card-status">
            <h4>God Card Selections:</h4>
            {players.map(player => (
                <div key={player}>
                    {player}: {godCards[player] || "Not Selected Yet"} 
                </div>
            ))}
        </div>
    );
};



  handleCellClick = (cell: Cell) => {
    const { action, currentPlayer } = this.state;
  
    if (action === 'chooseWorker' && cell.player === currentPlayer) {
      this.chooseWorker(cell.x, cell.y);
    } else if (action === 'place' && !cell.worker) {
      this.placeWorker(cell.x, cell.y);
    } else if (action === 'move' && cell.playable) {
      this.moveWorker(cell.x, cell.y);
    } else if (action === 'build' && cell.playable) {
      this.buildBlock(cell.x, cell.y);
    } else {
      console.log(`Action ${action} not permitted on cell (${cell.x}, ${cell.y}).`);
    }
  };
  
  createCell = (cell, index) => {
    let cellClass = 'board-cell';
    let content = ''; // Initialize content as empty
  
    // Set cell class based on player
    if (cell.player === 'Player1') {
        cellClass += ' player-one';
    } else if (cell.player === 'Player2') {
        cellClass += ' player-two';
    }

    // Display height if available
    if (cell.height > 0) {
        content = cell.height;
    }

    // Style for domed cells (non-playable)
    if (!cell.playable) {
        cellClass += ' dome'; // Add 'dome' class for styling
        content = 'D'; // Display 'D' for dome
        return (
            <div
                key={index}
                className={cellClass}
                style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: '50px',
                    height: '50px',
                    border: '1px solid #ccc',
                    fontSize: '16px',
                    backgroundColor: 'lightblue' // Light blue background for dome
                }}
            >
                {content}
            </div>
        );
    }

    // Return cell with standard or player-specific styling
    return (
        <div
            key={index}
            className={cellClass}
            onClick={() => this.handleCellClick(cell)}
            style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                width: '50px',
                height: '50px',
                border: '1px solid #ccc',
                fontSize: '16px'
            }}
        >
            {content}
        </div>
    );
};


  renderWinnerModal = () => {
    if (!this.state.showWinner) return null;

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>Congratulations!</h2>
                <p>{this.state.winner} Wins!</p>
                <button onClick={this.closeModal}>Close</button>
            </div>
        </div>
    );
};

closeModal = () => {
    this.setState({ showWinner: false });
    // Optionally reset the game or handle additional logic here
};

  componentDidMount(): void {
    if (!this.initialized) {
      this.newGame();
      this.initialized = true;
    }
  }

  render(): React.ReactNode {
    const { cells, winner, showWinner, currentPlayer, godCards } = this.state;
    return (
        <div className="app-container">  {/* Container using flex layout */}
            <div>  {/* Main content area */}
                <h1>Santorini Game</h1>
                {showWinner ? (
                    <h2>{currentPlayer} Wins</h2>
                ) : (
                    <h2>Current Turn: {currentPlayer}</h2>
                )}
                {!godCards[currentPlayer] && this.renderGodCardSelection()}
                <div id="board">
                    {cells.map((cell, i) => this.createCell(cell, i))}
                </div>
                <div id="bottombar">
                    <button onClick={this.newGame}>New Game</button>
                </div>
            </div>
            {this.renderGodCardStatus()}  {/* Right-side God card status area */}
        </div>
    );
}



  
}

export default App;
