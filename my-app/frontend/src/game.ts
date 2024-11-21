export interface Cell {
    x: number;
    y: number;
    playable: boolean; // Indicates if this cell is playable
    action?: 'place' | 'move' | 'build'; // Action associated with the cell
    worker?: string; // Which player owns the worker on this cell, if any
    level: number; // Building level (0-3)
    player: string;
  }
  
  export interface GameState {
    cells: Cell[];
    currentPlayer: string;
    winner: string | null;
  }
  