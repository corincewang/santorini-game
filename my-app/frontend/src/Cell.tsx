import React from 'react';
import { Cell } from './Game';

interface Props {
  cell: Cell;
}

const BoardCell: React.FC<Props> = ({ cell }) => {
  const cellStyle: React.CSSProperties = {
    border: '1px solid black',
    width: '50px',
    height: '50px',
    display: 'inline-block',
    textAlign: 'center', // Text alignment
    verticalAlign: 'middle', // Vertical alignment
    lineHeight: '50px', // Matches the height for centering
    backgroundColor: cell.worker
      ? cell.worker === 'Player 1'
        ? 'blue'
        : 'red'
      : 'white',
  };

  return (
    <div style={cellStyle}>
      {cell.worker ? `W` : cell.level > 0 ? cell.level : ''}
    </div>
  );
};

export default BoardCell;
